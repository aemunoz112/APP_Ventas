package co.edu.anders.proyectoventas.feature.product

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.edu.anders.proyectoventas.feature.product.ProductDetailUiState
import co.edu.anders.proyectoventas.feature.product.ProductViewModel
import co.edu.anders.proyectoventas.ui.components.appbar.CustomAppBar
import co.edu.anders.proyectoventas.ui.components.cards.InfoCard
import co.edu.anders.proyectoventas.ui.components.spacer.VerticalSpacer
import co.edu.anders.proyectoventas.ui.theme.PrimaryBlue
import co.edu.anders.proyectoventas.ui.theme.ProyectoVentasTheme
import co.edu.anders.proyectoventas.ui.theme.TextPrimaryLight
import co.edu.anders.proyectoventas.ui.theme.TextSecondaryLight

@Composable
fun ProductDetailScreen(
    productId: Int,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val viewModel: ProductViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    
    // Cargar producto cuando se abre la pantalla
    LaunchedEffect(productId) {
        viewModel.loadProduct(productId)
    }
    
    // Manejar errores
    LaunchedEffect(uiState) {
        if (uiState is ProductDetailUiState.Error) {
            Toast.makeText(context, (uiState as ProductDetailUiState.Error).message, Toast.LENGTH_LONG).show()
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
            Column(modifier = modifier.fillMaxSize()) {
                CustomAppBar(
                    title = "Detalle del Producto",
                    onNavigationClick = onNavigateBack
                )
                
                when (val state = uiState) {
                    is ProductDetailUiState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = PrimaryBlue)
                        }
                    }
                    is ProductDetailUiState.Success -> {
                        val producto = state.product
                        
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(horizontal = 20.dp)
                                .padding(top = 24.dp)
                                .padding(bottom = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            Text(
                                text = producto.nombreProducto,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimaryLight,
                                letterSpacing = 0.5.sp
                            )
                            
                            Text(
                                text = producto.descripcion ?: "Sin descripción",
                                fontSize = 16.sp,
                                color = TextSecondaryLight,
                                lineHeight = 24.sp
                            )
                            
                            Divider(
                                color = Color.Gray.copy(alpha = 0.2f),
                                thickness = 1.dp
                            )
                            
                            InfoCard(
                                title = "Código de Producto",
                                value = producto.codigoProducto
                            )
                            
                            InfoCard(
                                title = "Categoría",
                                value = producto.categoria ?: "N/A"
                            )
                            
                            InfoCard(
                                title = "Unidad de Medida",
                                value = producto.unidadMedida ?: "N/A"
                            )
                            
                            InfoCard(
                                title = "Estado",
                                value = producto.estado ?: "N/A"
                            )
                            
                            if (producto.dimensiones != null) {
                                VerticalSpacer(8.dp)
                                Divider(
                                    color = Color.Gray.copy(alpha = 0.2f),
                                    thickness = 1.dp
                                )
                                VerticalSpacer(8.dp)
                                Text(
                                    text = "Dimensiones",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimaryLight
                                )
                                
                                producto.dimensiones.ancho?.let {
                                    InfoCard(
                                        title = "Ancho",
                                        value = "$it mm"
                                    )
                                }
                                
                                producto.dimensiones.espesor?.let {
                                    InfoCard(
                                        title = "Espesor",
                                        value = "$it mm"
                                    )
                                }
                                
                                producto.dimensiones.diametroExterno?.let {
                                    InfoCard(
                                        title = "Diámetro Externo",
                                        value = "Ø $it mm"
                                    )
                                }
                                
                                producto.dimensiones.diametroInterno?.let {
                                    InfoCard(
                                        title = "Diámetro Interno",
                                        value = "Ø $it mm"
                                    )
                                }
                            }
                            
                            if (producto.inventario != null) {
                                VerticalSpacer(8.dp)
                                Divider(
                                    color = Color.Gray.copy(alpha = 0.2f),
                                    thickness = 1.dp
                                )
                                VerticalSpacer(8.dp)
                                Text(
                                    text = "Inventario",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimaryLight
                                )
                                
                                InfoCard(
                                    title = "Cantidad Disponible",
                                    value = "${producto.inventario.cantidadDisponible} ${producto.unidadMedida ?: ""}"
                                )
                                
                                producto.inventario.lote?.let {
                                    InfoCard(
                                        title = "Lote",
                                        value = it
                                    )
                                }
                            }
                        }
                    }
                    is ProductDetailUiState.Error -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Error al cargar producto",
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
                    else -> {}
                }
            }
        }
    }
}

