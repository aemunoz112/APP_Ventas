package co.edu.anders.proyectoventas.ui.components.pullrefresh

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import co.edu.anders.proyectoventas.ui.theme.PrimaryBlue
import kotlin.math.roundToInt

@Composable
fun PullToRefresh(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current
    
    var dragOffset by remember { mutableFloatStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }
    
    val threshold = with(density) { 100.dp.toPx() }
    val maxDrag = with(density) { 150.dp.toPx() }
    val minIndicatorDrag = with(density) { 50.dp.toPx() }
    val indicatorOffset = with(density) { 16.dp.toPx() }
    val indicatorSize = with(density) { 24.dp.toPx() }
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        if (dragOffset > threshold && !isRefreshing) {
                            onRefresh()
                        }
                        dragOffset = 0f
                        isDragging = false
                    },
                    onDrag = { change, dragAmount ->
                        if (!isRefreshing && change.position.y > 0 && dragAmount.y > 0) {
                            dragOffset = (dragOffset + dragAmount.y).coerceAtMost(maxDrag)
                            isDragging = true
                        } else if (dragOffset > 0) {
                            dragOffset = (dragOffset + dragAmount.y).coerceAtLeast(0f)
                            if (dragOffset <= 0) {
                                isDragging = false
                            }
                        }
                    }
                )
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset {
                    IntOffset(
                        x = 0,
                        y = if (isDragging || isRefreshing) dragOffset.roundToInt() else 0
                    )
                }
        ) {
            content()
        }
        
        // Indicador de refresh
        if (isRefreshing || (isDragging && dragOffset > minIndicatorDrag)) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset {
                        IntOffset(
                            x = 0,
                            y = if (isRefreshing) {
                                indicatorOffset.roundToInt()
                            } else {
                                (dragOffset - indicatorSize).roundToInt().coerceAtLeast(0)
                            }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier,
                    color = PrimaryBlue,
                    strokeWidth = 3.dp
                )
            }
        }
    }
}

