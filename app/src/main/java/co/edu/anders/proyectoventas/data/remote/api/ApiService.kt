package co.edu.anders.proyectoventas.data.remote.api

import co.edu.anders.proyectoventas.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

/**
 * Definición de la API REST
 * Endpoints del backend FastAPI
 */
interface ApiService {
    
    // ==================== AUTH ====================
    @POST("auth/login")
    suspend fun login(@Body credentials: LoginRequest): Response<LoginResponse>
    
    // ==================== PRODUCTOS ====================
    @GET("productos/")
    suspend fun listarProductos(): Response<List<ProductoDto>>
    
    @GET("productos/{producto_id}")
    suspend fun obtenerProducto(@Path("producto_id") productoId: Int): Response<ProductoDto>
    
    @POST("productos/")
    suspend fun crearProducto(@Body producto: ProductoCreateDto): Response<ProductoDto>
    
    @PUT("productos/{producto_id}")
    suspend fun actualizarProducto(
        @Path("producto_id") productoId: Int,
        @Body producto: ProductoUpdateDto
    ): Response<ProductoDto>
    
    @DELETE("productos/{producto_id}")
    suspend fun eliminarProducto(@Path("producto_id") productoId: Int): Response<Unit>
    
    // ==================== DIMENSIONES ====================
    @GET("dimensiones/")
    suspend fun listarDimensiones(): Response<List<DimensionesDto>>
    
    @GET("dimensiones/{dimension_id}")
    suspend fun obtenerDimension(@Path("dimension_id") dimensionId: Int): Response<DimensionesDto>
    
    // ==================== INVENTARIO ====================
    @GET("get_inventarios/")
    suspend fun listarInventarios(): Response<List<InventarioDto>>
    
    @GET("get_inventario/{inventario_id}")
    suspend fun obtenerInventario(@Path("inventario_id") inventarioId: Int): Response<InventarioDto>
    
    // ==================== PEDIDOS ====================
    @GET("get_encabezados_pedidos/")
    suspend fun listarPedidos(): Response<PedidosResponseDto>
    
    @GET("get_encabezado_pedido/{encabezado_id}")
    suspend fun obtenerPedido(@Path("encabezado_id") pedidoId: Int): Response<PedidoDto>
    
    @GET("get_encabezados_by_cliente/{id_cliente}")
    suspend fun obtenerPedidosPorCliente(@Path("id_cliente") clienteId: Int): Response<PedidosResponseDto>
    
    // ==================== DETALLES PEDIDO ====================
    @GET("get_detalles_pedidos/")
    suspend fun listarDetallesPedidos(): Response<List<DetallePedidoDto>>
    
    @GET("get_detalles_by_pedido/{id_pedido}")
    suspend fun obtenerDetallesPorPedido(@Path("id_pedido") pedidoId: Int): Response<DetallesPedidoResponseDto>
    
    // ==================== ORDENES PRODUCCIÓN ====================
    @GET("get_ordenes_produccion/")
    suspend fun listarOrdenesProduccion(): Response<List<OrdenProduccionDto>>
    
    // ==================== USUARIOS ====================
    @GET("get_users/")
    suspend fun listarUsuarios(): Response<List<UsuarioDto>>
    
    @GET("get_user/{user_id}")
    suspend fun obtenerUsuario(@Path("user_id") userId: Int): Response<UsuarioDto>
    
    @POST("create_user")
    suspend fun crearUsuario(@Body usuario: UsuarioCreateDto): Response<UsuarioDto>
    
    @PUT("update_user/{user_id}")
    suspend fun actualizarUsuario(
        @Path("user_id") userId: Int,
        @Body usuario: UsuarioUpdateDto
    ): Response<UsuarioDto>
    
    @DELETE("delete_user/{user_id}")
    suspend fun eliminarUsuario(@Path("user_id") userId: Int): Response<Unit>
    
    // ==================== ROLES ====================
    @GET("get_roles/")
    suspend fun listarRoles(): Response<List<RolDto>>
    
    @GET("get_rol/{rol_id}")
    suspend fun obtenerRol(@Path("rol_id") rolId: Int): Response<RolDto>
    
    @POST("create_rol")
    suspend fun crearRol(@Body rol: RolCreateDto): Response<RolDto>
    
    @PUT("update_rol/{rol_id}")
    suspend fun actualizarRol(
        @Path("rol_id") rolId: Int,
        @Body rol: RolUpdateDto
    ): Response<RolDto>
    
    @DELETE("delete_rol/{rol_id}")
    suspend fun eliminarRol(@Path("rol_id") rolId: Int): Response<Unit>
    
    // ==================== FAVORITOS ====================
    @GET("usuarios/{usuario_id}/favoritos")
    suspend fun listarFavoritos(
        @Path("usuario_id") usuarioId: Int,
        @Query("rol_id") rolId: Int? = null
    ): Response<List<FavoritoDto>>
    
    @POST("usuarios/{usuario_id}/favoritos")
    suspend fun crearFavorito(
        @Path("usuario_id") usuarioId: Int,
        @Body favorito: FavoritoCreateDto,
        @Query("rol_id") rolId: Int? = null
    ): Response<FavoritoDto>
    
    @PATCH("usuarios/{usuario_id}/favoritos/{favorito_id}")
    suspend fun actualizarFavorito(
        @Path("usuario_id") usuarioId: Int,
        @Path("favorito_id") favoritoId: Int,
        @Body favorito: FavoritoUpdateDto,
        @Query("rol_id") rolId: Int? = null
    ): Response<FavoritoDto>
    
    @DELETE("usuarios/{usuario_id}/favoritos/{favorito_id}")
    suspend fun eliminarFavorito(
        @Path("usuario_id") usuarioId: Int,
        @Path("favorito_id") favoritoId: Int
    ): Response<Unit>
    
    // ==================== VENTAS ====================
    @GET("ventas/")
    suspend fun listarVentas(): Response<List<VentaDto>>
    
    @GET("ventas/{venta_id}")
    suspend fun obtenerVenta(@Path("venta_id") ventaId: Int): Response<VentaDto>
    
    @POST("ventas/")
    suspend fun crearVenta(@Body venta: VentaCreateDto): Response<VentaDto>
    
    @PUT("ventas/{venta_id}")
    suspend fun actualizarVenta(
        @Path("venta_id") ventaId: Int,
        @Body venta: VentaUpdateDto
    ): Response<VentaDto>
    
    @DELETE("ventas/{venta_id}")
    suspend fun eliminarVenta(@Path("venta_id") ventaId: Int): Response<Unit>
}

