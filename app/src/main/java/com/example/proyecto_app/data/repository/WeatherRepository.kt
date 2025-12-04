package com.example.proyecto_app.data.repository

import com.example.proyecto_app.data.model.WeatherResponse
import com.example.proyecto_app.data.network.WeatherApiClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRepository {

    private val api = WeatherApiClient.instance

    // EXPLICACIÓN: Esta es la forma correcta y robusta de manejar errores en un Flow que puede emitir null.
    // Usamos un bloque try-catch explícito dentro del constructor del flow.
    fun getWeather(latitude: Double, longitude: Double): Flow<WeatherResponse?> = flow {
        try {
            val response = api.getWeather(latitude, longitude)
            emit(response)
        } catch (e: Exception) {
            // Si ocurre cualquier excepción de red, emitimos null.
            emit(null)
        }
    }
}
