package co.edu.anders.proyectoventas.ui.components.spacer

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun HorizontalSpacer(
    width: Dp = 16.dp,
    modifier: Modifier = Modifier
) {
    Spacer(
        modifier = modifier.width(width)
    )
}

