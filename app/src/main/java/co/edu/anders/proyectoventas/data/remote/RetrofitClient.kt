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
    
    // URL base de la API usando ngrok
    private const val BASE_URL = "https://nonceremonially-unwary-livia.ngrok-free.dev/"
    
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
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
        
        // Log de la URL que se está llamando
        android.util.Log.d("RetrofitClient", "=== REQUEST ===")
        android.util.Log.d("RetrofitClient", "URL: ${originalRequest.url}")
        android.util.Log.d("RetrofitClient", "Method: ${originalRequest.method}")
        android.util.Log.d("RetrofitClient", "Headers: ${originalRequest.headers}")
        
        // Agregar token si existe
        authToken?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }
        
        // Agregar headers comunes
        requestBuilder.addHeader("Content-Type", "application/json")
        requestBuilder.addHeader("Accept", "application/json")
        
        // Header necesario para ngrok free (evita la página de advertencia)
        requestBuilder.addHeader("ngrok-skip-browser-warning", "true")
        
        val newRequest = requestBuilder.build()
        val response = chain.proceed(newRequest)
        
        // Log de la respuesta
        android.util.Log.d("RetrofitClient", "=== RESPONSE ===")
        android.util.Log.d("RetrofitClient", "Code: ${response.code}")
        android.util.Log.d("RetrofitClient", "Message: ${response.message}")
        android.util.Log.d("RetrofitClient", "Headers: ${response.headers}")
        
        response
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

