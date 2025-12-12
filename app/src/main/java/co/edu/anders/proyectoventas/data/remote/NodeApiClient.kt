package co.edu.anders.proyectoventas.data.remote

import co.edu.anders.proyectoventas.data.remote.api.NodeApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Cliente Retrofit para conexión con la API de Node.js (departamentos y ciudades)
 */
object NodeApiClient {
    
    // URL de la API de Node.js usando Dev Tunnels -  reciente cambio en la URL
    private const val NODE_API_BASE_URL = "https://3sm4zxjz-8001.use.devtunnels.ms/"
    
    /**
     * Interceptor para logging (solo headers, no body para evitar spam de HTML)
     */
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.HEADERS // Solo headers, no body
    }
    
    /**
     * Cliente OkHttp con configuración
     */
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    
    /**
     * Instancia de Retrofit para Node.js API
     */
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(NODE_API_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    /**
     * Instancia del servicio API de Node.js
     */
    val nodeApiService: NodeApiService = retrofit.create(NodeApiService::class.java)
}

