package co.edu.anders.proyectoventas.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTOs para la API de Node.js (departamentos y ciudades)
 */

data class DepartamentoResponseDto(
    @SerializedName("data")
    val data: List<DepartamentoDto>
)

data class DepartamentoDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("nombre")
    val nombre: String
)

// DTOs compatibles con LoginDto (versión simple para el backend Python)
data class DepartamentoSimpleDto(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("nombre")
    val nombre: String?
)

data class CiudadSimpleDto(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("nombre")
    val nombre: String?
)

data class CiudadResponseDto(
    @SerializedName("data")
    val data: List<CiudadDto>
)

data class CiudadDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("nombre")
    val nombre: String,
    @SerializedName("es_capital")
    val esCapitalRaw: Any? = null, // Puede venir como 1/0 (Int) o true/false (Boolean)
    @SerializedName("departamento_id")
    val departamentoId: Int? = null,
    @SerializedName("departamento")
    val departamento: String? = null
) {
    // Convertir es_capital a Boolean
    val esCapital: Boolean?
        get() = when (esCapitalRaw) {
            is Boolean -> esCapitalRaw
            is Int -> esCapitalRaw == 1
            is Number -> esCapitalRaw.toInt() == 1
            else -> null
        }
}

