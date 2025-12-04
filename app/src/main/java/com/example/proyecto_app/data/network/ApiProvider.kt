package com.example.proyecto_app.data.network

object ApiProvider {

    // Cliente para nuestro backend de Spring Boot
    private val ourApiClient = ApiClient.getClient("http://10.0.2.2:8080/api/v1/")
    
    // Cliente para la API externa del clima
    private val weatherApiClient = ApiClient.getClient("https://api.open-meteo.com/")

    // --- Proveedores de Servicios ---

    val productoService: ProductoApiService by lazy {
        ourApiClient.create(ProductoApiService::class.java)
    }

    val petService: PetApiService by lazy {
        ourApiClient.create(PetApiService::class.java)
    }

    val weatherService: WeatherApiService by lazy {
        weatherApiClient.create(WeatherApiService::class.java)
    }
}
