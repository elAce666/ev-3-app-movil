package com.example.proyecto_app.data.model

import com.example.proyecto_app.data.model.compra.Compra
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val fullName: String,
    val email: String,
    val profilePictureUri: String? = null,
    val pets: List<Pet> = emptyList(),
    
    // Campos para el perfil
    val genero: String? = null,
    val edad: Int? = null,
    val direccion: String? = null,
    val telefono: String? = null,
    val historialCompras: List<Compra> = emptyList()
)
