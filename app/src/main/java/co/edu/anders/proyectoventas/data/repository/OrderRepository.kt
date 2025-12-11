package co.edu.anders.proyectoventas.data.repository

import co.edu.anders.proyectoventas.data.models.DetallePedido
import co.edu.anders.proyectoventas.data.models.Pedido
import co.edu.anders.proyectoventas.data.sample.SampleData

/**
 * Repositorio para gestionar pedidos
 * Por ahora usa datos de muestra, pero puede extenderse para usar una API o base de datos
 */
class OrderRepository {
    
    /**
     * Obtiene todos los pedidos
     */
    fun getAllOrders(): List<Pedido> {
        return SampleData.pedidos
    }
    
    /**
     * Obtiene un pedido por ID
     */
    fun getOrderById(id: Int): Pedido? {
        return SampleData.pedidos.find { it.id == id }
    }
    
    /**
     * Obtiene los detalles de un pedido
     * Por ahora simula los detalles, pero debería venir de una fuente de datos real
     */
    fun getOrderDetails(orderId: Int): List<DetallePedido> {
        val pedido = getOrderById(orderId) ?: return emptyList()
        
        // Simular detalles del pedido
        return listOf(
            DetallePedido(
                id = 1,
                idPedido = pedido.id,
                idProducto = 1,
                numeroLinea = 1,
                cantidadSolicitada = 100.0,
                cantidadConfirmada = 100.0,
                precioUnitario = 45000.0,
                precioTotal = 4500000.0,
                producto = SampleData.productos.firstOrNull()
            ),
            DetallePedido(
                id = 2,
                idPedido = pedido.id,
                idProducto = 2,
                numeroLinea = 2,
                cantidadSolicitada = 50.0,
                cantidadConfirmada = 50.0,
                precioUnitario = 52000.0,
                precioTotal = 2600000.0,
                producto = SampleData.productos.getOrNull(1)
            )
        )
    }
}

