package co.edu.anders.proyectoventas.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO para Órdenes de Producción
 */
data class OrdenProduccionDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("numero_orden")
    val numeroOrden: String,
    @SerializedName("fecha_creacion")
    val fechaCreacion: String?,
    @SerializedName("estado")
    val estado: String?
)

