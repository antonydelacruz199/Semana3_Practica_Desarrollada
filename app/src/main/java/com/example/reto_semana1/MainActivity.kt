package com.example.reto_semana1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
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
data class CuentaAhorro(
    val nombre: String,
    val numero: String,
    val saldo: Double,
    val movimientos: List<Movimiento>
)
data class Movimiento(val fecha: String, val descripcion: String, val monto: Double)
data class Credito(
    val tipo: String,
    val montoTotal: Double,
    val cuotaMensual: Double,
    val proximoPago: String
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
    var menuExpandido by remember { mutableStateOf(false) }
    var seccionSeleccionada by remember { mutableStateOf(0) }

    val secciones = listOf(
        Seccion(stringResource(R.string.menu_inicio), Icons.Default.Home),
        Seccion(stringResource(R.string.menu_cuentas), Icons.Default.AccountBalanceWallet),
        Seccion(stringResource(R.string.menu_creditos), Icons.Default.CreditCard),
        Seccion(stringResource(R.string.menu_perfil), Icons.Default.Person)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    Box {
                        IconButton(onClick = { menuExpandido = true }) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = stringResource(R.string.abrir_menu_usuario)
                            )
                        }
                        DropdownMenu(
                            expanded = menuExpandido,
                            onDismissRequest = { menuExpandido = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.menu_mi_perfil)) },
                                onClick = {
                                    seccionSeleccionada = 3
                                    menuExpandido = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.btn_cerrar_sesion)) },
                                onClick = {
                                    menuExpandido = false
                                    onCerrarSesion()
                                }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AzulAndino,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
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
                else -> SeccionPerfil(nombreUsuario, onCerrarSesion)
            }
        }
    }
}

