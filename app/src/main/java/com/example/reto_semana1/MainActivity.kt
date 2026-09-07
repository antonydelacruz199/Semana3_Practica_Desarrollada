package com.example.reto_semana1

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale
import kotlin.math.abs
import kotlinx.coroutines.delay

/** Cada opcion del menu inferior: el texto que se ve y su icono. */
data class Seccion(val etiqueta: String, val icono: ImageVector)
data class Movimiento(val fecha: String, val descripcion: String, val monto: Double)
data class Credito(
    val nombre: String,
    val saldoPendiente: Double,
    val cuotaMensual: Double,
    val cuotasPagadas: Int,
    val cuotasTotales: Int
)

private const val DURACION_SPLASH_MS = 2500L
private const val USUARIO_VALIDO = "cliente1"
private const val CLAVE_VALIDA = "banco2026"

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TemaBancoAndino {
                AppBancoAndino()
            }
        }
    }
}

@Composable
fun AppBancoAndino() {
    var pantallaActual by remember { mutableStateOf("splash") }
    var nombreUsuario by remember { mutableStateOf("") }

    when (pantallaActual) {
        "splash" -> PantallaSplash(
            onTiempoTerminado = { pantallaActual = "login" }
        )

        "login" -> PantallaLogin(
            onLoginCorrecto = { usuario ->
                nombreUsuario = usuario
                pantallaActual = "principal"
            }
        )

        else -> PantallaPrincipal(
            nombreUsuario = nombreUsuario,
            onCerrarSesion = {
                nombreUsuario = ""
                pantallaActual = "login"
            }
        )
    }
}

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

@Composable
fun PantallaLogin(onLoginCorrecto: (String) -> Unit) {
    var usuario by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf("") }

    val errorCamposVacios = stringResource(R.string.error_campos_vacios)
    val errorCredenciales = stringResource(R.string.error_credenciales)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
    ) {
        CabeceraLogin()

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
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )

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
            when (seccionSeleccionada) {
                0 -> SeccionInicio(nombreUsuario)
                1 -> SeccionCuentas()
                2 -> SeccionCreditos()
                else -> SeccionMas(nombreUsuario, onCerrarSesion)
            }
        }
    }
}

@Composable
fun SeccionInicio(nombreUsuario: String) {
    val contexto = LocalContext.current
    val textoProximamente = stringResource(R.string.proximamente)

    val operaciones = listOf(
        stringResource(R.string.op_transferir),
        stringResource(R.string.op_pagar),
        stringResource(R.string.op_recargar),
        stringResource(R.string.op_retirar)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.bienvenido_formato, nombreUsuario),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.saldo_disponible),
                    color = GrisTexto
                )
                Text(
                    text = formatearMoneda(DatosDemo.saldoAhorros),
                    color = AzulAndino,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.cuenta_ahorros),
                    color = GrisTexto,
                    fontSize = 13.sp
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.operaciones_frecuentes),
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(12.dp))

        for (fila in operaciones.chunked(2)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                for (operacion in fila) {
                    OutlinedButton(
                        onClick = {
                            Toast.makeText(
                                contexto,
                                "$operacion: $textoProximamente",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                    ) {
                        Text(operacion, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun SeccionCuentas() {
    Column(modifier = Modifier.fillMaxSize()) {
        CabeceraAzul(
            titulo = stringResource(R.string.cuenta_ahorros),
            monto = formatearMoneda(DatosDemo.saldoAhorros)
        )

        Text(
            text = stringResource(R.string.movimientos_recientes),
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(DatosDemo.obtenerMovimientos()) { movimiento ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(movimiento.descripcion, fontWeight = FontWeight.Bold)
                            Text(movimiento.fecha, color = GrisTexto, fontSize = 13.sp)
                        }
                        val esIngreso = movimiento.monto >= 0
                        Text(
                            text = formatearMonedaConSigno(movimiento.monto),
                            fontWeight = FontWeight.Bold,
                            color = if (esIngreso) VerdeIngreso else RojoEgreso
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SeccionCreditos() {
    Column(modifier = Modifier.fillMaxSize()) {
        CabeceraAzul(
            titulo = stringResource(R.string.creditos_titulo),
            monto = formatearMoneda(DatosDemo.calcularDeudaTotal())
        )

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(DatosDemo.obtenerCreditos()) { credito ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(credito.nombre, fontWeight = FontWeight.Bold)
                        Text(
                            text = formatearMoneda(credito.saldoPendiente),
                            color = AzulAndino,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        Text(
                            text = "Cuota: " + formatearMoneda(credito.cuotaMensual),
                            color = GrisTexto,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Pagadas: ${credito.cuotasPagadas} de ${credito.cuotasTotales}",
                            color = GrisTexto,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SeccionMas(nombreUsuario: String, onCerrarSesion: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))

        Text(
            text = nombreUsuario,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.mas_cliente),
            color = GrisTexto
        )

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = onCerrarSesion,
            colors = ButtonDefaults.buttonColors(
                containerColor = DoradoAndino,
                contentColor = AzulAndinoOscuro
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text(
                text = stringResource(R.string.btn_cerrar_sesion),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun CabeceraLogin() {
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

@Composable
private fun CabeceraAzul(titulo: String, monto: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AzulAndino)
            .padding(16.dp)
    ) {
        Text(
            text = titulo,
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 14.sp
        )
        Text(
            text = monto,
            color = DoradoAndino,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

private object DatosDemo {
    const val saldoAhorros = 2340.00

    fun obtenerMovimientos(): List<Movimiento> = listOf(
        Movimiento("20/08/2026", "Depósito en ventanilla", 350.00),
        Movimiento("18/08/2026", "Retiro cajero", -120.00),
        Movimiento("15/08/2026", "Pago de servicio", -45.50),
        Movimiento("12/08/2026", "Transferencia recibida", 500.00),
        Movimiento("10/08/2026", "Compra supermercado", -89.90),
        Movimiento("08/08/2026", "Abono de planilla", 1800.00)
    )

    fun obtenerCreditos(): List<Credito> = listOf(
        Credito("Crédito vehicular", 12500.00, 620.40, 8, 36),
        Credito("Crédito personal", 3200.00, 285.00, 5, 18),
        Credito("Tarjeta de crédito", 1450.75, 150.00, 2, 12)
    )

    fun calcularDeudaTotal(): Double {
        var total = 0.0
        for (credito in obtenerCreditos()) {
            total += credito.saldoPendiente
        }
        return total
    }
}

private fun formatearMoneda(monto: Double): String {
    return String.format(Locale.US, "S/ %,.2f", monto)
}

private fun formatearMonedaConSigno(monto: Double): String {
    val signo = if (monto >= 0) "+ " else "- "
    return signo + formatearMoneda(abs(monto))
}
