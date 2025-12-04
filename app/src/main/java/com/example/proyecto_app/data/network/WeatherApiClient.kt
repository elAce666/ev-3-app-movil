package com.example.proyecto_app.data.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

// EXPLICACIÓN (Punto J): Este es el cliente Retrofit específico para la API EXTERNA del clima.
// Nótese que es independiente de nuestro ProductoApiClient, lo que demuestra que la arquitectura
// puede manejar múltiples fuentes de datos de red.
object WeatherApiClient {

    private const val BASE_URL = "https://api.open-meteo.com/"

    private val contentType = "application/json".toMediaType()
    private val json = Json { ignoreUnknownKeys = true }

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(json.asConverterFactory(contentType))
        .build()

    val instance: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }
}
