package co.edu.anders.proyectoventas.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTOs para Productos (respuesta de la API)
 */
data class ProductoDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("codigo_producto")
    val codigoProducto: String,
    @SerializedName("nombre_producto")
    val nombreProducto: String,
    @SerializedName("descripcion")
    val descripcion: String?,
    @SerializedName("categoria")
    val categoria: String?,
    @SerializedName("unidad_medida")
    val unidadMedida: String?,
    @SerializedName("estado")
    val estado: String?,
    @SerializedName("dimensiones")
    val dimensiones: List<DimensionesDto>?,
    @SerializedName("inventario")
    val inventario: InventarioDto?
)

data class ProductoCreateDto(
    @SerializedName("codigo_producto")
    val codigoProducto: String,
    @SerializedName("nombre_producto")
    val nombreProducto: String,
    @SerializedName("descripcion")
    val descripcion: String?,
    @SerializedName("categoria")
    val categoria: String?,
    @SerializedName("unidad_medida")
    val unidadMedida: String?,
    @SerializedName("estado")
    val estado: String?
)

data class ProductoUpdateDto(
    @SerializedName("nombre_producto")
    val nombreProducto: String?,
    @SerializedName("descripcion")
    val descripcion: String?,
    @SerializedName("categoria")
    val categoria: String?,
    @SerializedName("unidad_medida")
    val unidadMedida: String?,
    @SerializedName("estado")
    val estado: String?
)

