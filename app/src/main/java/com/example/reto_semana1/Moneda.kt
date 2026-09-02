package com.example.reto_semana1

import java.util.Locale
import kotlin.math.abs

/** Da formato de soles a un monto: 2340.0 se convierte en "S/ 2,340.00". */
fun formatearMoneda(monto: Double): String {
    return String.format(Locale.US, "S/ %,.2f", monto)
}

/** Igual que formatearMoneda, pero anteponiendo + si es ingreso y - si es egreso. */
fun formatearMonedaConSigno(monto: Double): String {
    val signo = if (monto >= 0) "+ " else "- "
    return signo + formatearMoneda(abs(monto))
}
