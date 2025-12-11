package co.edu.anders.proyectoventas.feature.orders

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.edu.anders.proyectoventas.core.util.Formatters
import co.edu.anders.proyectoventas.ui.components.appbar.CustomAppBar
import co.edu.anders.proyectoventas.ui.components.badges.StatusBadge
import co.edu.anders.proyectoventas.ui.components.badges.StatusType
import co.edu.anders.proyectoventas.ui.components.empty.EmptyState
import co.edu.anders.proyectoventas.ui.components.spacer.VerticalSpacer
import co.edu.anders.proyectoventas.ui.theme.PrimaryBlue
import co.edu.anders.proyectoventas.ui.theme.ProyectoVentasTheme
import co.edu.anders.proyectoventas.ui.theme.SurfaceLight
import co.edu.anders.proyectoventas.ui.theme.TextPrimaryLight
import co.edu.anders.proyectoventas.ui.theme.TextSecondaryLight
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun OrderDetailScreen(
    orderId: Int,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val viewModel = remember { OrdersViewModel() }
    val uiState by viewModel.orderDetailState.collectAsState()
    
    // Cargar detalles del pedido cuando se abre la pantalla
    LaunchedEffect(orderId) {
        viewModel.loadOrderDetails(orderId)
    }
    
    // Manejar errores
    LaunchedEffect(uiState) {
        if (uiState is OrderDetailUiState.Error) {
            Toast.makeText(context, (uiState as OrderDetailUiState.Error).message, Toast.LENGTH_LONG).show()
        }
    }
    
    ProyectoVentasTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            Column(modifier = modifier.fillMaxSize().background(Color.White)) {
                CustomAppBar(
                    title = "Detalle del Pedido",
                    onNavigationClick = onNavigateBack
                )
                
                when (val state = uiState) {
                    is OrderDetailUiState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = PrimaryBlue)
                        }
                    }
                    is OrderDetailUiState.Success -> {
                        val pedido = state.order
                        val detalles = state.details
                        
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White)
                                .verticalScroll(rememberScrollState())
                                .padding(horizontal = 20.dp)
                                .padding(top = 24.dp)
                                .padding(bottom = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            // Encabezado del pedido
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = SurfaceLight
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp),
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Pedido #${pedido.id}",
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimaryLight
                                        )
                                        StatusBadge(
                                            text = pedido.tipoPedido ?: "N/A",
                                            statusType = if (pedido.tipoPedido == "Exportación") StatusType.INFO else StatusType.SUCCESS
                                        )
                                    }
                                    
                                    Divider(
                                        color = Color.White.copy(alpha = 0.1f),
                                        thickness = 1.dp
                                    )
                                    
                                    // Información del pedido
                                    InfoRow(
                                        label = "OC Cliente",
                                        value = pedido.ocCliente ?: "N/A"
                                    )
                                    
                                    InfoRow(
                                        label = "Fecha de Creación",
                                        value = pedido.fechaCreacion?.let {
                                            try {
                                                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                                                val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                                                val date = inputFormat.parse(it)
                                                outputFormat.format(date ?: "")
                                            } catch (e: Exception) {
                                                it
                                            }
                                        } ?: "N/A"
                                    )
                                    
                                    InfoRow(
                                        label = "Moneda",
                                        value = "${pedido.moneda ?: "N/A"} (TRM: ${Formatters.formatCurrency(pedido.trm ?: 0.0)})"
                                    )
                                    
                                    InfoRow(
                                        label = "Condición de Pago",
                                        value = pedido.condicionPago ?: "N/A"
                                    )
                                    
                                    if (pedido.direccion != null || pedido.ciudadNombre != null || pedido.departamentoNombre != null) {
                                        InfoRow(
                                            label = "Ubicación",
                                            value = buildString {
                                                if (pedido.direccion != null) {
                                                    append(pedido.direccion)
                                                    if (pedido.ciudadNombre != null || pedido.departamentoNombre != null) {
                                                        append(", ")
                                                    }
                                                }
                                                if (pedido.ciudadNombre != null) {
                                                    append(pedido.ciudadNombre)
                                                }
                                                if (pedido.departamentoNombre != null) {
                                                    if (pedido.ciudadNombre != null) append(", ")
                                                    append(pedido.departamentoNombre)
                                                }
                                                if (isEmpty()) append("N/A")
                                            }
                                        )
                                    }
                                    
                                    InfoRow(
                                        label = "ID Cliente",
                                        value = pedido.idCliente.toString()
                                    )
                                    
                                    InfoRow(
                                        label = "ID Vendedor",
                                        value = pedido.idVendedor.toString()
                                    )
                                }
                            }
                            
                            // Detalles del pedido (items)
                            Text(
                                text = "Items del Pedido (${detalles.size})",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimaryLight
                            )
                            
                            if (detalles.isEmpty()) {
                                EmptyState(
                                    title = "No hay items",
                                    message = "Este pedido no tiene items registrados",
                                    modifier = Modifier.fillMaxWidth()
                                )
                            } else {
                                detalles.forEachIndexed { index, detalle ->
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = SurfaceLight
                                        ),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            verticalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "Item #${detalle.numeroLinea ?: (index + 1)}",
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = TextPrimaryLight
                                                )
                                                if (detalle.producto != null) {
                                                    Text(
                                                        text = detalle.producto.nombreProducto ?: "Producto #${detalle.idProducto}",
                                                        fontSize = 14.sp,
                                                        fontWeight = FontWeight.SemiBold,
                                                        color = PrimaryBlue
                                                    )
                                                }
                                            }
                                            
                                            Divider(
                                                color = Color.White.copy(alpha = 0.1f),
                                                thickness = 1.dp
                                            )
                                            
                                            InfoRow(
                                                label = "ID Producto",
                                                value = detalle.idProducto.toString()
                                            )
                                            
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Column(modifier = Modifier.weight(1f)) {
                                                    InfoRow(
                                                        label = "Cantidad Solicitada",
                                                        value = Formatters.formatQuantity(detalle.cantidadSolicitada)
                                                    )
                                                }
                                                Column(modifier = Modifier.weight(1f)) {
                                                    InfoRow(
                                                        label = "Cantidad Confirmada",
                                                        value = detalle.cantidadConfirmada?.let { Formatters.formatQuantity(it) } ?: "N/A"
                                                    )
                                                }
                                            }
                                            
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Column(modifier = Modifier.weight(1f)) {
                                                    InfoRow(
                                                        label = "Precio Unitario",
                                                        value = detalle.precioUnitario?.let { Formatters.formatCurrency(it) } ?: "N/A"
                                                    )
                                                }
                                                Column(modifier = Modifier.weight(1f)) {
                                                    InfoRow(
                                                        label = "Precio Total",
                                                        value = detalle.precioTotal?.let { Formatters.formatCurrency(it) } ?: "N/A"
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    
                                    if (index < detalles.size - 1) {
                                        VerticalSpacer(8.dp)
                                    }
                                }
                                
                                // Resumen total
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = PrimaryBlue.copy(alpha = 0.1f)
                                    ),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            text = "Resumen",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimaryLight
                                        )
                                        
                                        Divider(
                                            color = Color.White.copy(alpha = 0.2f),
                                            thickness = 1.dp
                                        )
                                        
                                        val totalItems = detalles.size
                                        val totalCantidad = detalles.sumOf { it.cantidadSolicitada }
                                        val totalPrecio = detalles.sumOf { it.precioTotal ?: 0.0 }
                                        
                                        InfoRow(
                                            label = "Total Items",
                                            value = totalItems.toString()
                                        )
                                        
                                        InfoRow(
                                            label = "Total Cantidad",
                                            value = Formatters.formatQuantity(totalCantidad)
                                        )
                                        
                                        InfoRow(
                                            label = "Total Precio",
                                            value = Formatters.formatCurrency(totalPrecio),
                                            valueColor = PrimaryBlue,
                                            valueWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                    
                    is OrderDetailUiState.Error -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Error al cargar detalles",
                                    fontSize = 18.sp,
                                    color = TextPrimaryLight,
                                    fontWeight = FontWeight.SemiBold
                                )
                                VerticalSpacer(8.dp)
                                Text(
                                    text = state.message,
                                    fontSize = 14.sp,
                                    color = TextSecondaryLight
                                )
                            }
                        }
                    }
                    
                    is OrderDetailUiState.Idle -> {
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
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    valueColor: Color = TextPrimaryLight,
    valueWeight: FontWeight = FontWeight.SemiBold
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = TextSecondaryLight,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            color = valueColor,
            fontWeight = valueWeight,
            modifier = Modifier.weight(1f)
        )
    }
}

