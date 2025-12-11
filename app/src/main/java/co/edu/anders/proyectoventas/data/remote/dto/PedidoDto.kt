package co.edu.anders.proyectoventas.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTOs para Pedidos
 */
data class PedidosResponseDto(
    @SerializedName("resultado")
    val resultado: List<PedidoDto>
)

data class PedidoDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("tipo_pedido")
    val tipoPedido: String?,
    @SerializedName("id_cliente")
    val idCliente: Int,
    @SerializedName("id_vendedor")
    val idVendedor: Int,
    @SerializedName("moneda")
    val moneda: String?,
    @SerializedName("TRM")
    val trm: Double?,
    @SerializedName("OC_cliente")
    val ocCliente: String?,
    @SerializedName("condicion_pago")
    val condicionPago: String?,
    @SerializedName("direccion")
    val direccion: String?,
    @SerializedName("departamento_id")
    val departamentoId: Int?,
    @SerializedName("ciudad_id")
    val ciudadId: Int?,
    @SerializedName("created_at")
    val fechaCreacion: String?
)

data class DetallesPedidoResponseDto(
    @SerializedName("resultado")
    val resultado: List<DetallePedidoDto>
)

data class DetallePedidoDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("id_pedido")
    val idPedido: Int,
    @SerializedName("id_producto")
    val idProducto: Int,
    @SerializedName("numero_linea")
    val numeroLinea: Int?,
    @SerializedName("cantidad_solicitada")
    val cantidadSolicitada: Double,
    @SerializedName("cantidad_confirmada")
    val cantidadConfirmada: Double?,
    @SerializedName("precio_unitario")
    val precioUnitario: Double?,
    @SerializedName("precio_total")
    val precioTotal: Double?,
    @SerializedName("producto")
    val producto: ProductoDto?
)

