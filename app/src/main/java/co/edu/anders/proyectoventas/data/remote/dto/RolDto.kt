package co.edu.anders.proyectoventas.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTOs para Roles
 */
data class RolDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("nombre")
    val nombre: String,
    @SerializedName("descripcion")
    val descripcion: String?,
    @SerializedName("estado")
    val estado: String?
)

data class RolCreateDto(
    @SerializedName("nombre")
    val nombre: String,
    @SerializedName("descripcion")
    val descripcion: String?,
    @SerializedName("estado")
    val estado: String?
)

data class RolUpdateDto(
    @SerializedName("nombre")
    val nombre: String?,
    @SerializedName("descripcion")
    val descripcion: String?,
    @SerializedName("estado")
    val estado: String?
)

