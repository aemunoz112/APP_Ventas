package co.edu.anders.proyectoventas.ui.components.buttons

import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import co.edu.anders.proyectoventas.ui.theme.PrimaryBlue
import co.edu.anders.proyectoventas.ui.theme.TextPrimaryDark

@Composable
fun IconButtonComponent(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    backgroundColor: Color = PrimaryBlue,
    iconColor: Color = TextPrimaryDark,
    iconSize: androidx.compose.ui.unit.Dp = 24.dp,
    contentDescription: String? = null
) {
    IconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.size(48.dp),
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = backgroundColor,
            contentColor = iconColor,
            disabledContainerColor = backgroundColor.copy(alpha = 0.6f),
            disabledContentColor = iconColor.copy(alpha = 0.6f)
        )
    ) {
        androidx.compose.material3.Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(iconSize)
        )
    }
}

