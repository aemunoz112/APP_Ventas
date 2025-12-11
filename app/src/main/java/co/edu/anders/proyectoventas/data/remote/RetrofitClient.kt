package co.edu.anders.proyectoventas.data.remote

import co.edu.anders.proyectoventas.data.remote.api.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Cliente Retrofit para conexión con el backend FastAPI
 */
object RetrofitClient {
    
    // IMPORTANTE: Cambia esta URL por la IP de tu computadora
    // Para emulador Android: usa 10.0.2.2
    // Para dispositivo físico: usa la IP de tu PC en la red local (ej: 192.168.1.100)
    
    // ⚠️ ESTÁS USANDO DISPOSITIVO FÍSICO - Cambia por tu IP:
    private const val BASE_URL = "http://192.168.1.88:8000/"  // Ejemplo: "http://192.168.1.100:8000/"
    
    // Para emulador, usa esta:
    // private const val BASE_URL = "http://10.0.2.2:8000/"
    
    // Token de autenticación (se configurará después del login)
    private var authToken: String? = null
    
    /**
     * Configura el token de autenticación
     */
    fun setAuthToken(token: String?) {
        authToken = token
    }
    
    /**
     * Interceptor para agregar token en las peticiones
     */
    private val authInterceptor = okhttp3.Interceptor { chain ->
        val requestBuilder = chain.request().newBuilder()
        
        // Agregar token si existe
        authToken?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }
        
        // Agregar headers comunes
        requestBuilder.addHeader("Content-Type", "application/json")
        requestBuilder.addHeader("Accept", "application/json")
        
        chain.proceed(requestBuilder.build())
    }
    
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
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    
    /**
     * Instancia de Retrofit
     */
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    /**
     * Instancia del servicio API
     */
    val apiService: ApiService = retrofit.create(ApiService::class.java)
}

