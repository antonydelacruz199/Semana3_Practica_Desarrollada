package com.example.reto_semana1

/**
 * Datos de ejemplo de la app.
 * Mientras no exista base de datos (Unidad 2), la informacion sale de aqui,
 * asi las pantallas solo se encargan de mostrarla.
 */
object DatosDemo {

    const val SALDO_AHORROS = 2340.00

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

    /** Suma el saldo pendiente de todos los creditos del cliente. */
    fun calcularDeudaTotal(): Double {
        var total = 0.0
        for (credito in obtenerCreditos()) {
            total += credito.saldoPendiente
        }
        return total
    }
}
