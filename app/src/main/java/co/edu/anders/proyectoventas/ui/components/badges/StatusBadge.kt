package co.edu.anders.proyectoventas.ui.components.badges

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.edu.anders.proyectoventas.ui.theme.Error
import co.edu.anders.proyectoventas.ui.theme.Info
import co.edu.anders.proyectoventas.ui.theme.Success
import co.edu.anders.proyectoventas.ui.theme.TextPrimaryDark
import co.edu.anders.proyectoventas.ui.theme.Warning

enum class StatusType {
    SUCCESS, ERROR, WARNING, INFO
}

@Composable
fun StatusBadge(
    text: String,
    statusType: StatusType,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when (statusType) {
        StatusType.SUCCESS -> Success
        StatusType.ERROR -> Error
        StatusType.WARNING -> Warning
        StatusType.INFO -> Info
    }
    
    Text(
        text = text,
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp),
        color = TextPrimaryDark,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold
    )
}

