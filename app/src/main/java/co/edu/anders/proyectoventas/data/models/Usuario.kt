package co.edu.anders.proyectoventas.data.models

data class Usuario(
    val id: Int,
    val nombres: String,
    val apellidos: String,
    val email: String,
    val telefono: String?,
    val cedula: String,
    val rolId: Int?,
    val estado: String?,
    val departamentoNombre: String? = null,
    val ciudadNombre: String? = null,
    val rol: Rol? = null
)

data class Rol(
    val id: Int,
    val nombre: String,
    val descripcion: String?,
    val estado: String?
)

