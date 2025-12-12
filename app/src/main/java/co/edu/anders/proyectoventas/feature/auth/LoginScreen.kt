package co.edu.anders.proyectoventas.feature.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.edu.anders.proyectoventas.data.local.UserPreferences
import co.edu.anders.proyectoventas.data.repository.AuthRepository
import co.edu.anders.proyectoventas.feature.login.LoginUiState
import co.edu.anders.proyectoventas.feature.login.LoginViewModel
import co.edu.anders.proyectoventas.ui.components.buttons.PrimaryButton
import co.edu.anders.proyectoventas.ui.components.buttons.SecondaryButton
import co.edu.anders.proyectoventas.ui.components.spacer.VerticalSpacer
import co.edu.anders.proyectoventas.ui.components.textfields.CustomTextField
import co.edu.anders.proyectoventas.ui.theme.PrimaryBlue
import co.edu.anders.proyectoventas.ui.theme.ProyectoVentasTheme
import co.edu.anders.proyectoventas.ui.theme.TextPrimaryLight
import co.edu.anders.proyectoventas.core.util.Validators

@Composable
fun LoginScreen(
    onNavigateToSignUp: () -> Unit,
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val viewModel = remember {
        LoginViewModel(AuthRepository(UserPreferences(context)))
    }
    
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    
    // Estados de validación
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var isEmailValid by remember { mutableStateOf(false) }
    var isPasswordValid by remember { mutableStateOf(false) }
    
    val uiState by viewModel.uiState.collectAsState()
    
    // Validación en tiempo real
    fun validateEmailField(value: String) {
        val result = Validators.validateEmail(value)
        emailError = if (!result.isValid && value.isNotEmpty()) result.errorMessage else ""
        isEmailValid = result.isValid && value.isNotEmpty()
    }
    
    fun validatePasswordField(value: String) {
        val result = Validators.validatePassword(value)
        passwordError = if (!result.isValid && value.isNotEmpty()) result.errorMessage else ""
        isPasswordValid = result.isValid && value.isNotEmpty()
    }
    
    // Manejar estados del login
    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is LoginUiState.Success -> {
                Toast.makeText(context, "¡Bienvenido!", Toast.LENGTH_SHORT).show()
                onLoginSuccess()
            }
            is LoginUiState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                viewModel.resetState()
            }
            else -> { /* No hacer nada */ }
        }
    }
    
    ProyectoVentasTheme {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            PrimaryBlue.copy(alpha = 0.18f),
                            PrimaryBlue.copy(alpha = 0.06f)
                        )
                    )
                )
        ) {
            Card(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 32.dp)
                    .align(Alignment.Center),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.96f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Bienvenido de nuevo",
                            fontSize = 14.sp,
                            color = TextPrimaryLight.copy(alpha = 0.65f)
                        )
                        Text(
                            text = "Iniciar Sesión",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryLight
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    CustomTextField(
                        value = email,
                        onValueChange = { 
                            email = it
                            validateEmailField(it)
                        },
                        label = "Correo electrónico",
                        placeholder = "ejemplo@correo.com",
                        keyboardType = androidx.compose.ui.text.input.KeyboardType.Email,
                        enabled = uiState !is LoginUiState.Loading,
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
                        placeholder = "Ingresa tu contraseña",
                        isPassword = true,
                        enabled = uiState !is LoginUiState.Loading,
                        isError = passwordError.isNotEmpty(),
                        errorMessage = passwordError,
                        isValid = isPasswordValid
                    )
                    
                    PrimaryButton(
                        text = if (uiState is LoginUiState.Loading) "Iniciando..." else "Iniciar Sesión",
                        onClick = {
                            // Validar antes de enviar
                            validateEmailField(email)
                            validatePasswordField(password)
                            
                            if (emailError.isEmpty() && passwordError.isEmpty() && 
                                email.isNotBlank() && password.isNotBlank()) {
                                viewModel.login(email, password)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = uiState !is LoginUiState.Loading && 
                                 email.isNotBlank() && password.isNotBlank()
                    )
                    
                    SecondaryButton(
                        text = "Crear cuenta",
                        onClick = onNavigateToSignUp,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = uiState !is LoginUiState.Loading
                    )
                }
            }
            
            // Loading indicator
            if (uiState is LoginUiState.Loading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = PrimaryBlue)
                }
            }
        }
    }
}
