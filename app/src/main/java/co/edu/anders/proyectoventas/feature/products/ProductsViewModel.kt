package co.edu.anders.proyectoventas.feature.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.anders.proyectoventas.data.models.Producto
import co.edu.anders.proyectoventas.data.repository.ApiProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Estados de la UI para productos
 */
sealed class ProductsUiState {
    object Idle : ProductsUiState()
    object Loading : ProductsUiState()
    data class Success(val products: List<Producto>) : ProductsUiState()
    data class Error(val message: String) : ProductsUiState()
}

/**
 * ViewModel para gestión de productos
 */
class ProductsViewModel : ViewModel() {
    
    private val repository = ApiProductRepository()
    
    private val _uiState = MutableStateFlow<ProductsUiState>(ProductsUiState.Idle)
    val uiState: StateFlow<ProductsUiState> = _uiState.asStateFlow()
    
    private val _products = MutableStateFlow<List<Producto>>(emptyList())
    val products: StateFlow<List<Producto>> = _products.asStateFlow()
    
    init {
        loadProducts()
    }
    
    /**
     * Carga todos los productos desde la API
     */
    fun loadProducts() {
        _uiState.value = ProductsUiState.Loading
        
        viewModelScope.launch {
            val result = repository.getAllProducts()
            
            _uiState.value = if (result.isSuccess) {
                val productos = result.getOrNull() ?: emptyList()
                _products.value = productos
                ProductsUiState.Success(productos)
            } else {
                ProductsUiState.Error(result.exceptionOrNull()?.message ?: "Error desconocido")
            }
        }
    }
    
    /**
     * Busca productos por término
     */
    fun searchProducts(query: String) {
        if (query.isBlank()) {
            _uiState.value = ProductsUiState.Success(_products.value)
            return
        }
        
        _uiState.value = ProductsUiState.Loading
        
        viewModelScope.launch {
            val result = repository.searchProducts(query)
            
            _uiState.value = if (result.isSuccess) {
                val productos = result.getOrNull() ?: emptyList()
                ProductsUiState.Success(productos)
            } else {
                ProductsUiState.Error(result.exceptionOrNull()?.message ?: "Error en la búsqueda")
            }
        }
    }
    
    /**
     * Obtiene productos destacados
     */
    fun loadFeaturedProducts(limit: Int = 6) {
        _uiState.value = ProductsUiState.Loading
        
        viewModelScope.launch {
            val result = repository.getFeaturedProducts(limit)
            
            _uiState.value = if (result.isSuccess) {
                val productos = result.getOrNull() ?: emptyList()
                ProductsUiState.Success(productos)
            } else {
                ProductsUiState.Error(result.exceptionOrNull()?.message ?: "Error al cargar productos")
            }
        }
    }
}

