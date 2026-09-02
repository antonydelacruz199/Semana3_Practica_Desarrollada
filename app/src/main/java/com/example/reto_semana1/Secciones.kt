package com.example.reto_semana1

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ---------------------------------------------------------------- Inicio

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
                    text = formatearMoneda(DatosDemo.SALDO_AHORROS),
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

        // Las operaciones se muestran de dos en dos.
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

// ---------------------------------------------------------------- Cuentas

@Composable
fun SeccionCuentas() {
    Column(modifier = Modifier.fillMaxSize()) {

        CabeceraAzul(
            titulo = stringResource(R.string.cuenta_ahorros),
            monto = formatearMoneda(DatosDemo.SALDO_AHORROS)
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

// ---------------------------------------------------------------- Créditos

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

// ---------------------------------------------------------------- Más

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

// ---------------------------------------------------------------- Común

/** Cabecera azul con un titulo y un monto grande, reutilizada por varias secciones. */
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
