package co.edu.anders.proyectoventas.data.repository

import co.edu.anders.proyectoventas.data.models.Producto
import co.edu.anders.proyectoventas.data.sample.SampleData

/**
 * Repositorio para gestionar productos
 * Por ahora usa datos de muestra, pero puede extenderse para usar una API o base de datos
 */
class ProductRepository {
    
    /**
     * Obtiene todos los productos
     */
    fun getAllProducts(): List<Producto> {
        return SampleData.productos
    }
    
    /**
     * Obtiene un producto por ID
     */
    fun getProductById(id: Int): Producto? {
        return SampleData.productos.find { it.id == id }
    }
    
    /**
     * Busca productos por término de búsqueda
     */
    fun searchProducts(query: String): List<Producto> {
        if (query.isBlank()) {
            return getAllProducts()
        }
        
        val lowerQuery = query.lowercase()
        return SampleData.productos.filter {
            it.nombreProducto.lowercase().contains(lowerQuery) ||
            it.codigoProducto.lowercase().contains(lowerQuery) ||
            it.categoria?.lowercase()?.contains(lowerQuery) == true ||
            it.descripcion?.lowercase()?.contains(lowerQuery) == true
        }
    }
    
    /**
     * Obtiene productos destacados
     */
    fun getFeaturedProducts(limit: Int = 6): List<Producto> {
        return SampleData.productos.take(limit)
    }
}

