package co.edu.anders.proyectoventas.navigation

/**
 * Rutas de navegación de la app
 * Contiene todas las rutas como constantes para evitar errores de tipeo
 */
object Routes {
    // Autenticación
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val SIGN_UP = "signup"
    
    // Principal
    const val HOME = "home"
    const val DASHBOARD = "dashboard"  // Dashboard empresarial
    
    // Asistente Virtual
    const val ASSISTANT = "assistant"
    
    // Productos
    const val PRODUCT_DETAIL = "product_detail/{productId}"
    const val SEARCH = "search"
    
    // Pedidos
    const val ORDERS = "orders"
    const val ORDER_DETAIL = "order_detail/{orderId}"
    
    // Perfil
    const val PROFILE = "profile"
    
    // Funciones helper para construir rutas con parámetros
    fun productDetail(productId: Int) = "product_detail/$productId"
    fun orderDetail(orderId: Int) = "order_detail/$orderId"
}

