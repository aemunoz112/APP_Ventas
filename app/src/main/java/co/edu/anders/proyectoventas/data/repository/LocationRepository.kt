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
            
            // Manejar errores 429 (rate limit) y 200 con HTML (redirección a GitHub)
            if (response.code() == 429) {
                Log.w("LocationRepository", "Rate limit (429) al obtener departamento $departamentoId. Dev Tunnels requiere autenticación.")
                return Result.success(null)
            }
            
            // Verificar si el Content-Type es HTML (indica redirección a GitHub)
            val contentType = response.headers()["Content-Type"] ?: ""
            if (contentType.contains("text/html")) {
                Log.w("LocationRepository", "Respuesta HTML recibida para departamento $departamentoId (Dev Tunnels requiere autenticación GitHub)")
                return Result.success(null)
            }
            
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
                // En caso de error, retornar null en lugar de fallar para no bloquear la UI
                Result.success(null)
            }
        } catch (e: com.google.gson.stream.MalformedJsonException) {
            // Dev Tunnels está devolviendo HTML (página de login de GitHub) en lugar de JSON
            Log.w("LocationRepository", "Respuesta HTML recibida (Dev Tunnels requiere autenticación) para departamento $departamentoId")
            Result.success(null)
        } catch (e: Exception) {
            // Solo loggear el mensaje, no el stack trace completo para evitar spam
            val errorMsg = e.message ?: e.javaClass.simpleName
            if (errorMsg.contains("MalformedJsonException") || errorMsg.contains("HTML")) {
                Log.w("LocationRepository", "Respuesta no-JSON recibida para departamento $departamentoId (probablemente HTML de autenticación)")
            } else {
                Log.e("LocationRepository", "Excepción al obtener departamento: $errorMsg")
            }
            // En caso de excepción, retornar null en lugar de fallar
            Result.success(null)
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
            
            // Manejar errores 429 (rate limit) y 200 con HTML (redirección a GitHub)
            if (response.code() == 429) {
                Log.w("LocationRepository", "Rate limit (429) al obtener ciudad $ciudadId. Dev Tunnels requiere autenticación.")
                return Result.success(null)
            }
            
            // Verificar si el Content-Type es HTML (indica redirección a GitHub)
            val contentType = response.headers()["Content-Type"] ?: ""
            if (contentType.contains("text/html")) {
                Log.w("LocationRepository", "Respuesta HTML recibida para ciudad $ciudadId (Dev Tunnels requiere autenticación GitHub)")
                return Result.success(null)
            }
            
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
                    // Retornar null en lugar de fallar para no bloquear la UI
                    Result.success(null)
                }
            } else {
                val errorBody = response.errorBody()?.string()
                val error = "Error al obtener ciudad: ${response.code()} ${response.message()}. Body: $errorBody"
                Log.e("LocationRepository", error)
                // En caso de error, retornar null en lugar de fallar para no bloquear la UI
                Result.success(null)
            }
        } catch (e: com.google.gson.stream.MalformedJsonException) {
            // Dev Tunnels está devolviendo HTML (página de login de GitHub) en lugar de JSON
            Log.w("LocationRepository", "Respuesta HTML recibida (Dev Tunnels requiere autenticación) para ciudad $ciudadId")
            Result.success(null)
        } catch (e: Exception) {
            // Solo loggear el mensaje, no el stack trace completo para evitar spam
            val errorMsg = e.message ?: e.javaClass.simpleName
            if (errorMsg.contains("MalformedJsonException") || errorMsg.contains("HTML")) {
                Log.w("LocationRepository", "Respuesta no-JSON recibida para ciudad $ciudadId (probablemente HTML de autenticación)")
            } else {
                Log.e("LocationRepository", "Excepción al obtener ciudad: $errorMsg")
            }
            // En caso de excepción, retornar null en lugar de fallar
            Result.success(null)
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

