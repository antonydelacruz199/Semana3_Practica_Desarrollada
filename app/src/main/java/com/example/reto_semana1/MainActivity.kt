package com.example.reto_semana1

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource

/** Cada opcion del menu inferior: el texto que se ve y su icono. */
data class Seccion(val etiqueta: String, val icono: ImageVector)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Se recibe el nombre que envio LoginActivity por el Intent.
        val nombreUsuario = intent.getStringExtra(LoginActivity.EXTRA_USUARIO) ?: "cliente"

        setContent {
            TemaBancoAndino {
                PantallaPrincipal(
                    nombreUsuario = nombreUsuario,
                    onCerrarSesion = { cerrarSesion() }
                )
            }
        }
    }

    /** Vuelve al Login y limpia las pantallas anteriores. */
    private fun cerrarSesion() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPrincipal(nombreUsuario: String, onCerrarSesion: () -> Unit) {

    // Guarda que pestaña esta activa. Al cambiar, Compose redibuja el contenido.
    var seccionSeleccionada by remember { mutableStateOf(0) }

    val secciones = listOf(
        Seccion(stringResource(R.string.menu_inicio), Icons.Default.Home),
        Seccion(stringResource(R.string.menu_cuentas), Icons.Default.AccountBalanceWallet),
        Seccion(stringResource(R.string.menu_creditos), Icons.Default.CreditCard),
        Seccion(stringResource(R.string.menu_mas), Icons.Default.MoreHoriz)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AzulAndino,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            NavigationBar {
                secciones.forEachIndexed { indice, seccion ->
                    NavigationBarItem(
                        selected = seccionSeleccionada == indice,
                        onClick = { seccionSeleccionada = indice },
                        icon = { Icon(seccion.icono, contentDescription = seccion.etiqueta) },
                        label = { Text(seccion.etiqueta) }
                    )
                }
            }
        }
    ) { espacioInterno ->
        Box(modifier = Modifier.padding(espacioInterno)) {
            // Segun la pestaña elegida se muestra una pantalla distinta.
            when (seccionSeleccionada) {
                0 -> SeccionInicio(nombreUsuario)
                1 -> SeccionCuentas()
                2 -> SeccionCreditos()
                else -> SeccionMas(nombreUsuario, onCerrarSesion)
            }
        }
    }
}
