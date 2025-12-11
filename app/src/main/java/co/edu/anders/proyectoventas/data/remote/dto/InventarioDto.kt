package co.edu.anders.proyectoventas.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO para Inventario
 */
data class InventarioDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("id_producto")
    val idProducto: Int,
    @SerializedName("lote")
    val lote: String?,
    @SerializedName("cantidad_disponible")
    val cantidadDisponible: Double
)

