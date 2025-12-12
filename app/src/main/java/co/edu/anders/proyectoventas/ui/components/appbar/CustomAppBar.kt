package co.edu.anders.proyectoventas.ui.components.appbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.edu.anders.proyectoventas.ui.theme.PrimaryBlue
import co.edu.anders.proyectoventas.ui.theme.TextPrimaryDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: ImageVector? = Icons.Default.ArrowBack,
    onNavigationClick: (() -> Unit)? = null,
    actions: @Composable (() -> Unit)? = null
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            if (navigationIcon != null && onNavigationClick != null) {
                IconButton(onClick = onNavigationClick) {
                    Icon(
                        imageVector = navigationIcon,
                        contentDescription = "Navegar atrás"
                    )
                }
            }
        },
        actions = {
            actions?.invoke()
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = PrimaryBlue,
            titleContentColor = TextPrimaryDark,
            navigationIconContentColor = TextPrimaryDark,
            actionIconContentColor = TextPrimaryDark
        ),
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(bottomStart = 0.dp, bottomEnd = 0.dp),
                clip = false
            )
            .background(
                brush = Brush.horizontalGradient(
                    listOf(
                        PrimaryBlue.copy(alpha = 0.95f),
                        PrimaryBlue.copy(alpha = 0.9f)
                    )
                )
            )
    )
}

