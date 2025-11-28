package com.example.proyecto_app.data.network

object JsonPlaceholderApiClient {
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    val instance: JsonPlaceholderApi by lazy {
        ApiClient.getClient(BASE_URL).create(JsonPlaceholderApi::class.java)
    }
}
