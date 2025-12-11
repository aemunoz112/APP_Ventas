package co.edu.anders.proyectoventas.domain.usecase

import co.edu.anders.proyectoventas.data.models.DetallePedido
import co.edu.anders.proyectoventas.data.models.Pedido
import co.edu.anders.proyectoventas.data.repository.OrderRepository

/**
 * Caso de uso para gestionar pedidos
 * Encapsula la lógica de negocio relacionada con pedidos
 */
class GetOrdersUseCase(
    private val orderRepository: OrderRepository = OrderRepository()
) {
    /**
     * Obtiene todos los pedidos
     */
    operator fun invoke(): List<Pedido> {
        return orderRepository.getAllOrders()
    }
    
    /**
     * Obtiene un pedido por ID
     */
    fun getById(id: Int): Pedido? {
        return orderRepository.getOrderById(id)
    }
    
    /**
     * Obtiene los detalles de un pedido
     */
    fun getDetails(orderId: Int): List<DetallePedido> {
        return orderRepository.getOrderDetails(orderId)
    }
    
    /**
     * Calcula el total de un pedido
     */
    fun calculateTotal(orderId: Int): Double {
        val details = getDetails(orderId)
        return details.sumOf { it.precioTotal ?: 0.0 }
    }
    
    /**
     * Obtiene estadísticas de pedidos
     */
    fun getStatistics(): OrderStatistics {
        val orders = invoke()
        return OrderStatistics(
            totalOrders = orders.size,
            totalAmount = orders.flatMap { orderRepository.getOrderDetails(it.id) }
                .sumOf { it.precioTotal ?: 0.0 },
            pendingOrders = orders.count { it.tipoPedido == "Nacional" },
            exportOrders = orders.count { it.tipoPedido == "Exportación" }
        )
    }
}

/**
 * Data class para estadísticas de pedidos
 */
data class OrderStatistics(
    val totalOrders: Int,
    val totalAmount: Double,
    val pendingOrders: Int,
    val exportOrders: Int
)

