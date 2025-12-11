package co.edu.anders.proyectoventas.data.repository

import android.util.Log
import co.edu.anders.proyectoventas.data.remote.NodeApiClient

/**
 * Repositorio para obtener información de ubicación (departamentos y ciudades)
 * desde la API de Node.js
 */
class LocationRepository {
    
    private val nodeApi = NodeApiClient.nodeApiService
    
    /**
     * Obtiene el nombre de un departamento por su ID
     */
    suspend fun getDepartamentoNombre(departamentoId: Int): Result<String?> {
        return try {
            Log.d("LocationRepository", "Obteniendo departamento con ID: $departamentoId")
            val response = nodeApi.getDepartamentoById(departamentoId)
            if (response.isSuccessful && response.body() != null) {
                val data = response.body()!!.data
                if (data.isNotEmpty()) {
                    val nombre = data[0].nombre
                    Log.d("LocationRepository", "Departamento encontrado: $nombre")
                    Result.success(nombre)
                } else {
                    Log.w("LocationRepository", "No se encontró departamento con ID: $departamentoId")
                    Result.success(null)
                }
            } else {
                val error = "Error al obtener departamento: ${response.code()} ${response.message()}"
                Log.e("LocationRepository", error)
                Result.failure(Exception(error))
            }
        } catch (e: Exception) {
            Log.e("LocationRepository", "Excepción al obtener departamento: ${e.message}", e)
            Result.failure(e)
        }
    }
    
    /**
     * Obtiene el nombre de una ciudad por su ID
     */
    suspend fun getCiudadNombre(ciudadId: Int): Result<String?> {
        return try {
            Log.d("LocationRepository", "Obteniendo ciudad con ID: $ciudadId")
            val response = nodeApi.getCiudadById(ciudadId)
            Log.d("LocationRepository", "Respuesta ciudad - Código: ${response.code()}, Exitosa: ${response.isSuccessful}")
            
            if (response.isSuccessful) {
                val body = response.body()
                Log.d("LocationRepository", "Body de respuesta ciudad: $body")
                
                if (body != null) {
                    val data = body.data
                    Log.d("LocationRepository", "Datos de ciudad recibidos: ${data.size} elementos")
                    
                    if (data.isNotEmpty()) {
                        val ciudad = data[0]
                        val nombre = ciudad.nombre
                        Log.d("LocationRepository", "Ciudad encontrada: $nombre (ID: ${ciudad.id})")
                        Result.success(nombre)
                    } else {
                        Log.w("LocationRepository", "Array de datos vacío para ciudad ID: $ciudadId")
                        Result.success(null)
                    }
                } else {
                    Log.e("LocationRepository", "Body de respuesta es null para ciudad ID: $ciudadId")
                    Result.failure(Exception("Respuesta vacía del servidor"))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                val error = "Error al obtener ciudad: ${response.code()} ${response.message()}. Body: $errorBody"
                Log.e("LocationRepository", error)
                Result.failure(Exception(error))
            }
        } catch (e: Exception) {
            Log.e("LocationRepository", "Excepción al obtener ciudad ID $ciudadId: ${e.message}", e)
            Log.e("LocationRepository", "Stack trace: ${e.stackTraceToString()}")
            Result.failure(e)
        }
    }
    
    /**
     * Obtiene los nombres de departamento y ciudad simultáneamente
     */
    suspend fun getUbicacionNombres(departamentoId: Int?, ciudadId: Int?): Pair<String?, String?> {
        Log.d("LocationRepository", "Obteniendo ubicación - Departamento ID: $departamentoId, Ciudad ID: $ciudadId")
        
        val departamentoNombre = departamentoId?.let { 
            val result = getDepartamentoNombre(it)
            if (result.isSuccess) {
                result.getOrNull()
            } else {
                Log.e("LocationRepository", "Error al obtener departamento: ${result.exceptionOrNull()?.message}")
                null
            }
        } ?: run {
            Log.w("LocationRepository", "Departamento ID es null")
            null
        }
        
        val ciudadNombre = ciudadId?.let { 
            val result = getCiudadNombre(it)
            if (result.isSuccess) {
                result.getOrNull()
            } else {
                Log.e("LocationRepository", "Error al obtener ciudad: ${result.exceptionOrNull()?.message}")
                null
            }
        } ?: run {
            Log.w("LocationRepository", "Ciudad ID es null")
            null
        }
        
        Log.d("LocationRepository", "Resultado - Departamento: $departamentoNombre, Ciudad: $ciudadNombre")
        return Pair(departamentoNombre, ciudadNombre)
    }
}

