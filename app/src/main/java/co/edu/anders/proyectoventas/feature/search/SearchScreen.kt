package co.edu.anders.proyectoventas.feature.search

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import co.edu.anders.proyectoventas.ui.components.pullrefresh.PullToRefresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.edu.anders.proyectoventas.feature.products.ProductsViewModel
import co.edu.anders.proyectoventas.feature.products.ProductsUiState
import co.edu.anders.proyectoventas.ui.components.appbar.CustomAppBar
import co.edu.anders.proyectoventas.ui.components.bottomnav.BottomNavBar
import co.edu.anders.proyectoventas.ui.components.cards.ProductCard
import co.edu.anders.proyectoventas.ui.components.empty.EmptyState
import co.edu.anders.proyectoventas.ui.components.loading.ProductSkeletonLoader
import co.edu.anders.proyectoventas.ui.components.search.SearchBar
import co.edu.anders.proyectoventas.ui.components.spacer.VerticalSpacer
import co.edu.anders.proyectoventas.ui.theme.PrimaryBlue
import co.edu.anders.proyectoventas.ui.theme.ProyectoVentasTheme
import co.edu.anders.proyectoventas.ui.theme.TextPrimaryLight
import co.edu.anders.proyectoventas.ui.theme.TextSecondaryLight
import kotlinx.coroutines.delay

@Composable
fun SearchScreen(
    onNavigateBack: () -> Unit,
    onProductClick: (Int) -> Unit,
    onNavigateToHome: () -> Unit = {},
    onNavigateToAssistant: () -> Unit = {},
    onNavigateToOrders: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val viewModel: ProductsViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    
    var searchQuery by remember { mutableStateOf("") }
    var selectedBottomItem by remember { mutableIntStateOf(1) }
    
    // Estado de refresh
    val isRefreshing = uiState is ProductsUiState.Loading
    
    // Búsqueda con debounce
    LaunchedEffect(searchQuery) {
        if (searchQuery.isBlank()) {
            viewModel.loadProducts()
        } else {
            delay(500) // Esperar 500ms después de que el usuario deje de escribir
            if (searchQuery.isNotBlank()) {
                viewModel.searchProducts(searchQuery)
            }
        }
    }
    
    // Manejar errores
    LaunchedEffect(uiState) {
        if (uiState is ProductsUiState.Error) {
            Toast.makeText(context, (uiState as ProductsUiState.Error).message, Toast.LENGTH_LONG).show()
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
                                PrimaryBlue.copy(alpha = 0.08f),
                                Color.White
                            )
                        )
                    )
            )
            Scaffold(
                containerColor = Color.Transparent,
                topBar = {
                    CustomAppBar(
                        title = "Buscar Productos"
                    )
                },
                bottomBar = {
                    BottomNavBar(
                        selectedItem = selectedBottomItem,
                        onItemSelected = { index ->
                            selectedBottomItem = index
                            when (index) {
                                0 -> onNavigateToHome()
                                1 -> {} // Ya estamos en Search
                                2 -> onNavigateToAssistant()
                                3 -> onNavigateToOrders()
                                4 -> onNavigateToProfile()
                            }
                        }
                    )
                }
            ) { padding ->
                PullToRefresh(
                    isRefreshing = isRefreshing,
                    onRefresh = {
                        if (searchQuery.isBlank()) {
                            viewModel.loadProducts()
                        } else {
                            viewModel.searchProducts(searchQuery)
                        }
                    },
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
                    Text(
                        text = "Encuentra productos de acero",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryLight
                    )
                    Text(
                        text = "Busca por código, nombre o categoría.",
                        fontSize = 13.sp,
                        color = TextSecondaryLight
                    )
                    SearchBar(
                        query = searchQuery,
                        onQueryChange = { searchQuery = it },
                        placeholder = "Buscar por código, nombre o categoría...",
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    when (val state = uiState) {
                        is ProductsUiState.Loading -> {
                            ProductSkeletonLoader(
                                modifier = Modifier.fillMaxWidth(),
                                itemCount = 5
                            )
                        }
                        is ProductsUiState.Success -> {
                            if (searchQuery.isNotBlank()) {
                                Text(
                                    text = "${state.products.size} resultado${if (state.products.size != 1) "s" else ""} encontrado${if (state.products.size != 1) "s" else ""}",
                                    fontSize = 14.sp,
                                    color = TextPrimaryLight.copy(alpha = 0.6f),
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )
                            }
                            
                            if (state.products.isEmpty()) {
                                EmptyState(
                                    title = if (searchQuery.isNotBlank()) {
                                        "No se encontraron productos"
                                    } else {
                                        "Buscar productos"
                                    },
                                    message = if (searchQuery.isNotBlank()) {
                                        "Intenta con otros términos de búsqueda"
                                    } else {
                                        "Busca productos por código, nombre o categoría"
                                    },
                                    defaultIcon = Icons.Default.Search,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            } else {
                                LazyColumn(
                                    verticalArrangement = Arrangement.spacedBy(16.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    items(
                                        items = state.products,
                                        key = { it.id }
                                    ) { producto ->
                                        // Animación de entrada suave
                                        var itemAlpha by remember(producto.id) { mutableFloatStateOf(0f) }
                                        val animatedAlpha by animateFloatAsState(
                                            targetValue = itemAlpha,
                                            animationSpec = tween(durationMillis = 300),
                                            label = "item_alpha_${producto.id}"
                                        )
                                        
                                        LaunchedEffect(producto.id) {
                                            itemAlpha = 1f
                                        }
                                        
                                        ProductCard(
                                            title = producto.nombreProducto,
                                            description = "${producto.descripcion ?: ""}\nCódigo: ${producto.codigoProducto} | Categoría: ${producto.categoria ?: "N/A"}",
                                            onClick = { onProductClick(producto.id) },
                                            additionalInfo = {
                                                if (producto.inventario != null) {
                                                    co.edu.anders.proyectoventas.ui.components.badges.StatusBadge(
                                                        text = "Disponible: ${producto.inventario.cantidadDisponible} ${producto.unidadMedida ?: ""}",
                                                        statusType = co.edu.anders.proyectoventas.ui.components.badges.StatusType.SUCCESS
                                                    )
                                                }
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .alpha(animatedAlpha)
                                        )
                                    }
                                }
                            }
                        }
                        is ProductsUiState.Error -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "Error al buscar productos",
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
                        else -> {}
                    }
                    }
                }
            }
        }
    }
}

