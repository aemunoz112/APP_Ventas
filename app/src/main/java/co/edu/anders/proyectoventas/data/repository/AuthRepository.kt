package co.edu.anders.proyectoventas.data.repository

import co.edu.anders.proyectoventas.data.local.UserPreferences
import co.edu.anders.proyectoventas.data.remote.RetrofitClient
import co.edu.anders.proyectoventas.data.remote.dto.LoginRequest

/**
 * Repositorio de autenticación
 */
class AuthRepository(private val userPreferences: UserPreferences) {
    
    private val api = RetrofitClient.apiService
    private val locationRepository = LocationRepository()
    
    /**
     * Inicia sesión con email y contraseña
     */
    suspend fun login(email: String, password: String): Result<Boolean> {
        return try {
            android.util.Log.d("AuthRepository", "=== INICIO LOGIN ===")
            android.util.Log.d("AuthRepository", "Email: $email")
            android.util.Log.d("AuthRepository", "URL base: https://nonceremonially-unwary-livia.ngrok-free.app/")
            android.util.Log.d("AuthRepository", "Endpoint: auth/login")
            android.util.Log.d("AuthRepository", "URL completa esperada: https://nonceremonially-unwary-livia.ngrok-free.app/auth/login")
            
            val response = api.login(LoginRequest(email, password))
            
            android.util.Log.d("AuthRepository", "=== RESPUESTA ===")
            android.util.Log.d("AuthRepository", "Código HTTP: ${response.code()}")
            android.util.Log.d("AuthRepository", "Exitoso: ${response.isSuccessful}")
            android.util.Log.d("AuthRepository", "Headers: ${response.headers()}")
            
            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string()
                android.util.Log.e("AuthRepository", "Error body: $errorBody")
            }
            
            if (response.isSuccessful && response.body() != null) {
                val loginResponse = response.body()!!
                val user = loginResponse.usuario
                val rol = loginResponse.rol
                
                if (user != null && rol != null) {
                    android.util.Log.d("AuthRepository", "=== INICIO LOGIN ===")
                    android.util.Log.d("AuthRepository", "Usuario ID del login: ${user.id}")
                    
                    // Obtener información completa del usuario desde el endpoint /get_user/{id}
                    // Este endpoint SÍ devuelve departamento_id y ciudad_id con sus nombres desde Node.js
                    var departamentoId: Int? = null
                    var ciudadId: Int? = null
                    var departamentoNombre: String? = null
                    var ciudadNombre: String? = null
                    
                    try {
                        android.util.Log.d("AuthRepository", "Obteniendo información completa del usuario desde /get_user/${user.id}...")
                        val userResponse = api.obtenerUsuario(user.id)
                        
                        if (userResponse.isSuccessful && userResponse.body() != null) {
                            val usuarioCompleto = userResponse.body()!!
                            departamentoId = usuarioCompleto.departamentoId
                            ciudadId = usuarioCompleto.ciudadId
                            departamentoNombre = usuarioCompleto.departamentoNombre?.takeIf { it.isNotEmpty() }
                            ciudadNombre = usuarioCompleto.ciudadNombre?.takeIf { it.isNotEmpty() }
                            
                            android.util.Log.d("AuthRepository", "Usuario completo obtenido:")
                            android.util.Log.d("AuthRepository", "  Departamento ID: $departamentoId")
                            android.util.Log.d("AuthRepository", "  Ciudad ID: $ciudadId")
                            android.util.Log.d("AuthRepository", "  Departamento nombre: '$departamentoNombre'")
                            android.util.Log.d("AuthRepository", "  Ciudad nombre: '$ciudadNombre'")
                        } else {
                            android.util.Log.w("AuthRepository", "No se pudo obtener usuario completo: ${userResponse.code()}")
                            // Usar los valores del login como fallback
                            departamentoId = user.departamentoId
                            ciudadId = user.ciudadId
                            departamentoNombre = user.departamentoNombre?.takeIf { it.isNotEmpty() }
                            ciudadNombre = user.ciudadNombre?.takeIf { it.isNotEmpty() }
                        }
                    } catch (e: Exception) {
                        android.util.Log.e("AuthRepository", "Error al obtener usuario completo: ${e.message}", e)
                        // Usar los valores del login como fallback
                        departamentoId = user.departamentoId
                        ciudadId = user.ciudadId
                        departamentoNombre = user.departamentoNombre?.takeIf { it.isNotEmpty() }
                        ciudadNombre = user.ciudadNombre?.takeIf { it.isNotEmpty() }
                    }
                    
                    // Si aún no hay nombres pero hay IDs, obtenerlos desde la API de Node.js directamente
                    if (departamentoNombre.isNullOrEmpty() && departamentoId != null) {
                        android.util.Log.d("AuthRepository", "Obteniendo departamento desde Node.js API...")
                        val result = locationRepository.getDepartamentoNombre(departamentoId)
                        departamentoNombre = if (result.isSuccess) {
                            result.getOrNull()?.takeIf { it.isNotEmpty() }
                        } else {
                            null
                        }
                    }
                    
                    if (ciudadNombre.isNullOrEmpty() && ciudadId != null) {
                        android.util.Log.d("AuthRepository", "Obteniendo ciudad desde Node.js API...")
                        val result = locationRepository.getCiudadNombre(ciudadId)
                        ciudadNombre = if (result.isSuccess) {
                            result.getOrNull()?.takeIf { it.isNotEmpty() }
                        } else {
                            null
                        }
                    }
                    
                    android.util.Log.d("AuthRepository", "Valores finales antes de guardar:")
                    android.util.Log.d("AuthRepository", "  Departamento ID: $departamentoId, Nombre: '$departamentoNombre'")
                    android.util.Log.d("AuthRepository", "  Ciudad ID: $ciudadId, Nombre: '$ciudadNombre'")
                    
                    // Guardar sesión
                    userPreferences.saveSession(
                        userId = user.id,
                        userName = "${user.nombres ?: ""} ${user.apellidos ?: ""}".trim(),
                        userEmail = user.email,
                        rolId = rol.id,
                        rolName = rol.nombre,
                        telefono = user.telefono,
                        cedula = user.cedula,
                        estado = user.estado,
                        departamentoId = departamentoId,
                        ciudadId = ciudadId,
                        departamento = departamentoNombre ?: "",
                        ciudad = ciudadNombre ?: "",
                        token = "session_${user.id}"  // Token de sesión simple
                    )
                    
                    // Verificar inmediatamente después de guardar
                    android.util.Log.d("AuthRepository", "VERIFICACIÓN POST-GUARDADO:")
                    android.util.Log.d("AuthRepository", "  Departamento ID guardado: ${userPreferences.getUserDepartamentoId()}")
                    android.util.Log.d("AuthRepository", "  Ciudad ID guardada: ${userPreferences.getUserCiudadId()}")
                    android.util.Log.d("AuthRepository", "  Departamento nombre guardado: '${userPreferences.getUserDepartamento()}'")
                    android.util.Log.d("AuthRepository", "  Ciudad nombre guardada: '${userPreferences.getUserCiudad()}'")
                    android.util.Log.d("AuthRepository", "=== FIN LOGIN ===")
                    
                    Result.success(true)
                } else {
                    Result.failure(Exception("Datos de usuario incompletos"))
                }
            } else {
                val errorBody = try {
                    response.errorBody()?.string() ?: ""
                } catch (e: Exception) {
                    "No se pudo leer el cuerpo del error: ${e.message}"
                }
                
                android.util.Log.e("AuthRepository", "=== ERROR EN LOGIN ===")
                android.util.Log.e("AuthRepository", "Código HTTP: ${response.code()}")
                android.util.Log.e("AuthRepository", "Mensaje: ${response.message()}")
                android.util.Log.e("AuthRepository", "Error Body (primeros 500 chars): ${errorBody.take(500)}")
                
                // Verificar si es una respuesta HTML (página de ngrok)
                val isHtmlResponse = errorBody.contains("<html", ignoreCase = true) || 
                                   errorBody.contains("<!DOCTYPE", ignoreCase = true) ||
                                   response.headers()["content-type"]?.contains("text/html") == true
                
                val errorMessage = when {
                    isHtmlResponse -> {
                        android.util.Log.e("AuthRepository", "La respuesta es HTML - probablemente página de ngrok")
                        "Error de conexión: El servidor devolvió HTML en lugar de JSON.\n" +
                        "Verifica:\n" +
                        "1. Que ngrok esté corriendo\n" +
                        "2. Que el backend FastAPI esté activo\n" +
                        "3. Que la URL sea correcta: https://nonceremonially-unwary-livia.ngrok-free.app/auth/login"
                    }
                    errorBody.contains("ERR_NGROK_3200", ignoreCase = true) || 
                    errorBody.contains("is offline", ignoreCase = true) -> {
                        android.util.Log.e("AuthRepository", "Ngrok está offline")
                        "❌ Ngrok está offline\n\n" +
                        "El túnel de ngrok no está activo. Por favor:\n\n" +
                        "1. Abre una terminal y ejecuta:\n   ngrok http 8000\n\n" +
                        "2. Copia la nueva URL de ngrok (si cambió)\n\n" +
                        "3. Actualiza la URL en RetrofitClient.kt\n\n" +
                        "4. Verifica que tu backend FastAPI esté corriendo"
                    }
                    response.code() == 401 -> "Email o contraseña incorrectos"
                    response.code() == 404 -> {
                        if (errorBody.contains("Not Found") || errorBody.contains("404")) {
                            "Usuario no encontrado o endpoint incorrecto.\n" +
                            "URL esperada: https://nonceremonially-unwary-livia.ngrok-free.app/auth/login\n" +
                            "Verifica que el backend esté corriendo."
                        } else {
                            "Usuario no encontrado"
                        }
                    }
                    response.code() == 500 -> "Error en el servidor: $errorBody"
                    else -> "Error al iniciar sesión (Código: ${response.code()})\n$errorBody"
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: java.net.ConnectException) {
            Result.failure(Exception("No se puede conectar al servidor.\n¿El backend está corriendo?\nVerifica: SOLUCION_ERROR_CONEXION.md"))
        } catch (e: java.net.SocketTimeoutException) {
            Result.failure(Exception("Tiempo de espera agotado.\nVerifica tu conexión de red"))
        } catch (e: java.net.UnknownHostException) {
            Result.failure(Exception("No se encuentra el servidor.\nVerifica la URL en RetrofitClient.kt"))
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión:\n${e.javaClass.simpleName}\n${e.message}"))
        }
    }
    
    /**
     * Cierra la sesión del usuario
     */
    fun logout() {
        userPreferences.clearSession()
    }
    
    /**
     * Verifica si hay una sesión activa
     */
    fun isLoggedIn(): Boolean {
        return userPreferences.isLoggedIn()
    }
    
    /**
     * Obtiene el nombre del usuario actual
     */
    fun getCurrentUserName(): String {
        return userPreferences.getUserName()
    }
    
    /**
     * Obtiene el rol del usuario actual
     */
    fun getCurrentUserRole(): String {
        return userPreferences.getRolName()
    }
}

