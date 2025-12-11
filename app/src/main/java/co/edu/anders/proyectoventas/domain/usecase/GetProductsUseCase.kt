package co.edu.anders.proyectoventas.domain.usecase

import co.edu.anders.proyectoventas.data.models.Producto
import co.edu.anders.proyectoventas.data.repository.ProductRepository

/**
 * Caso de uso para obtener productos
 * Encapsula la lógica de negocio relacionada con la obtención de productos
 */
class GetProductsUseCase(
    private val productRepository: ProductRepository = ProductRepository()
) {
    /**
     * Obtiene todos los productos disponibles
     */
    operator fun invoke(): List<Producto> {
        return productRepository.getAllProducts()
    }
    
    /**
     * Obtiene productos destacados
     */
    fun getFeatured(limit: Int = 6): List<Producto> {
        return productRepository.getFeaturedProducts(limit)
    }
    
    /**
     * Busca productos por término
     */
    fun search(query: String): List<Producto> {
        return productRepository.searchProducts(query)
    }
    
    /**
     * Obtiene un producto por ID
     */
    fun getById(id: Int): Producto? {
        return productRepository.getProductById(id)
    }
}

