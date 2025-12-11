package co.edu.anders.proyectoventas.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO para Dimensiones de Producto
 */
data class DimensionesDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("id_producto")
    val idProducto: Int,
    @SerializedName("ancho")
    val ancho: Double?,
    @SerializedName("espesor")
    val espesor: Double?,
    @SerializedName("diametro_interno")
    val diametroInterno: Double?,
    @SerializedName("diametro_externo")
    val diametroExterno: Double?
)

