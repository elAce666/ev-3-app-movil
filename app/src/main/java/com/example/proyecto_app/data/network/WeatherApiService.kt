package com.example.proyecto_app.data.network

import com.example.proyecto_app.data.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

// EXPLICACIÓN (Punto J): Esta es la interfaz que define cómo hablamos con la API externa del clima.
// Cada función representa un "endpoint" de la API.
interface WeatherApiService {

    // EXPLICACIÓN: Usamos la anotación @GET para una petición GET al endpoint "forecast".
    // Los parámetros se pasan con @Query, que los añade a la URL (ej: ?latitude=...).
    @GET("v1/forecast")
    suspend fun getWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current_weather") currentWeather: Boolean = true
    ): WeatherResponse
}
