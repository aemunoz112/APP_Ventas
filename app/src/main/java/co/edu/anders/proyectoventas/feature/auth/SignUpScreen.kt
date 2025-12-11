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
                onValueChange = { name = it },
                label = "Nombre completo",
                placeholder = "Ingresa tu nombre"
            )
            
            CustomTextField(
                value = email,
                onValueChange = { email = it },
                label = "Correo electrónico",
                placeholder = "ejemplo@correo.com",
                keyboardType = androidx.compose.ui.text.input.KeyboardType.Email
            )
            
            CustomTextField(
                value = password,
                onValueChange = { password = it },
                label = "Contraseña",
                placeholder = "Mínimo 8 caracteres",
                isPassword = true
            )
            
            CustomTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = "Confirmar contraseña",
                placeholder = "Repite tu contraseña",
                isPassword = true
            )
            
            VerticalSpacer(24.dp)
            
            PrimaryButton(
                text = "Registrarse",
                onClick = {
                    // TODO: Implementar lógica de registro
                    onSignUpSuccess()
                },
                modifier = Modifier.fillMaxWidth()
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
