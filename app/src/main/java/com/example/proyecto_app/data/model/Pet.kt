package com.example.proyecto_app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Pet(
    val name: String,
    val type: String,
    // Nuevos campos
    val fechaEntrada: String? = null,
    val fechaSalida: String? = null,
    val servicios: List<String> = emptyList()
)
