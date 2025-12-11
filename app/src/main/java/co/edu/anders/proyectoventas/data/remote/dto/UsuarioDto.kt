package co.edu.anders.proyectoventas.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTOs para Usuarios completo
 */
data class UsuarioDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("nombres")
    val nombres: String,
    @SerializedName("apellidos")
    val apellidos: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("telefono")
    val telefono: String?,
    @SerializedName("cedula")
    val cedula: String,
    @SerializedName("rol_id")
    val rolId: Int,
    @SerializedName("estado")
    val estado: String?,
    @SerializedName("departamento_id")
    val departamentoId: Int?,
    @SerializedName("ciudad_id")
    val ciudadId: Int?,
    @SerializedName("departamento_nombre")
    val departamentoNombre: String?,
    @SerializedName("ciudad_nombre")
    val ciudadNombre: String?,
    @SerializedName("atributos")
    val atributos: Map<String, String>?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("rol")
    val rol: RolDto?
)

data class UsuarioCreateDto(
    @SerializedName("nombres")
    val nombres: String,
    @SerializedName("apellidos")
    val apellidos: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("telefono")
    val telefono: String?,
    @SerializedName("cedula")
    val cedula: String,
    @SerializedName("contrasena")
    val contrasena: String,
    @SerializedName("rol_id")
    val rolId: Int,
    @SerializedName("estado")
    val estado: String = "Activo",
    @SerializedName("departamento_id")
    val departamentoId: Int?,
    @SerializedName("ciudad_id")
    val ciudadId: Int?
)

data class UsuarioUpdateDto(
    @SerializedName("nombres")
    val nombres: String?,
    @SerializedName("apellidos")
    val apellidos: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("telefono")
    val telefono: String?,
    @SerializedName("estado")
    val estado: String?,
    @SerializedName("departamento_id")
    val departamentoId: Int?,
    @SerializedName("ciudad_id")
    val ciudadId: Int?
)

