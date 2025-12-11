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
    
    // URL de la API de Node.js
    // Para dispositivo físico: usa la IP de tu PC (misma que el backend Python)
    private const val NODE_API_BASE_URL = "http://192.168.1.88:3000/"
    
    // Para emulador, usa esta:
    // private const val NODE_API_BASE_URL = "http://10.0.2.2:3000/"
    
    /**
     * Interceptor para logging (solo en desarrollo)
     */
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
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

