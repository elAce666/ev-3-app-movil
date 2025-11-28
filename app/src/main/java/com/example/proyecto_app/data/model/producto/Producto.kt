package com.example.proyecto_app.data.model.producto

import kotlinx.serialization.Serializable

@Serializable
data class Producto(
    val id: Long? = null, // El ID puede ser nulo al crear un nuevo producto
    val nombre: String,
    val precio: Double,
    val stock: Int
)
