package com.example.proyecto_app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val fullName: String,
    val email: String,
    val profilePictureUri: String? = null, // Guardaremos la URI como String
    val pets: List<Pet> = emptyList()
)