@Composable
fun SeccionInicio(nombreUsuario: String) {
    val totalAhorros = DatosDemo.obtenerTotalAhorros()
    val progresoAhorro = 0.72f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text(
            text = stringResource(R.string.bienvenido_formato, nombreUsuario),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = AzulAndino)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.total_ahorros),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = formatearMoneda(totalAhorros),
                    color = DoradoAndino,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        Text(
            text = stringResource(R.string.mis_cuentas),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))

        DatosDemo.obtenerCuentasAhorro().forEach { cuenta ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(cuenta.nombre, fontWeight = FontWeight.Medium)
                    Text(cuenta.numero, color = GrisTexto, fontSize = 12.sp)
                }
                Text(formatearMoneda(cuenta.saldo), fontWeight = FontWeight.Bold)
            }
        }

        Spacer(Modifier.height(24.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Canvas(modifier = Modifier.height(120.dp).fillMaxWidth()) {
                val diametro = minOf(size.width, size.height)
                val grosor = 22f
                val izquierda = (size.width - diametro) / 2f
                val arriba = (size.height - diametro) / 2f

                drawArc(
                    color = GrisTexto.copy(alpha = 0.2f),
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = androidx.compose.ui.geometry.Offset(izquierda, arriba),
                    size = androidx.compose.ui.geometry.Size(diametro, diametro),
                    style = Stroke(width = grosor)
                )
                drawArc(
                    color = VerdeIngreso,
                    startAngle = -90f,
                    sweepAngle = 360f * progresoAhorro,
                    useCenter = false,
                    topLeft = androidx.compose.ui.geometry.Offset(izquierda, arriba),
                    size = androidx.compose.ui.geometry.Size(diametro, diametro),
                    style = Stroke(width = grosor)
                )
            }
            Text(
                text = stringResource(R.string.progreso_ahorro),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun SeccionCuentas() {
    var cuentaSeleccionada by remember { mutableStateOf<CuentaAhorro?>(null) }
    val cuenta = cuentaSeleccionada

    if (cuenta == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Text(
                text = stringResource(R.string.mis_cuentas_ahorro),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(
                    R.string.total_cuentas_formato,
                    formatearMoneda(DatosDemo.obtenerTotalAhorros())
                ),
                fontSize = 14.sp,
                color = GrisTexto
            )
            Spacer(Modifier.height(16.dp))

            DatosDemo.obtenerCuentasAhorro().forEach { cuentaActual ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable { cuentaSeleccionada = cuentaActual },
                    colors = CardDefaults.cardColors(containerColor = AzulAndino)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            text = cuentaActual.nombre,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 14.sp
                        )
                        Text(
                            text = formatearMoneda(cuentaActual.saldo),
                            color = DoradoAndino,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = cuentaActual.numero,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { cuentaSeleccionada = null }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.volver_cuentas)
                    )
                }
                Spacer(Modifier.width(4.dp))
                Column {
                    Text(cuenta.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(
                        text = formatearMoneda(cuenta.saldo),
                        color = AzulAndino,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            HorizontalDivider()

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
            ) {
                if (cuenta.movimientos.isEmpty()) {
                    Text(
                        text = stringResource(R.string.sin_movimientos),
                        modifier = Modifier.padding(vertical = 20.dp),
                        color = GrisTexto
                    )
                } else {
                    cuenta.movimientos.forEach { movimiento ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(movimiento.descripcion)
                                Text(movimiento.fecha, fontSize = 12.sp, color = GrisTexto)
                            }
                            Text(
                                text = formatearMonedaConSigno(movimiento.monto),
                                fontWeight = FontWeight.Bold,
                                color = if (movimiento.monto >= 0) VerdeIngreso else RojoEgreso
                            )
                        }
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
fun SeccionCreditos() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text(
            text = stringResource(R.string.mis_creditos),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(16.dp))

        DatosDemo.obtenerCreditos().forEach { credito ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                colors = CardDefaults.cardColors(containerColor = AzulAndinoOscuro)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        text = credito.tipo,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 14.sp
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = stringResource(
                            R.string.deuda_formato,
                            formatearMoneda(credito.montoTotal)
                        ),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = stringResource(
                            R.string.cuota_proximo_pago_formato,
                            formatearMoneda(credito.cuotaMensual),
                            credito.proximoPago
                        ),
                        color = DoradoAndino,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun SeccionPerfil(nombreUsuario: String, onCerrarSesion: () -> Unit) {
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
            text = stringResource(R.string.perfil_cliente),
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
    fun obtenerCuentasAhorro(): List<CuentaAhorro> = listOf(
        CuentaAhorro(
            nombre = "Cuenta Sueldo",
            numero = "191-000123",
            saldo = 2340.00,
            movimientos = listOf(
                Movimiento("20/08/2026", "Depósito en ventanilla", 350.00),
                Movimiento("18/08/2026", "Retiro cajero", -120.00),
                Movimiento("15/08/2026", "Pago de servicio", -45.50),
                Movimiento("12/08/2026", "Transferencia recibida", 500.00)
            )
        ),
        CuentaAhorro(
            nombre = "Cuenta Vacaciones",
            numero = "191-000456",
            saldo = 980.00,
            movimientos = listOf(
                Movimiento("16/08/2026", "Depósito programado", 200.00),
                Movimiento("09/08/2026", "Depósito programado", 200.00),
                Movimiento("02/08/2026", "Depósito programado", 200.00)
            )
        ),
        CuentaAhorro(
            nombre = "Cuenta Emergencia",
            numero = "191-000789",
            saldo = 500.00,
            movimientos = emptyList()
        )
    )

    fun obtenerTotalAhorros(): Double {
        return obtenerCuentasAhorro().sumOf { it.saldo }
    }

    fun obtenerCreditos(): List<Credito> = listOf(
        Credito("Préstamo personal", 5000.00, 350.00, "05/10/2026"),
        Credito("Tarjeta de crédito", 1850.00, 180.00, "15/10/2026"),
        Credito("Crédito vehicular", 12800.00, 620.00, "28/10/2026")
    )
}

private fun formatearMoneda(monto: Double): String {
    return String.format(Locale.US, "S/ %,.2f", monto)
}

private fun formatearMonedaConSigno(monto: Double): String {
    val signo = if (monto >= 0) "+ " else "- "
    return signo + formatearMoneda(abs(monto))
}
