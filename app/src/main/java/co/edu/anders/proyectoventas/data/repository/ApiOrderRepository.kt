package co.edu.anders.proyectoventas.data.repository

import co.edu.anders.proyectoventas.data.models.DetallePedido
import co.edu.anders.proyectoventas.data.models.Pedido
import co.edu.anders.proyectoventas.data.remote.RetrofitClient
import co.edu.anders.proyectoventas.data.remote.mapper.toDomain

/**
 * Repositorio de pedidos que usa la API REST
 */
class ApiOrderRepository {
    
    private val api = RetrofitClient.apiService
    
    /**
     * Obtiene todos los pedidos desde la API
     */
    suspend fun getAllOrders(): Result<List<Pedido>> {
        return try {
            val response = api.listarPedidos()
            if (response.isSuccessful && response.body() != null) {
                val pedidos = response.body()!!.resultado.map { it.toDomain() }
                Result.success(pedidos)
            } else {
                Result.failure(Exception("Error al obtener pedidos: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Obtiene un pedido por ID
     */
    suspend fun getOrderById(id: Int): Result<Pedido> {
        return try {
            val response = api.obtenerPedido(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toDomain())
            } else {
                Result.failure(Exception("Pedido no encontrado"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Obtiene los detalles de un pedido
     */
    suspend fun getOrderDetails(orderId: Int): Result<List<DetallePedido>> {
        return try {
            val response = api.obtenerDetallesPorPedido(orderId)
            if (response.isSuccessful && response.body() != null) {
                val detalles = response.body()!!.resultado.map { it.toDomain() }
                Result.success(detalles)
            } else {
                Result.failure(Exception("Error al obtener detalles del pedido: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Obtiene pedidos por cliente
     */
    suspend fun getOrdersByClient(clientId: Int): Result<List<Pedido>> {
        return try {
            val response = api.obtenerPedidosPorCliente(clientId)
            if (response.isSuccessful && response.body() != null) {
                val pedidos = response.body()!!.resultado.map { it.toDomain() }
                Result.success(pedidos)
            } else {
                Result.failure(Exception("Error al obtener pedidos del cliente: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

