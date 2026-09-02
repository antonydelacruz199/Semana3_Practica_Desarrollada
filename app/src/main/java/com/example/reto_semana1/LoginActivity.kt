package com.example.reto_semana1

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Credenciales fijas en el codigo, todavia no hay base de datos.
private const val USUARIO_VALIDO = "cliente1"
private const val CLAVE_VALIDA = "banco2026"

class LoginActivity : ComponentActivity() {

    companion object {
        /** Clave con la que se envia el usuario a la pantalla principal. */
        const val EXTRA_USUARIO = "nombreUsuario"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TemaBancoAndino {
                PantallaLogin(onLoginCorrecto = { usuario -> abrirPantallaPrincipal(usuario) })
            }
        }
    }

    /** Navega a MainActivity pasandole el nombre del usuario mediante un Intent. */
    private fun abrirPantallaPrincipal(usuario: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(EXTRA_USUARIO, usuario)
        startActivity(intent)
    }
}

@Composable
fun PantallaLogin(onLoginCorrecto: (String) -> Unit) {

    // remember + mutableStateOf guardan lo que el usuario escribe y hacen que
    // la pantalla se vuelva a dibujar cada vez que el valor cambia.
    var usuario by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf("") }

    // Los textos se leen aqui porque stringResource no puede usarse dentro del onClick.
    val errorCamposVacios = stringResource(R.string.error_campos_vacios)
    val errorCredenciales = stringResource(R.string.error_credenciales)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
    ) {
        Cabecera()

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {

                OutlinedTextField(
                    value = usuario,
                    onValueChange = {
                        usuario = it
                        mensajeError = ""
                    },
                    label = { Text(stringResource(R.string.hint_usuario)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = clave,
                    onValueChange = {
                        clave = it
                        mensajeError = ""
                    },
                    label = { Text(stringResource(R.string.hint_clave)) },
                    singleLine = true,
                    // Oculta la clave mostrando puntos en lugar de las letras.
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )

                // El mensaje de error solo ocupa espacio cuando hay algo que avisar.
                if (mensajeError.isNotEmpty()) {
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = mensajeError,
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = {
                        // Validacion con if / else, tal como pide el reto.
                        if (usuario.isBlank() || clave.isBlank()) {
                            mensajeError = errorCamposVacios
                        } else if (usuario != USUARIO_VALIDO || clave != CLAVE_VALIDA) {
                            mensajeError = errorCredenciales
                        } else {
                            mensajeError = ""
                            onLoginCorrecto(usuario)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DoradoAndino,
                        contentColor = AzulAndinoOscuro
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text(
                        text = stringResource(R.string.btn_ingresar),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun Cabecera() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AzulAndino)
            .statusBarsPadding()
            .padding(horizontal = 24.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.app_name),
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.login_subtitulo),
            color = DoradoAndino,
            fontSize = 16.sp
        )
    }
}
