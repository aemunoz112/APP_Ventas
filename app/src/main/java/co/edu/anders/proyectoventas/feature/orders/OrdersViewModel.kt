package co.edu.anders.proyectoventas.feature.orders

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.content.Context
import co.edu.anders.proyectoventas.data.local.UserPreferences
import co.edu.anders.proyectoventas.data.models.DetallePedido
import co.edu.anders.proyectoventas.data.models.Pedido
import co.edu.anders.proyectoventas.data.repository.ApiOrderRepository
import co.edu.anders.proyectoventas.data.repository.LocationRepository
import co.edu.anders.proyectoventas.data.remote.RetrofitClient
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
class OrdersViewModel(
    private val context: Context? = null
) : ViewModel() {
    
    private val repository = ApiOrderRepository()
    private val locationRepository = LocationRepository()
    private val userPreferences = context?.let { UserPreferences(it) }
    
    private val _uiState = MutableStateFlow<OrdersUiState>(OrdersUiState.Idle)
    val uiState: StateFlow<OrdersUiState> = _uiState.asStateFlow()
    
    private val _orderDetailState = MutableStateFlow<OrderDetailUiState>(OrderDetailUiState.Idle)
    val orderDetailState: StateFlow<OrderDetailUiState> = _orderDetailState.asStateFlow()
    
    private val _clienteNombre = MutableStateFlow<String?>(null)
    val clienteNombre: StateFlow<String?> = _clienteNombre.asStateFlow()
    
    private val _vendedorNombre = MutableStateFlow<String?>(null)
    val vendedorNombre: StateFlow<String?> = _vendedorNombre.asStateFlow()
    
    init {
        loadOrders()
    }
    
    /**
     * Carga todos los pedidos desde la API y obtiene nombres de ubicación
     */
    fun loadOrders() {
        Log.d("OrdersViewModel", "=== INICIANDO loadOrders() ===")
        _uiState.value = OrdersUiState.Loading
        
        viewModelScope.launch {
            try {
                Log.d("OrdersViewModel", "Llamando a repository.getAllOrders()...")
                val result = repository.getAllOrders()
                Log.d("OrdersViewModel", "Resultado recibido - Success: ${result.isSuccess}")
                
                if (result.isSuccess) {
                    var pedidos = result.getOrNull() ?: emptyList()
                    Log.d("OrdersViewModel", "Pedidos cargados desde API: ${pedidos.size}")
                    
                    // Filtrar pedidos por vendedor logueado si hay contexto
                    val vendedorId = userPreferences?.getUserId()
                    if (vendedorId != null && vendedorId != -1) {
                        val pedidosFiltrados = pedidos.filter { it.idVendedor == vendedorId }
                        Log.d("OrdersViewModel", "Pedidos filtrados por vendedor ID $vendedorId: ${pedidosFiltrados.size} de ${pedidos.size}")
                        pedidos = pedidosFiltrados
                    } else {
                        Log.w("OrdersViewModel", "No se pudo obtener ID del vendedor logueado, mostrando todos los pedidos")
                    }
                    
                    // MOSTRAR LOS PEDIDOS INMEDIATAMENTE sin esperar los nombres de ubicación
                    Log.d("OrdersViewModel", "Mostrando pedidos inmediatamente...")
                    _uiState.value = OrdersUiState.Success(pedidos)
                    
                    // Luego, obtener nombres de ubicación de forma asíncrona y actualizar
                    // Para TODOS los pedidos que tengan IDs pero nombres vacíos, null o "N/A"
                    val pedidosSinUbicacion = pedidos.filter { pedido ->
                        val necesitaDept = pedido.departamentoId != null && 
                                          (pedido.departamentoNombre == null || 
                                           pedido.departamentoNombre.isEmpty() || 
                                           pedido.departamentoNombre == "N/A")
                        val necesitaCiudad = pedido.ciudadId != null && 
                                            (pedido.ciudadNombre == null || 
                                             pedido.ciudadNombre.isEmpty() || 
                                             pedido.ciudadNombre == "N/A")
                        necesitaDept || necesitaCiudad
                    }
                    
                    if (pedidosSinUbicacion.isNotEmpty()) {
                        Log.d("OrdersViewModel", "Obteniendo nombres de ubicación para ${pedidosSinUbicacion.size} pedidos...")
                        
                        // Actualizar cada pedido de forma individual cuando se obtenga su ubicación
                        pedidosSinUbicacion.forEach { pedido ->
                            viewModelScope.launch {
                                try {
                                    var deptNombre = pedido.departamentoNombre
                                    var ciuNombre = pedido.ciudadNombre
                                    
                                    // Obtener departamento si es necesario
                                    if (pedido.departamentoId != null && (deptNombre == null || deptNombre.isEmpty() || deptNombre == "N/A")) {
                                        val result = locationRepository.getDepartamentoNombre(pedido.departamentoId)
                                        if (result.isSuccess) {
                                            val nombre = result.getOrNull()
                                            if (!nombre.isNullOrEmpty() && nombre != "N/A") {
                                                deptNombre = nombre
                                                Log.d("OrdersViewModel", "Departamento obtenido para pedido ${pedido.id}: $deptNombre")
                                            }
                                        }
                                    }
                                    
                                    // Obtener ciudad si es necesario
                                    if (pedido.ciudadId != null && (ciuNombre == null || ciuNombre.isEmpty() || ciuNombre == "N/A")) {
                                        val result = locationRepository.getCiudadNombre(pedido.ciudadId)
                                        if (result.isSuccess) {
                                            val nombre = result.getOrNull()
                                            if (!nombre.isNullOrEmpty() && nombre != "N/A") {
                                                ciuNombre = nombre
                                                Log.d("OrdersViewModel", "Ciudad obtenida para pedido ${pedido.id}: $ciuNombre")
                                            }
                                        }
                                    }
                                    
                                    // Actualizar el pedido específico en la lista actual
                                    val currentState = _uiState.value
                                    if (currentState is OrdersUiState.Success) {
                                        val updatedPedidos = currentState.orders.map { p ->
                                            if (p.id == pedido.id) {
                                                p.copy(
                                                    departamentoNombre = deptNombre,
                                                    ciudadNombre = ciuNombre
                                                )
                                            } else {
                                                p
                                            }
                                        }
                                        _uiState.value = OrdersUiState.Success(updatedPedidos)
                                        Log.d("OrdersViewModel", "Actualizado pedido ${pedido.id} con ubicación - Dept: $deptNombre, Ciudad: $ciuNombre")
                                    }
                                } catch (e: Exception) {
                                    Log.w("OrdersViewModel", "No se pudo obtener ubicación para pedido ${pedido.id}: ${e.message}")
                                    // Continuar con el siguiente pedido
                                }
                            }
                        }
                    }
                } else {
                    val errorMsg = result.exceptionOrNull()?.message ?: "Error al cargar pedidos"
                    Log.e("OrdersViewModel", "Error al obtener pedidos: $errorMsg")
                    _uiState.value = OrdersUiState.Error(errorMsg)
                }
            } catch (e: Exception) {
                Log.e("OrdersViewModel", "Excepción inesperada al cargar pedidos: ${e.message}", e)
                _uiState.value = OrdersUiState.Error("Error inesperado: ${e.message}")
            }
        }
    }
    
    /**
     * Carga los detalles de un pedido específico y obtiene nombres de ubicación y cliente
     */
    fun loadOrderDetails(orderId: Int) {
        _orderDetailState.value = OrderDetailUiState.Loading
        _clienteNombre.value = null
        _vendedorNombre.value = null
        
        viewModelScope.launch {
            val orderResult = repository.getOrderById(orderId)
            val detailsResult = repository.getOrderDetails(orderId)
            
            if (orderResult.isSuccess && detailsResult.isSuccess) {
                var order = orderResult.getOrNull()!!
                val details = detailsResult.getOrNull() ?: emptyList()
                
                Log.d("OrdersViewModel", "Detalle pedido ${order.id} - Dept ID: ${order.departamentoId}, Ciudad ID: ${order.ciudadId}")
                Log.d("OrdersViewModel", "Detalle pedido ${order.id} - Dept Nombre inicial: ${order.departamentoNombre}, Ciudad Nombre inicial: ${order.ciudadNombre}")
                
                // SIEMPRE intentar obtener nombres de departamento y ciudad si hay IDs
                // Incluso si ya hay nombres, intentar actualizarlos por si acaso están vacíos o son "N/A"
                var deptNombre = order.departamentoNombre
                var ciuNombre = order.ciudadNombre
                
                if (order.departamentoId != null && (deptNombre == null || deptNombre.isEmpty() || deptNombre == "N/A")) {
                    Log.d("OrdersViewModel", "Obteniendo nombre de departamento para ID: ${order.departamentoId}")
                    val result = locationRepository.getDepartamentoNombre(order.departamentoId)
                    if (result.isSuccess) {
                        val nombre = result.getOrNull()
                        if (!nombre.isNullOrEmpty() && nombre != "N/A") {
                            deptNombre = nombre
                            Log.d("OrdersViewModel", "Departamento obtenido: $deptNombre")
                        }
                    }
                }
                
                if (order.ciudadId != null && (ciuNombre == null || ciuNombre.isEmpty() || ciuNombre == "N/A")) {
                    Log.d("OrdersViewModel", "Obteniendo nombre de ciudad para ID: ${order.ciudadId}")
                    val result = locationRepository.getCiudadNombre(order.ciudadId)
                    if (result.isSuccess) {
                        val nombre = result.getOrNull()
                        if (!nombre.isNullOrEmpty() && nombre != "N/A") {
                            ciuNombre = nombre
                            Log.d("OrdersViewModel", "Ciudad obtenida: $ciuNombre")
                        }
                    }
                }
                
                order = order.copy(
                    departamentoNombre = deptNombre,
                    ciudadNombre = ciuNombre
                )
                
                Log.d("OrdersViewModel", "Detalle pedido ${order.id} - FINAL - Dept: ${order.departamentoNombre}, Ciudad: ${order.ciudadNombre}")
                
                // Obtener información del cliente y vendedor
                loadClienteInfo(order.idCliente)
                loadVendedorInfo(order.idVendedor)
                
                // Obtener nombres de productos para cada detalle
                val detallesConProductos = details.map { detalle ->
                    if (detalle.producto?.nombreProducto == null || detalle.producto?.nombreProducto?.isEmpty() == true) {
                        // Si no tiene producto o no tiene nombre, obtenerlo
                        val productoNombre = loadProductoNombre(detalle.idProducto)
                        if (productoNombre != null) {
                            // Crear o actualizar el producto con el nombre obtenido
                            val productoActualizado = detalle.producto?.copy(
                                nombreProducto = productoNombre
                            ) ?: co.edu.anders.proyectoventas.data.models.Producto(
                                id = detalle.idProducto,
                                codigoProducto = "",
                                nombreProducto = productoNombre,
                                descripcion = null,
                                categoria = null,
                                unidadMedida = null,
                                estado = null
                            )
                            detalle.copy(producto = productoActualizado)
                        } else {
                            // Si no se pudo obtener, crear un producto mínimo con el ID
                            val productoMinimo = detalle.producto?.copy(
                                nombreProducto = "Producto #${detalle.idProducto}"
                            ) ?: co.edu.anders.proyectoventas.data.models.Producto(
                                id = detalle.idProducto,
                                codigoProducto = "",
                                nombreProducto = "Producto #${detalle.idProducto}",
                                descripcion = null,
                                categoria = null,
                                unidadMedida = null,
                                estado = null
                            )
                            detalle.copy(producto = productoMinimo)
                        }
                    } else {
                        detalle
                    }
                }
                
                _orderDetailState.value = OrderDetailUiState.Success(order, detallesConProductos)
            } else {
                val errorMessage = orderResult.exceptionOrNull()?.message 
                    ?: detailsResult.exceptionOrNull()?.message 
                    ?: "Error al cargar detalles del pedido"
                _orderDetailState.value = OrderDetailUiState.Error(errorMessage)
            }
        }
    }
    
    /**
     * Obtiene el nombre de empresa o nombre del cliente
     */
    private suspend fun loadClienteInfo(clienteId: Int) {
        try {
            Log.d("OrdersViewModel", "Obteniendo información del cliente ID: $clienteId")
            val response = RetrofitClient.apiService.obtenerUsuario(clienteId)
            
            if (response.isSuccessful && response.body() != null) {
                val cliente = response.body()!!
                
                // Buscar "nombre empresa" en los atributos (puede estar con diferentes nombres)
                val nombreEmpresa = cliente.atributos?.let { atributos ->
                    atributos["nombre empresa"] 
                        ?: atributos["Nombre Empresa"]
                        ?: atributos["nombre_empresa"]
                        ?: atributos["Nombre_Empresa"]
                        ?: atributos["Empresa"]
                        ?: atributos["empresa"]
                }
                
                val nombreCliente = if (nombreEmpresa != null && nombreEmpresa.isNotEmpty()) {
                    nombreEmpresa
                } else {
                    // Usar nombre completo del cliente
                    "${cliente.nombres} ${cliente.apellidos}".trim()
                }
                
                Log.d("OrdersViewModel", "Cliente encontrado - Nombre empresa: $nombreEmpresa, Nombre completo: ${cliente.nombres} ${cliente.apellidos}")
                _clienteNombre.value = nombreCliente
            } else {
                Log.w("OrdersViewModel", "No se pudo obtener información del cliente: ${response.code()}")
                _clienteNombre.value = null
            }
        } catch (e: Exception) {
            Log.e("OrdersViewModel", "Error al obtener información del cliente: ${e.message}", e)
            _clienteNombre.value = null
        }
    }
    
    /**
     * Obtiene el nombre del vendedor
     */
    private suspend fun loadVendedorInfo(vendedorId: Int) {
        try {
            Log.d("OrdersViewModel", "Obteniendo información del vendedor ID: $vendedorId")
            val response = RetrofitClient.apiService.obtenerUsuario(vendedorId)
            
            if (response.isSuccessful && response.body() != null) {
                val vendedor = response.body()!!
                val nombreVendedor = "${vendedor.nombres} ${vendedor.apellidos}".trim()
                
                Log.d("OrdersViewModel", "Vendedor encontrado - Nombre: $nombreVendedor")
                _vendedorNombre.value = nombreVendedor
            } else {
                Log.w("OrdersViewModel", "No se pudo obtener información del vendedor: ${response.code()}")
                _vendedorNombre.value = null
            }
        } catch (e: Exception) {
            Log.e("OrdersViewModel", "Error al obtener información del vendedor: ${e.message}", e)
            _vendedorNombre.value = null
        }
    }
    
    /**
     * Obtiene el nombre de un producto por su ID
     */
    private suspend fun loadProductoNombre(productoId: Int): String? {
        return try {
            Log.d("OrdersViewModel", "Obteniendo nombre del producto ID: $productoId")
            val response = RetrofitClient.apiService.obtenerProducto(productoId)
            
            if (response.isSuccessful && response.body() != null) {
                val producto = response.body()!!
                val nombre = producto.nombreProducto
                Log.d("OrdersViewModel", "Producto encontrado - Nombre: $nombre")
                nombre
            } else {
                Log.w("OrdersViewModel", "No se pudo obtener información del producto: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("OrdersViewModel", "Error al obtener información del producto: ${e.message}", e)
            null
        }
    }
}

