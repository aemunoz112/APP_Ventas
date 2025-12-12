package co.edu.anders.proyectoventas.feature.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.edu.anders.proyectoventas.ui.components.buttons.PrimaryButton
import co.edu.anders.proyectoventas.ui.components.buttons.SecondaryButton
import co.edu.anders.proyectoventas.ui.components.spacer.VerticalSpacer
import co.edu.anders.proyectoventas.ui.components.textfields.CustomTextField
import co.edu.anders.proyectoventas.ui.theme.ProyectoVentasTheme
import co.edu.anders.proyectoventas.ui.theme.TextPrimaryLight
import co.edu.anders.proyectoventas.core.util.Validators

@Composable
fun SignUpScreen(
    onNavigateToLogin: () -> Unit,
    onSignUpSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    
    // Estados de validación
    var nameError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf("") }
    
    var isNameValid by remember { mutableStateOf(false) }
    var isEmailValid by remember { mutableStateOf(false) }
    var isPasswordValid by remember { mutableStateOf(false) }
    var isConfirmPasswordValid by remember { mutableStateOf(false) }
    
    // Validación en tiempo real
    fun validateConfirmPasswordField(value: String, originalPassword: String = password) {
        val result = Validators.validatePasswordMatch(originalPassword, value)
        confirmPasswordError = if (!result.isValid && value.isNotEmpty()) result.errorMessage else ""
        isConfirmPasswordValid = result.isValid && value.isNotEmpty()
    }
    
    fun validateNameField(value: String) {
        val result = Validators.validateName(value)
        nameError = if (!result.isValid && value.isNotEmpty()) result.errorMessage else ""
        isNameValid = result.isValid && value.isNotEmpty()
    }
    
    fun validateEmailField(value: String) {
        val result = Validators.validateEmail(value)
        emailError = if (!result.isValid && value.isNotEmpty()) result.errorMessage else ""
        isEmailValid = result.isValid && value.isNotEmpty()
    }
    
    fun validatePasswordField(value: String) {
        val result = Validators.validatePassword(value)
        passwordError = if (!result.isValid && value.isNotEmpty()) result.errorMessage else ""
        isPasswordValid = result.isValid && value.isNotEmpty()
        
        // Si hay confirmación, validar también
        if (confirmPassword.isNotEmpty()) {
            validateConfirmPasswordField(confirmPassword, value)
        }
    }
    
    ProyectoVentasTheme {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Crear Cuenta",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimaryLight
            )
            
            VerticalSpacer(32.dp)
            
            CustomTextField(
                value = name,
                onValueChange = { 
                    name = it
                    validateNameField(it)
                },
                label = "Nombre completo",
                placeholder = "Ingresa tu nombre",
                isError = nameError.isNotEmpty(),
                errorMessage = nameError,
                isValid = isNameValid
            )
            
            CustomTextField(
                value = email,
                onValueChange = { 
                    email = it
                    validateEmailField(it)
                },
                label = "Correo electrónico",
                placeholder = "ejemplo@correo.com",
                keyboardType = androidx.compose.ui.text.input.KeyboardType.Email,
                isError = emailError.isNotEmpty(),
                errorMessage = emailError,
                isValid = isEmailValid
            )
            
            CustomTextField(
                value = password,
                onValueChange = { 
                    password = it
                    validatePasswordField(it)
                },
                label = "Contraseña",
                placeholder = "Mínimo 8 caracteres",
                isPassword = true,
                isError = passwordError.isNotEmpty(),
                errorMessage = passwordError,
                isValid = isPasswordValid
            )
            
            CustomTextField(
                value = confirmPassword,
                onValueChange = { 
                    confirmPassword = it
                    validateConfirmPasswordField(it, password)
                },
                label = "Confirmar contraseña",
                placeholder = "Repite tu contraseña",
                isPassword = true,
                isError = confirmPasswordError.isNotEmpty(),
                errorMessage = confirmPasswordError,
                isValid = isConfirmPasswordValid
            )
            
            VerticalSpacer(24.dp)
            
            PrimaryButton(
                text = "Registrarse",
                onClick = {
                    // Validar todos los campos antes de enviar
                    validateNameField(name)
                    validateEmailField(email)
                    validatePasswordField(password)
                    validateConfirmPasswordField(confirmPassword, password)
                    
                    if (nameError.isEmpty() && emailError.isEmpty() && 
                        passwordError.isEmpty() && confirmPasswordError.isEmpty() &&
                        name.isNotBlank() && email.isNotBlank() && 
                        password.isNotBlank() && confirmPassword.isNotBlank()) {
                        // TODO: Implementar lógica de registro
                        onSignUpSuccess()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = name.isNotBlank() && email.isNotBlank() && 
                         password.isNotBlank() && confirmPassword.isNotBlank()
            )
            
            VerticalSpacer(16.dp)
            
            SecondaryButton(
                text = "Ya tengo cuenta",
                onClick = onNavigateToLogin,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
