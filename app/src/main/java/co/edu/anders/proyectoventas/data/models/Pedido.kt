package co.edu.anders.proyectoventas.data.models

data class Pedido(
    val id: Int,
    val tipoPedido: String?,
    val idCliente: Int,
    val idVendedor: Int,
    val moneda: String?,
    val trm: Double?,
    val ocCliente: String?,
    val condicionPago: String?,
    val direccion: String? = null,
    val departamentoId: Int? = null,
    val ciudadId: Int? = null,
    val departamentoNombre: String? = null,
    val ciudadNombre: String? = null,
    val detalles: List<DetallePedido> = emptyList(),
    val fechaCreacion: String? = null
)

data class DetallePedido(
    val id: Int,
    val idPedido: Int,
    val idProducto: Int,
    val numeroLinea: Int?,
    val cantidadSolicitada: Double,
    val cantidadConfirmada: Double?,
    val precioUnitario: Double?,
    val precioTotal: Double?,
    val producto: Producto? = null
)

