package com.example.reto_semana1

/** Un movimiento de la cuenta. El monto es positivo si entra dinero y negativo si sale. */
data class Movimiento(
    val fecha: String,
    val descripcion: String,
    val monto: Double
)

/** Un credito contratado por el cliente. */
data class Credito(
    val nombre: String,
    val saldoPendiente: Double,
    val cuotaMensual: Double,
    val cuotasPagadas: Int,
    val cuotasTotales: Int
)
