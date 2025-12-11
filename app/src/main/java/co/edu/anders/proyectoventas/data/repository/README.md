# Data Repository Package

Este paquete contiene los repositorios que gestionan el acceso a datos.

## Repositorios

### `ProductRepository`
Gestiona el acceso a datos de productos:
- `getAllProducts()`: Obtiene todos los productos
- `getProductById(id)`: Obtiene un producto por ID
- `searchProducts(query)`: Busca productos por término
- `getFeaturedProducts(limit)`: Obtiene productos destacados

### `OrderRepository`
Gestiona el acceso a datos de pedidos:
- `getAllOrders()`: Obtiene todos los pedidos
- `getOrderById(id)`: Obtiene un pedido por ID
- `getOrderDetails(orderId)`: Obtiene los detalles de un pedido

### `UserRepository`
Gestiona el acceso a datos de usuarios:
- `getCurrentUser()`: Obtiene el usuario actual
- `getUserById(id)`: Obtiene un usuario por ID

## Uso

```kotlin
val productRepository = ProductRepository()
val products = productRepository.getAllProducts()
val product = productRepository.getProductById(1)
```

## Nota

Actualmente estos repositorios usan datos de muestra (`SampleData`), pero están diseñados para ser fácilmente reemplazables por implementaciones que usen una API o base de datos real.

