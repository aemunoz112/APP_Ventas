package co.edu.anders.proyectoventas.core.util

/**
 * Utilidades para validación de formularios
 */
object Validators {
    
    /**
     * Resultado de validación
     */
    data class ValidationResult(
        val isValid: Boolean,
        val errorMessage: String = ""
    )
    
    /**
     * Valida un email
     */
    fun validateEmail(email: String): ValidationResult {
        return when {
            email.isBlank() -> ValidationResult(false, "El correo electrónico es requerido")
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> 
                ValidationResult(false, "Ingresa un correo electrónico válido")
            else -> ValidationResult(true)
        }
    }
    
    /**
     * Valida una contraseña
     */
    fun validatePassword(password: String, minLength: Int = 8): ValidationResult {
        return when {
            password.isBlank() -> ValidationResult(false, "La contraseña es requerida")
            password.length < minLength -> 
                ValidationResult(false, "La contraseña debe tener al menos $minLength caracteres")
            !password.any { it.isDigit() } -> 
                ValidationResult(false, "La contraseña debe contener al menos un número")
            !password.any { it.isLetter() } -> 
                ValidationResult(false, "La contraseña debe contener al menos una letra")
            else -> ValidationResult(true)
        }
    }
    
    /**
     * Valida que dos contraseñas coincidan
     */
    fun validatePasswordMatch(password: String, confirmPassword: String): ValidationResult {
        return when {
            confirmPassword.isBlank() -> ValidationResult(false, "Confirma tu contraseña")
            password != confirmPassword -> ValidationResult(false, "Las contraseñas no coinciden")
            else -> ValidationResult(true)
        }
    }
    
    /**
     * Valida un nombre completo
     */
    fun validateName(name: String, minLength: Int = 2): ValidationResult {
        return when {
            name.isBlank() -> ValidationResult(false, "El nombre es requerido")
            name.length < minLength -> 
                ValidationResult(false, "El nombre debe tener al menos $minLength caracteres")
            !name.all { it.isLetter() || it.isWhitespace() } -> 
                ValidationResult(false, "El nombre solo puede contener letras")
            else -> ValidationResult(true)
        }
    }
    
    /**
     * Valida un campo requerido
     */
    fun validateRequired(value: String, fieldName: String): ValidationResult {
        return when {
            value.isBlank() -> ValidationResult(false, "$fieldName es requerido")
            else -> ValidationResult(true)
        }
    }
}

