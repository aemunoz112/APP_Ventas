package co.edu.anders.proyectoventas.feature.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.anders.proyectoventas.data.models.Producto
import co.edu.anders.proyectoventas.data.repository.ApiProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Estados de la UI para detalle de producto
 */
sealed class ProductDetailUiState {
    object Idle : ProductDetailUiState()
    object Loading : ProductDetailUiState()
    data class Success(val product: Producto) : ProductDetailUiState()
    data class Error(val message: String) : ProductDetailUiState()
}

/**
 * ViewModel para detalle de producto
 */
class ProductViewModel : ViewModel() {
    
    private val repository = ApiProductRepository()
    
    private val _uiState = MutableStateFlow<ProductDetailUiState>(ProductDetailUiState.Idle)
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()
    
    /**
     * Carga un producto por ID desde la API
     */
    fun loadProduct(productId: Int) {
        _uiState.value = ProductDetailUiState.Loading
        
        viewModelScope.launch {
            val result = repository.getProductById(productId)
            
            _uiState.value = if (result.isSuccess) {
                val producto = result.getOrNull()!!
                ProductDetailUiState.Success(producto)
            } else {
                ProductDetailUiState.Error(result.exceptionOrNull()?.message ?: "Producto no encontrado")
            }
        }
    }
}

