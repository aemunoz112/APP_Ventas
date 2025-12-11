package co.edu.anders.proyectoventas.data.repository

import co.edu.anders.proyectoventas.data.models.Usuario
import co.edu.anders.proyectoventas.data.sample.SampleData

/**
 * Repositorio para gestionar usuarios
 * Por ahora usa datos de muestra, pero puede extenderse para usar una API o base de datos
 */
class UserRepository {
    
    /**
     * Obtiene el usuario actual
     */
    fun getCurrentUser(): Usuario {
        return SampleData.usuarioActual
    }
    
    /**
     * Obtiene un usuario por ID
     */
    fun getUserById(id: Int): Usuario? {
        // Por ahora solo retorna el usuario actual
        val currentUser = getCurrentUser()
        return if (currentUser.id == id) currentUser else null
    }
}

