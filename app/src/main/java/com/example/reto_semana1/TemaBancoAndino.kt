package com.example.reto_semana1

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Paleta oficial de Banco Andino.
// Se define una sola vez aqui para que todas las pantallas usen los mismos colores.
val AzulAndino = Color(0xFF16406B)
val AzulAndinoOscuro = Color(0xFF0D2B4E)
val AzulLogo = Color(0xFF001D4B)
val DoradoAndino = Color(0xFFE0A94D)
val GrisFondo = Color(0xFFF2F4F7)
val GrisTexto = Color(0xFF5E6B7A)
val TextoPrincipal = Color(0xFF1B2733)
val VerdeIngreso = Color(0xFF1B7A4A)
val RojoEgreso = Color(0xFFC0392B)

private val ColoresBancoAndino = lightColorScheme(
    primary = AzulAndino,
    onPrimary = Color.White,
    secondary = DoradoAndino,
    onSecondary = AzulAndinoOscuro,
    background = GrisFondo,
    onBackground = TextoPrincipal,
    surface = Color.White,
    onSurface = TextoPrincipal,
    error = RojoEgreso
)

/**
 * Tema visual de la app. Se envuelve el contenido de cada pantalla con este
 * composable para que hereden los mismos colores.
 */
@Composable
fun TemaBancoAndino(contenido: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ColoresBancoAndino,
        content = contenido
    )
}
