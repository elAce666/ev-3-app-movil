package com.example.proyecto_app.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Data classes para decodificar la respuesta JSON de la API del clima.
@Serializable
data class WeatherResponse(
    val latitude: Double,
    val longitude: Double,
    @SerialName("current_weather")
    val currentWeather: CurrentWeather
)

@Serializable
data class CurrentWeather(
    val temperature: Double,
    @SerialName("windspeed")
    val windSpeed: Double,
    @SerialName("weathercode")
    val weatherCode: Int
)
