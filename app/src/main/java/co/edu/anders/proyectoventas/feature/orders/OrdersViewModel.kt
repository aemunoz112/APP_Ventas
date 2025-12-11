package co.edu.anders.proyectoventas.feature.orders

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.anders.proyectoventas.data.models.DetallePedido
import co.edu.anders.proyectoventas.data.models.Pedido
import co.edu.anders.proyectoventas.data.repository.ApiOrderRepository
import co.edu.anders.proyectoventas.data.repository.LocationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Estados de la UI para pedidos
 */
sealed class OrdersUiState {
    object Idle : OrdersUiState()
    object Loading : OrdersUiState()
    data class Success(val orders: List<Pedido>) : OrdersUiState()
    data class Error(val message: String) : OrdersUiState()
}

/**
 * Estados de la UI para detalles de pedido
 */
sealed class OrderDetailUiState {
    object Idle : OrderDetailUiState()
    object Loading : OrderDetailUiState()
    data class Success(val order: Pedido, val details: List<DetallePedido>) : OrderDetailUiState()
    data class Error(val message: String) : OrderDetailUiState()
}

/**
 * ViewModel para gestión de pedidos
 */
class OrdersViewModel : ViewModel() {
    
    private val repository = ApiOrderRepository()
    private val locationRepository = LocationRepository()
    
    private val _uiState = MutableStateFlow<OrdersUiState>(OrdersUiState.Idle)
    val uiState: StateFlow<OrdersUiState> = _uiState.asStateFlow()
    
    private val _orderDetailState = MutableStateFlow<OrderDetailUiState>(OrderDetailUiState.Idle)
    val orderDetailState: StateFlow<OrderDetailUiState> = _orderDetailState.asStateFlow()
    
    init {
        loadOrders()
    }
    
    /**
     * Carga todos los pedidos desde la API y obtiene nombres de ubicación
     */
    fun loadOrders() {
        _uiState.value = OrdersUiState.Loading
        
        viewModelScope.launch {
            val result = repository.getAllOrders()
            
            if (result.isSuccess) {
                val pedidos = result.getOrNull() ?: emptyList()
                Log.d("OrdersViewModel", "Pedidos cargados: ${pedidos.size}")
                
                // Obtener nombres de departamento y ciudad para cada pedido
                val pedidosConUbicacion = pedidos.map { pedido ->
                    Log.d("OrdersViewModel", "Procesando pedido ${pedido.id} - Dept ID: ${pedido.departamentoId}, Ciudad ID: ${pedido.ciudadId}")
                    
                    if (pedido.departamentoNombre == null || pedido.ciudadNombre == null) {
                        val (deptNombre, ciuNombre) = locationRepository.getUbicacionNombres(
                            pedido.departamentoId,
                            pedido.ciudadId
                        )
                        Log.d("OrdersViewModel", "Pedido ${pedido.id} - Dept: $deptNombre, Ciudad: $ciuNombre")
                        pedido.copy(
                            departamentoNombre = deptNombre,
                            ciudadNombre = ciuNombre
                        )
                    } else {
                        Log.d("OrdersViewModel", "Pedido ${pedido.id} ya tiene nombres: Dept: ${pedido.departamentoNombre}, Ciudad: ${pedido.ciudadNombre}")
                        pedido
                    }
                }
                _uiState.value = OrdersUiState.Success(pedidosConUbicacion)
            } else {
                _uiState.value = OrdersUiState.Error(result.exceptionOrNull()?.message ?: "Error al cargar pedidos")
            }
        }
    }
    
    /**
     * Carga los detalles de un pedido específico y obtiene nombres de ubicación
     */
    fun loadOrderDetails(orderId: Int) {
        _orderDetailState.value = OrderDetailUiState.Loading
        
        viewModelScope.launch {
            val orderResult = repository.getOrderById(orderId)
            val detailsResult = repository.getOrderDetails(orderId)
            
            if (orderResult.isSuccess && detailsResult.isSuccess) {
                var order = orderResult.getOrNull()!!
                val details = detailsResult.getOrNull() ?: emptyList()
                
                Log.d("OrdersViewModel", "Detalle pedido ${order.id} - Dept ID: ${order.departamentoId}, Ciudad ID: ${order.ciudadId}")
                
                // Obtener nombres de departamento y ciudad si no están
                if (order.departamentoNombre == null || order.ciudadNombre == null) {
                    val (deptNombre, ciuNombre) = locationRepository.getUbicacionNombres(
                        order.departamentoId,
                        order.ciudadId
                    )
                    Log.d("OrdersViewModel", "Detalle pedido ${order.id} - Dept: $deptNombre, Ciudad: $ciuNombre")
                    order = order.copy(
                        departamentoNombre = deptNombre,
                        ciudadNombre = ciuNombre
                    )
                } else {
                    Log.d("OrdersViewModel", "Detalle pedido ${order.id} ya tiene nombres: Dept: ${order.departamentoNombre}, Ciudad: ${order.ciudadNombre}")
                }
                
                _orderDetailState.value = OrderDetailUiState.Success(order, details)
            } else {
                val errorMessage = orderResult.exceptionOrNull()?.message 
                    ?: detailsResult.exceptionOrNull()?.message 
                    ?: "Error al cargar detalles del pedido"
                _orderDetailState.value = OrderDetailUiState.Error(errorMessage)
            }
        }
    }
}

