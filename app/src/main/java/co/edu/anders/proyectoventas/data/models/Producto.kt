package co.edu.anders.proyectoventas.data.models

data class Producto(
    val id: Int,
    val codigoProducto: String,
    val nombreProducto: String,
    val descripcion: String?,
    val categoria: String?,
    val unidadMedida: String?,
    val estado: String?,
    val dimensiones: DimensionesProducto? = null,
    val inventario: Inventario? = null
)

data class DimensionesProducto(
    val id: Int,
    val idProducto: Int,
    val ancho: Double?,
    val espesor: Double?,
    val diametroInterno: Double?,
    val diametroExterno: Double?
)

data class Inventario(
    val id: Int,
    val idProducto: Int,
    val lote: String?,
    val cantidadDisponible: Double
)

