package co.edu.anders.proyectoventas.ui.components.textfields

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import co.edu.anders.proyectoventas.ui.theme.BorderLight
import co.edu.anders.proyectoventas.ui.theme.PrimaryBlue
import co.edu.anders.proyectoventas.ui.theme.TextPrimaryLight
import co.edu.anders.proyectoventas.ui.theme.TextSecondaryLight

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "",
    placeholder: String = "",
    isPassword: Boolean = false,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String = "",
    isValid: Boolean = false,
    colors: TextFieldColors? = null
) {
    val defaultColors = colors ?: OutlinedTextFieldDefaults.colors(
        focusedBorderColor = when {
            isError -> Color(0xFFD32F2F) // Rojo para error
            isValid && value.isNotEmpty() -> Color(0xFF388E3C) // Verde para válido
            else -> PrimaryBlue
        },
        unfocusedBorderColor = when {
            isError -> Color(0xFFD32F2F)
            isValid && value.isNotEmpty() -> Color(0xFF388E3C)
            else -> BorderLight
        },
        focusedLabelColor = when {
            isError -> Color(0xFFD32F2F)
            isValid && value.isNotEmpty() -> Color(0xFF388E3C)
            else -> PrimaryBlue
        },
        unfocusedLabelColor = TextSecondaryLight,
        focusedTextColor = TextPrimaryLight,
        unfocusedTextColor = TextPrimaryLight,
        errorSupportingTextColor = Color(0xFFD32F2F)
    )
    
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        label = if (label.isNotEmpty()) { { Text(label) } } else null,
        placeholder = if (placeholder.isNotEmpty()) { { Text(placeholder) } } else null,
        enabled = enabled,
        singleLine = singleLine,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isError = isError,
        supportingText = if (isError && errorMessage.isNotEmpty()) {
            { Text(errorMessage, color = Color(0xFFD32F2F)) }
        } else null,
        shape = RoundedCornerShape(12.dp),
        colors = defaultColors
    )
}

