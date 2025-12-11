package co.edu.anders.proyectoventas.core.util

import java.text.NumberFormat
import java.util.Locale

/**
 * Utilidades para formatear datos
 */
object Formatters {
    /**
     * Formateador de moneda colombiana
     */
    val currencyFormatter: NumberFormat = NumberFormat.getCurrencyInstance(Locale("es", "CO"))
    
    /**
     * Formatea un valor como moneda
     */
    fun formatCurrency(value: Double): String {
        return currencyFormatter.format(value)
    }
    
    /**
     * Formatea un valor como moneda
     */
    fun formatCurrency(value: Int): String {
        return currencyFormatter.format(value)
    }
    
    /**
     * Formatea un valor como moneda
     */
    fun formatCurrency(value: Long): String {
        return currencyFormatter.format(value)
    }
}

