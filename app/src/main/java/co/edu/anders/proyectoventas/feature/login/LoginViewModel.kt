package co.edu.anders.proyectoventas.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.anders.proyectoventas.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Estados del login
 */
sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    object Success : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

/**
 * ViewModel para la pantalla de login
 */
class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {
    
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()
    
    /**
     * Realiza el login
     */
    fun login(email: String, password: String) {
        // Validaciones
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = LoginUiState.Error("Por favor completa todos los campos")
            return
        }
        
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.value = LoginUiState.Error("Email inválido")
            return
        }
        
        _uiState.value = LoginUiState.Loading
        
        viewModelScope.launch {
            val result = authRepository.login(email, password)
            
            _uiState.value = if (result.isSuccess) {
                LoginUiState.Success
            } else {
                LoginUiState.Error(result.exceptionOrNull()?.message ?: "Error desconocido")
            }
        }
    }
    
    /**
     * Resetea el estado
     */
    fun resetState() {
        _uiState.value = LoginUiState.Idle
    }
}

