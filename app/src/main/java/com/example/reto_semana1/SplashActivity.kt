package com.example.reto_semana1

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.delay

/** Cuanto tiempo se queda visible el Splash antes de pasar al Login. */
private const val DURACION_SPLASH_MS = 2500L

class SplashActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TemaBancoAndino {
                PantallaSplash(onTiempoTerminado = { abrirLogin() })
            }
        }
    }

    private fun abrirLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        // Se cierra el Splash para que el boton "atras" no vuelva a el.
        finish()
    }
}

/**
 * Pantalla de bienvenida.
 * LaunchedEffect ejecuta el bloque una sola vez al mostrarse la pantalla:
 * espera con delay y despues avisa que ya puede pasarse al Login.
 */
@Composable
fun PantallaSplash(onTiempoTerminado: () -> Unit) {

    LaunchedEffect(Unit) {
        delay(DURACION_SPLASH_MS)
        onTiempoTerminado()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AzulLogo),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.logo_banco_andino),
            contentDescription = stringResource(R.string.app_name),
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        )
    }
}
