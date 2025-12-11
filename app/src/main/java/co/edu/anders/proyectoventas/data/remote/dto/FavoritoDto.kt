package co.edu.anders.proyectoventas.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTOs para Favoritos
 */
data class FavoritoDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("usuario_id")
    val usuarioId: Int,
    @SerializedName("producto_id")
    val productoId: Int,
    @SerializedName("rol_id")
    val rolId: Int?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("producto")
    val producto: ProductoDto?
)

data class FavoritoCreateDto(
    @SerializedName("producto_id")
    val productoId: Int
)

data class FavoritoUpdateDto(
    @SerializedName("producto_id")
    val productoId: Int?
)

