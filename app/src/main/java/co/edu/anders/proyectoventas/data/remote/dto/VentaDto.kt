package co.edu.anders.proyectoventas.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTOs para Ventas
 */
data class VentaDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("usuario_id")
    val usuarioId: Int,
    @SerializedName("pedido_id")
    val pedidoId: Int?,
    @SerializedName("total")
    val total: Double,
    @SerializedName("fecha_venta")
    val fechaVenta: String?,
    @SerializedName("estado")
    val estado: String?,
    @SerializedName("created_at")
    val createdAt: String?
)

data class VentaCreateDto(
    @SerializedName("usuario_id")
    val usuarioId: Int,
    @SerializedName("pedido_id")
    val pedidoId: Int?,
    @SerializedName("total")
    val total: Double,
    @SerializedName("fecha_venta")
    val fechaVenta: String?
)

data class VentaUpdateDto(
    @SerializedName("total")
    val total: Double?,
    @SerializedName("estado")
    val estado: String?,
    @SerializedName("fecha_venta")
    val fechaVenta: String?
)

