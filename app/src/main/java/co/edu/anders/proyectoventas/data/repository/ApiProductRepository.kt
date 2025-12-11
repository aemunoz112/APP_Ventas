package co.edu.anders.proyectoventas.data.repository

import co.edu.anders.proyectoventas.data.models.Producto
import co.edu.anders.proyectoventas.data.remote.RetrofitClient
import co.edu.anders.proyectoventas.data.remote.mapper.toDomain

/**
 * Repositorio de productos que usa la API REST
 * Reemplaza ProductRepository cuando el backend esté disponible
 */
class ApiProductRepository {
    
    private val api = RetrofitClient.apiService
    
    /**
     * Obtiene todos los productos desde la API
     */
    suspend fun getAllProducts(): Result<List<Producto>> {
        return try {
            val response = api.listarProductos()
            if (response.isSuccessful && response.body() != null) {
                val productos = response.body()!!.map { it.toDomain() }
                Result.success(productos)
            } else {
                Result.failure(Exception("Error al obtener productos: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Obtiene un producto por ID desde la API
     */
    suspend fun getProductById(id: Int): Result<Producto> {
        return try {
            val response = api.obtenerProducto(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toDomain())
            } else {
                Result.failure(Exception("Producto no encontrado"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Busca productos por término
     */
    suspend fun searchProducts(query: String): Result<List<Producto>> {
        return try {
            // Obtener todos y filtrar localmente
            // TODO: Implementar búsqueda en el backend
            val result = getAllProducts()
            if (result.isSuccess) {
                val productos = result.getOrNull()!!
                val filtered = productos.filter {
                    it.nombreProducto.contains(query, ignoreCase = true) ||
                    it.codigoProducto.contains(query, ignoreCase = true) ||
                    it.categoria?.contains(query, ignoreCase = true) == true
                }
                Result.success(filtered)
            } else {
                result
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Obtiene productos destacados
     */
    suspend fun getFeaturedProducts(limit: Int = 6): Result<List<Producto>> {
        return try {
            val result = getAllProducts()
            if (result.isSuccess) {
                val productos = result.getOrNull()!!.take(limit)
                Result.success(productos)
            } else {
                result
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

