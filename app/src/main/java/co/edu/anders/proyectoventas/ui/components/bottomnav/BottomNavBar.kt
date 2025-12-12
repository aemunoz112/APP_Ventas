package co.edu.anders.proyectoventas.ui.components.bottomnav

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.edu.anders.proyectoventas.ui.theme.PrimaryBlue
import co.edu.anders.proyectoventas.ui.theme.PrimaryBlueDark

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun BottomNavBar(
    selectedItem: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavItem("Dashboard", Icons.Default.Home, "dashboard"),
        BottomNavItem("Buscar", Icons.Default.Search, "search"),
        BottomNavItem("Asistente", Icons.Default.Chat, "assistant"),
        BottomNavItem("Pedidos", Icons.Default.Receipt, "orders"),
        BottomNavItem("Perfil", Icons.Default.Person, "profile")
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 16.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(24.dp),
                clip = false
            )
            .background(
                brush = Brush.horizontalGradient(
                    listOf(
                        PrimaryBlue.copy(alpha = 0.92f),
                        PrimaryBlueDark.copy(alpha = 0.92f)
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(76.dp)
                .clip(RoundedCornerShape(24.dp)),
            containerColor = Color.Transparent,
            contentColor = Color.White,
            tonalElevation = 0.dp
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = selectedItem == index

                val iconScale by animateFloatAsState(
                    targetValue = if (isSelected) 1.15f else 1f,
                    animationSpec = tween(durationMillis = 200),
                    label = "icon_scale_$index"
                )

                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            modifier = Modifier
                                .size(24.dp)
                                .scale(iconScale)
                        )
                    },
                    label = {
                        Text(
                            text = item.label,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                            letterSpacing = 0.3.sp,
                            textAlign = TextAlign.Center,
                            maxLines = 1
                        )
                    },
                    selected = isSelected,
                    onClick = { onItemSelected(index) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = Color.White,
                        indicatorColor = Color.White.copy(alpha = 0.16f),
                        unselectedIconColor = Color.White.copy(alpha = 0.68f),
                        unselectedTextColor = Color.White.copy(alpha = 0.68f)
                    ),
                    alwaysShowLabel = true,  //Ajuste de pading para la barra inferior.
                    modifier = Modifier.padding(top = 24.dp, bottom = 4.dp)
                )
            }
        }
    }
}