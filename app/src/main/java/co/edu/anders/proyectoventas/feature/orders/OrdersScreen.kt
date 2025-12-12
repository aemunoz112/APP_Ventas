package co.edu.anders.proyectoventas.feature.orders

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.draw.alpha
import co.edu.anders.proyectoventas.ui.components.pullrefresh.PullToRefresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.edu.anders.proyectoventas.feature.orders.OrdersViewModel
import co.edu.anders.proyectoventas.feature.orders.OrdersUiState
import co.edu.anders.proyectoventas.ui.components.appbar.CustomAppBar
import co.edu.anders.proyectoventas.ui.components.badges.StatusBadge
import co.edu.anders.proyectoventas.ui.components.badges.StatusType
import co.edu.anders.proyectoventas.ui.components.bottomnav.BottomNavBar
import co.edu.anders.proyectoventas.ui.components.empty.EmptyState
import co.edu.anders.proyectoventas.ui.components.loading.OrderSkeletonLoader
import co.edu.anders.proyectoventas.ui.components.spacer.VerticalSpacer
import kotlinx.coroutines.delay
import co.edu.anders.proyectoventas.ui.theme.PrimaryBlue
import co.edu.anders.proyectoventas.ui.theme.ProyectoVentasTheme
import co.edu.anders.proyectoventas.ui.theme.SurfaceLight
import co.edu.anders.proyectoventas.ui.theme.TextPrimaryLight
import co.edu.anders.proyectoventas.ui.theme.TextSecondaryLight
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun OrdersScreen(
    onNavigateBack: () -> Unit,
    onOrderClick: (Int) -> Unit,
    onNavigateToHome: () -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
    onNavigateToAssistant: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val viewModel: OrdersViewModel = viewModel { OrdersViewModel(context) }
    val uiState by viewModel.uiState.collectAsState()

    var selectedBottomItem by remember { mutableIntStateOf(3) }
    
    // Flag para controlar si ya se cargaron los pedidos la primera vez
    var hasLoadedInitially by remember { mutableIntStateOf(0) }
    
    // Estado de refresh
    val isRefreshing = uiState is OrdersUiState.Loading

    // Cargar pedidos solo la primera vez que se entra a la pantalla
    LaunchedEffect(Unit) {
        if (hasLoadedInitially == 0) {
            hasLoadedInitially = 1
            viewModel.loadOrders()
        }
    }

    // Manejar errores
    LaunchedEffect(uiState) {
        if (uiState is OrdersUiState.Error) {
            Toast.makeText(context, (uiState as OrdersUiState.Error).message, Toast.LENGTH_LONG).show()
        }
    }

    ProyectoVentasTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Transparent
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                PrimaryBlue.copy(alpha = 0.1f),
                                Color.White
                            )
                        )
                    )
            )
            Scaffold(
                containerColor = Color.Transparent,
                topBar = {
                    CustomAppBar(
                        title = "Mis Pedidos",
                        actions = {
                            IconButton(
                                onClick = { viewModel.loadOrders() },
                                enabled = !isRefreshing
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Actualizar pedidos"
                                )
                            }
                        }
                    )
                },
                bottomBar = {
                    BottomNavBar(
                        selectedItem = selectedBottomItem,
                        onItemSelected = { index ->
                            selectedBottomItem = index
                            when (index) {
                                0 -> onNavigateToHome()
                                1 -> onNavigateToSearch()
                                2 -> onNavigateToAssistant()
                                3 -> {} // Ya estamos en Orders
                                4 -> onNavigateToProfile()
                            }
                        }
                    )
                }
            ) { padding ->
                PullToRefresh(
                    isRefreshing = isRefreshing,
                    onRefresh = { viewModel.loadOrders() },
                    modifier = modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp)
                            .padding(top = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Mis Pedidos",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimaryLight
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Estado y ubicación de tus órdenes",
                                fontSize = 13.sp,
                                color = TextSecondaryLight
                            )
                        }
                        when (val state = uiState) {
                            is OrdersUiState.Success -> {
                                Text(
                                    text = "${state.orders.size} pedido${if (state.orders.size != 1) "s" else ""}",
                                    fontSize = 14.sp,
                                    color = TextPrimaryLight.copy(alpha = 0.6f)
                                )
                            }
                            else -> {}
                        }
                    }

                    when (val state = uiState) {
                        is OrdersUiState.Idle -> {
                            OrderSkeletonLoader(
                                modifier = Modifier.fillMaxWidth(),
                                itemCount = 3
                            )
                        }

                        is OrdersUiState.Loading -> {
                            OrderSkeletonLoader(
                                modifier = Modifier.fillMaxWidth(),
                                itemCount = 3
                            )
                        }

                        is OrdersUiState.Success -> {
                            if (state.orders.isEmpty()) {
                                EmptyState(
                                    title = "No hay pedidos",
                                    message = "Aún no se han registrado pedidos",
                                    defaultIcon = Icons.Default.Inbox,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            } else {
                                LazyColumn(
                                    verticalArrangement = Arrangement.spacedBy(16.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    items(
                                        items = state.orders,
                                        key = { it.id }
                                    ) { pedido ->
                                        // Animación de entrada suave
                                        var itemAlpha by remember(key1 = pedido.id) { mutableFloatStateOf(0f) }
                                        val animatedAlpha by animateFloatAsState(
                                            targetValue = itemAlpha,
                                            animationSpec = tween(durationMillis = 300),
                                            label = "order_alpha_${pedido.id}"
                                        )
                                        
                                        LaunchedEffect(pedido.id) {
                                            itemAlpha = 1f
                                        }
                                            Card(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .alpha(animatedAlpha)
                                                    .clickable { onOrderClick(pedido.id) },
                                                shape = RoundedCornerShape(18.dp),
                                                colors = CardDefaults.cardColors(
                                                    containerColor = Color.White
                                                ),
                                                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                                            ) {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(20.dp),
                                                verticalArrangement = Arrangement.spacedBy(12.dp)
                                            ) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Text(
                                                        text = "Pedido #${pedido.id}",
                                                        fontSize = 20.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = TextPrimaryLight
                                                    )
                                                    StatusBadge(
                                                        text = pedido.tipoPedido ?: "N/A",
                                                        statusType = if (pedido.tipoPedido == "Exportación") StatusType.INFO else StatusType.SUCCESS
                                                    )
                                                }

                                                    Divider(
                                                        color = Color.LightGray.copy(alpha = 0.25f),
                                                        thickness = 1.dp
                                                    )

                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Column(modifier = Modifier.weight(1f)) {
                                                        Text(
                                                            text = "OC Cliente",
                                                            fontSize = 12.sp,
                                                            color = TextSecondaryLight,
                                                            fontWeight = FontWeight.Medium
                                                        )
                                                        VerticalSpacer(4.dp)
                                                        Text(
                                                            text = pedido.ocCliente ?: "N/A",
                                                            fontSize = 15.sp,
                                                            color = TextPrimaryLight,
                                                            fontWeight = FontWeight.SemiBold
                                                        )
                                                    }
                                                    Column(modifier = Modifier.weight(1f)) {
                                                        Text(
                                                            text = "Fecha",
                                                            fontSize = 12.sp,
                                                            color = TextSecondaryLight,
                                                            fontWeight = FontWeight.Medium
                                                        )
                                                        VerticalSpacer(4.dp)
                                                        Text(
                                                            text = pedido.fechaCreacion?.let {
                                                                try {
                                                                    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                                                                    val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                                                    val date = inputFormat.parse(it)
                                                                    outputFormat.format(date ?: "")
                                                                } catch (e: Exception) {
                                                                    it
                                                                }
                                                            } ?: "N/A",
                                                            fontSize = 15.sp,
                                                            color = TextPrimaryLight,
                                                            fontWeight = FontWeight.SemiBold
                                                        )
                                                    }
                                                }

                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Column(modifier = Modifier.weight(1f)) {
                                                        Text(
                                                            text = "Moneda",
                                                            fontSize = 12.sp,
                                                            color = TextSecondaryLight,
                                                            fontWeight = FontWeight.Medium
                                                        )
                                                        VerticalSpacer(4.dp)
                                                        Text(
                                                            text = "${pedido.moneda ?: "N/A"} (TRM: ${pedido.trm ?: 0.0})",
                                                            fontSize = 15.sp,
                                                            color = TextPrimaryLight,
                                                            fontWeight = FontWeight.SemiBold
                                                        )
                                                    }
                                                }

                                                // Mostrar ubicación siempre (departamento, ciudad)
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Column(modifier = Modifier.weight(1f)) {
                                                        Text(
                                                            text = "Ubicación",
                                                            fontSize = 12.sp,
                                                            color = TextSecondaryLight,
                                                            fontWeight = FontWeight.Medium
                                                        )
                                                        VerticalSpacer(4.dp)
                                                        Text(
                                                            text = buildString {
                                                                val partes = mutableListOf<String>()
                                                                
                                                                // Mostrar departamento primero
                                                                if (!pedido.departamentoNombre.isNullOrEmpty() && pedido.departamentoNombre != "N/A") {
                                                                    partes.add(pedido.departamentoNombre!!)
                                                                }
                                                                
                                                                // Luego ciudad
                                                                if (!pedido.ciudadNombre.isNullOrEmpty() && pedido.ciudadNombre != "N/A") {
                                                                    partes.add(pedido.ciudadNombre!!)
                                                                }
                                                                
                                                                if (partes.isNotEmpty()) {
                                                                    append(partes.joinToString(", "))
                                                                } else {
                                                                    // Si no hay nombres pero hay IDs, mostrar IDs como fallback
                                                                    val idsPartes = mutableListOf<String>()
                                                                    if (pedido.departamentoId != null) {
                                                                        idsPartes.add("Dept ID: ${pedido.departamentoId}")
                                                                    }
                                                                    if (pedido.ciudadId != null) {
                                                                        idsPartes.add("Ciudad ID: ${pedido.ciudadId}")
                                                                    }
                                                                    if (idsPartes.isNotEmpty()) {
                                                                        append(idsPartes.joinToString(", "))
                                                                    } else {
                                                                        append("N/A")
                                                                    }
                                                                }
                                                            },
                                                            fontSize = 14.sp,
                                                            color = TextPrimaryLight,
                                                            fontWeight = FontWeight.SemiBold
                                                        )
                                                    }
                                                }
                                                VerticalSpacer(8.dp)

                                                Text(
                                                    text = "Condición: ${pedido.condicionPago ?: "N/A"}",
                                                    fontSize = 14.sp,
                                                    color = TextSecondaryLight
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        is OrdersUiState.Error -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "Error al cargar pedidos",
                                        fontSize = 16.sp,
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
                    }
                    }
                }
            }
        }
    }
}