package co.edu.anders.proyectoventas.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTOs para autenticación
 */
data class LoginRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)

data class LoginResponse(
    @SerializedName("usuario")
    val usuario: UserDto?,
    @SerializedName("rol")
    val rol: RolLoginDto?,
    @SerializedName("menu")
    val menu: List<Any>? = null,  // Opcional
    @SerializedName("modulos")
    val modulos: List<Any>? = null,  // Opcional
    @SerializedName("favoritos")
    val favoritos: List<Any>? = null  // Opcional
)

data class UserDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("email")
    val email: String,
    @SerializedName("nombres")
    val nombres: String?,
    @SerializedName("apellidos")
    val apellidos: String?,
    @SerializedName("rol_id")
    val rolId: Int?,
    @SerializedName("telefono")
    val telefono: String?,
    @SerializedName("cedula")
    val cedula: String?,
    @SerializedName("estado")
    val estado: String?,
    @SerializedName("ciudad_id")
    val ciudadId: Int?,
    @SerializedName("departamento_id")
    val departamentoId: Int?,
    @SerializedName("ciudad_nombre")
    val ciudadNombre: String?,
    @SerializedName("departamento_nombre")
    val departamentoNombre: String?,
    @SerializedName("ciudad")
    val ciudad: CiudadSimpleDto?,
    @SerializedName("departamento")
    val departamento: DepartamentoSimpleDto?
)

// CiudadDto y DepartamentoDto están definidos en NodeApiDto.kt

data class RolLoginDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("nombre")
    val nombre: String,
    @SerializedName("descripcion")
    val descripcion: String?,
    @SerializedName("estado")
    val estado: String?
)

