package co.edu.anders.proyectoventas.data.remote.api

import co.edu.anders.proyectoventas.data.remote.dto.CiudadResponseDto
import co.edu.anders.proyectoventas.data.remote.dto.DepartamentoResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Definición de la API REST de Node.js
 * Endpoints para departamentos y ciudades
 */
interface NodeApiService {
    
    /**
     * Obtiene un departamento por ID
     */
    @GET("getDepartamentoById/{id}")
    suspend fun getDepartamentoById(@Path("id") id: Int): Response<DepartamentoResponseDto>
    
    /**
     * Obtiene una ciudad por ID
     */
    @GET("getCiudadById/{id}")
    suspend fun getCiudadById(@Path("id") id: Int): Response<CiudadResponseDto>
    
    /**
     * Obtiene todos los departamentos
     */
    @GET("getDepartamentos")
    suspend fun getDepartamentos(): Response<DepartamentoResponseDto>
    
    /**
     * Obtiene todas las ciudades
     */
    @GET("getCiudades")
    suspend fun getCiudades(): Response<CiudadResponseDto>
    
    /**
     * Obtiene ciudades por departamento
     */
    @GET("getCiudadesByDepartamento/{departamentoId}")
    suspend fun getCiudadesByDepartamento(@Path("departamentoId") departamentoId: Int): Response<CiudadResponseDto>
}

