package co.edu.anders.proyectoventas.domain.usecase

import co.edu.anders.proyectoventas.data.models.Usuario
import co.edu.anders.proyectoventas.data.repository.UserRepository

/**
 * Caso de uso para gestionar usuarios
 * Encapsula la lógica de negocio relacionada con usuarios
 */
class GetUserUseCase(
    private val userRepository: UserRepository = UserRepository()
) {
    /**
     * Obtiene el usuario actual
     */
    operator fun invoke(): Usuario {
        return userRepository.getCurrentUser()
    }
    
    /**
     * Obtiene un usuario por ID
     */
    fun getById(id: Int): Usuario? {
        return userRepository.getUserById(id)
    }
}

