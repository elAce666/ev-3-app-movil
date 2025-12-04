package com.example.proyecto_app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_app.data.model.WeatherResponse
import com.example.proyecto_app.data.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// EXPLICACIÓN (Punto J): Este es el ViewModel para nuestra API externa.
// Su lógica es muy simple: pide el clima al repositorio y expone el resultado.
class WeatherViewModel : ViewModel() {

    private val repository = WeatherRepository()

    private val _weatherState = MutableStateFlow<WeatherResponse?>(null)
    val weatherState = _weatherState.asStateFlow()

    init {
        fetchWeatherForSantiago()
    }

    private fun fetchWeatherForSantiago() {
        viewModelScope.launch {
            // Coordenadas de Santiago de Chile
            val lat = -33.45
            val lon = -70.67

            repository.getWeather(lat, lon).collect {
                _weatherState.value = it
            }
        }
    }
}
