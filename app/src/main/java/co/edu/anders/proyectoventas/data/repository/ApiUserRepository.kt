package co.edu.anders.proyectoventas.data.repository

import co.edu.anders.proyectoventas.data.models.Usuario
import co.edu.anders.proyectoventas.data.remote.RetrofitClient
import co.edu.anders.proyectoventas.data.remote.mapper.toDomain

/**
 * Repositorio de usuarios que usa la API REST
 */
class ApiUserRepository {
    
    private val api = RetrofitClient.apiService
    
    /**
     * Obtiene todos los usuarios desde la API
     */
    suspend fun getAllUsers(): Result<List<Usuario>> {
        return try {
            val response = api.listarUsuarios()
            if (response.isSuccessful && response.body() != null) {
                val usuarios = response.body()!!.map { it.toDomain() }
                Result.success(usuarios)
            } else {
                Result.failure(Exception("Error al obtener usuarios: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Obtiene un usuario por ID
     */
    suspend fun getUserById(id: Int): Result<Usuario> {
        return try {
            val response = api.obtenerUsuario(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toDomain())
            } else {
                Result.failure(Exception("Usuario no encontrado"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Obtiene el usuario actual (simulado por ahora)
     */
    suspend fun getCurrentUser(): Result<Usuario> {
        // Por ahora retorna el primer usuario
        // TODO: Implementar con el ID del usuario logueado
        return try {
            val result = getAllUsers()
            if (result.isSuccess) {
                val usuarios = result.getOrNull()
                if (usuarios != null && usuarios.isNotEmpty()) {
                    Result.success(usuarios.first())
                } else {
                    Result.failure(Exception("No hay usuarios"))
                }
            } else {
                // Convertir el error de Result<List<Usuario>> a Result<Usuario>
                Result.failure(result.exceptionOrNull() ?: Exception("Error desconocido"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

