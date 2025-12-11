package co.edu.anders.proyectoventas.data.local

import android.content.Context
import android.content.SharedPreferences
import co.edu.anders.proyectoventas.data.models.Usuario

/**
 * Gestiona las preferencias del usuario y la sesión activa
 */
class UserPreferences(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )
    
    companion object {
        private const val PREFS_NAME = "user_prefs"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_ROL_ID = "user_rol_id"
        private const val KEY_USER_ROL_NAME = "user_rol_name"
        private const val KEY_USER_TELEFONO = "user_telefono"
        private const val KEY_USER_CEDULA = "user_cedula"
        private const val KEY_USER_ESTADO = "user_estado"
        private const val KEY_USER_DEPARTAMENTO_ID = "user_departamento_id"
        private const val KEY_USER_CIUDAD_ID = "user_ciudad_id"
        private const val KEY_USER_DEPARTAMENTO = "user_departamento"
        private const val KEY_USER_CIUDAD = "user_ciudad"
        private const val KEY_TOKEN = "token"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }
    
    /**
     * Guarda la sesión del usuario
     */
    fun saveSession(
        userId: Int,
        userName: String,
        userEmail: String,
        rolId: Int,
        rolName: String,
        telefono: String? = null,
        cedula: String? = null,
        estado: String? = null,
        departamentoId: Int? = null,
        ciudadId: Int? = null,
        departamento: String? = null,
        ciudad: String? = null,
        token: String? = null
    ) {
        prefs.edit().apply {
            putInt(KEY_USER_ID, userId)
            putString(KEY_USER_NAME, userName)
            putString(KEY_USER_EMAIL, userEmail)
            putInt(KEY_USER_ROL_ID, rolId)
            putString(KEY_USER_ROL_NAME, rolName)
            putString(KEY_USER_TELEFONO, telefono)
            putString(KEY_USER_CEDULA, cedula)
            putString(KEY_USER_ESTADO, estado)
            
            // Guardar IDs de departamento y ciudad (incluso si son null, para limpiar valores anteriores)
            if (departamentoId != null) {
                putInt(KEY_USER_DEPARTAMENTO_ID, departamentoId)
            } else {
                remove(KEY_USER_DEPARTAMENTO_ID) // Limpiar si es null
            }
            if (ciudadId != null) {
                putInt(KEY_USER_CIUDAD_ID, ciudadId)
            } else {
                remove(KEY_USER_CIUDAD_ID) // Limpiar si es null
            }
            
            putString(KEY_USER_DEPARTAMENTO, departamento ?: "")
            putString(KEY_USER_CIUDAD, ciudad ?: "")
            putString(KEY_TOKEN, token)
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
    }
    
    /**
     * Verifica si hay una sesión activa
     */
    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }
    
    /**
     * Obtiene el ID del usuario actual
     */
    fun getUserId(): Int {
        return prefs.getInt(KEY_USER_ID, -1)
    }
    
    /**
     * Obtiene el nombre del usuario actual
     */
    fun getUserName(): String {
        return prefs.getString(KEY_USER_NAME, "") ?: ""
    }
    
    /**
     * Obtiene el email del usuario actual
     */
    fun getUserEmail(): String {
        return prefs.getString(KEY_USER_EMAIL, "") ?: ""
    }
    
    /**
     * Obtiene el ID del rol del usuario actual
     */
    fun getRolId(): Int {
        return prefs.getInt(KEY_USER_ROL_ID, -1)
    }
    
    /**
     * Obtiene el nombre del rol del usuario actual
     */
    fun getRolName(): String {
        return prefs.getString(KEY_USER_ROL_NAME, "") ?: ""
    }
    
    /**
     * Obtiene el token de autenticación
     */
    fun getToken(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }
    
    /**
     * Obtiene el teléfono del usuario actual
     */
    fun getUserTelefono(): String {
        return prefs.getString(KEY_USER_TELEFONO, "") ?: ""
    }
    
    /**
     * Obtiene la cédula del usuario actual
     */
    fun getUserCedula(): String {
        return prefs.getString(KEY_USER_CEDULA, "") ?: ""
    }
    
    /**
     * Obtiene el estado del usuario actual
     */
    fun getUserEstado(): String {
        return prefs.getString(KEY_USER_ESTADO, "") ?: ""
    }
    
    /**
     * Obtiene el ID del departamento del usuario actual
     */
    fun getUserDepartamentoId(): Int? {
        val id = prefs.getInt(KEY_USER_DEPARTAMENTO_ID, -1)
        return if (id != -1) id else null
    }
    
    /**
     * Obtiene el ID de la ciudad del usuario actual
     */
    fun getUserCiudadId(): Int? {
        val id = prefs.getInt(KEY_USER_CIUDAD_ID, -1)
        return if (id != -1) id else null
    }
    
    /**
     * Obtiene el departamento del usuario actual
     */
    fun getUserDepartamento(): String {
        return prefs.getString(KEY_USER_DEPARTAMENTO, "") ?: ""
    }
    
    /**
     * Obtiene la ciudad del usuario actual
     */
    fun getUserCiudad(): String {
        return prefs.getString(KEY_USER_CIUDAD, "") ?: ""
    }
    
    /**
     * Actualiza los nombres de departamento y ciudad
     */
    fun updateUbicacionNombres(departamento: String?, ciudad: String?) {
        prefs.edit().apply {
            putString(KEY_USER_DEPARTAMENTO, departamento)
            putString(KEY_USER_CIUDAD, ciudad)
            apply()
        }
    }
    
    /**
     * Cierra la sesión del usuario
     */
    fun clearSession() {
        prefs.edit().clear().apply()
    }
}

