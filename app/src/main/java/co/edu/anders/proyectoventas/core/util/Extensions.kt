package co.edu.anders.proyectoventas.core.util

import java.text.NumberFormat
import java.util.Locale

/**
 * Extensiones útiles para el proyecto
 */

/**
 * Formatea un número como moneda colombiana
 */
fun Double.toCurrency(): String {
    return NumberFormat.getCurrencyInstance(Locale("es", "CO")).format(this)
}

/**
 * Formatea un número como moneda colombiana
 */
fun Int.toCurrency(): String {
    return this.toDouble().toCurrency()
}

/**
 * Obtiene las iniciales de un nombre completo
 */
fun String.getInitials(): String {
    return this.split(" ")
        .take(2)
        .mapNotNull { it.firstOrNull()?.uppercase() }
        .joinToString("")
}

/**
 * Verifica si una cadena no está vacía ni es nula
 */
fun String?.isNotNullOrEmpty(): Boolean {
    return !this.isNullOrEmpty()
}

/**
 * Obtiene un valor por defecto si la cadena es nula o vacía
 */
fun String?.orDefault(default: String = "N/A"): String {
    return if (this.isNullOrEmpty()) default else this
}

