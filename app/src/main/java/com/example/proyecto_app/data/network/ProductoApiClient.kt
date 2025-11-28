package com.example.proyecto_app.data.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

// EXPLICACIÓN (Punto I y J): Este objeto configura y crea el cliente Retrofit.
// Retrofit es la librería estándar para realizar llamadas de red a una API REST.
// Se usaría tanto para nuestro backend de Spring Boot como para cualquier API externa.
object ProductoApiClient {

    // EXPLICACIÓN: Esta es la URL base de nuestro backend. Para que el emulador de Android
    // pueda conectarse a un servidor que corre en el mismo computador, usamos la IP especial 10.0.2.2.
    private const val BASE_URL = "http://10.0.2.2:8080/api/v1/"

    // Configuración para que Retrofit entienda JSON y lo convierta a nuestras data classes.
    private val contentType = "application/json".toMediaType()
    private val json = Json { ignoreUnknownKeys = true }

    // Construcción del objeto Retrofit.
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(json.asConverterFactory(contentType))
        .build()

    // Creación de la instancia del servicio que podemos usar en nuestro repositorio.
    // Retrofit implementa automáticamente la interfaz `ProductoApiService`.
    val instance: ProductoApiService by lazy {
        retrofit.create(ProductoApiService::class.java)
    }
}
