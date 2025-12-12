package co.edu.anders.proyectoventas.feature.home

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.edu.anders.proyectoventas.feature.products.ProductsViewModel
import co.edu.anders.proyectoventas.feature.products.ProductsUiState
import co.edu.anders.proyectoventas.ui.components.appbar.CustomAppBar
import co.edu.anders.proyectoventas.ui.components.bottomnav.BottomNavBar
import co.edu.anders.proyectoventas.ui.components.cards.ProductCard
import co.edu.anders.proyectoventas.ui.components.spacer.VerticalSpacer
import co.edu.anders.proyectoventas.ui.theme.PrimaryBlue
import co.edu.anders.proyectoventas.ui.theme.ProyectoVentasTheme
import co.edu.anders.proyectoventas.ui.theme.TextPrimaryLight

@Composable
fun HomeScreen(
    onNavigateToAssistant: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToProduct: (Int) -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val viewModel: ProductsViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    
    var selectedBottomItem by remember { mutableIntStateOf(0) }
    
    // Cargar productos destacados al iniciar
    LaunchedEffect(Unit) {
        viewModel.loadFeaturedProducts(6)
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
            color = Color.White
        ) {
            Scaffold(
                containerColor = Color.White,
                topBar = {
                    CustomAppBar(
                        title = "Inicio",
                        actions = {
                            IconButton(onClick = onNavigateToSearch) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Buscar"
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
                                0 -> {} // Ya estamos en Home
                                1 -> onNavigateToSearch()
                                2 -> onNavigateToAssistant()
                                3 -> onNavigateToOrders()
                                4 -> onNavigateToProfile()
                            }
                        }
                    )
                }
            ) { padding ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(padding)
                    .padding(horizontal = 20.dp)
                    .padding(top = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Título de bienvenida
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Bienvenido",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryLight,
                        letterSpacing = 0.5.sp
                    )
                    VerticalSpacer(8.dp)
                    Text(
                        text = "Explora nuestros productos de acero",
                        fontSize = 16.sp,
                        color = TextPrimaryLight.copy(alpha = 0.7f),
                        lineHeight = 22.sp
                    )
                }
                
                // Sección de productos destacados
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Productos Destacados",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryLight
                        )
                        when (val state = uiState) {
                            is ProductsUiState.Success -> {
                                Text(
                                    text = "${state.products.size} productos",
                                    fontSize = 14.sp,
                                    color = TextPrimaryLight.copy(alpha = 0.6f)
                                )
                            }
                            else -> {}
                        }
                    }
                    
                    VerticalSpacer(16.dp)
                    
                    when (val state = uiState) {
                        is ProductsUiState.Loading -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = PrimaryBlue)
                            }
                        }
                        is ProductsUiState.Success -> {
                            if (state.products.isEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(32.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "No hay productos disponibles",
                                        fontSize = 16.sp,
                                        color = TextPrimaryLight.copy(alpha = 0.6f)
                                    )
                                }
                            } else {
                                // Mostrar productos destacados
                                state.products.forEach { producto ->
                                    ProductCard(
                                        title = producto.nombreProducto,
                                        description = "${producto.descripcion ?: ""}\nCódigo: ${producto.codigoProducto}",
                                        onClick = { onNavigateToProduct(producto.id) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 16.dp)
                                    )
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
                                Text(
                                    text = "Error al cargar productos",
                                    fontSize = 16.sp,
                                    color = TextPrimaryLight.copy(alpha = 0.6f)
                                )
                            }
                        }
                        else -> {}
                    }
                }
                
                VerticalSpacer(32.dp)
            }
            }
        }
    }
}
