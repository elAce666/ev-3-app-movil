package com.example.proyecto_app.data.model.cart

import com.example.proyecto_app.data.model.producto.Producto
import kotlinx.serialization.Serializable

// Añadimos @Serializable para que pueda ser convertido a JSON
@Serializable
data class CartItem(
    val producto: Producto,
    val quantity: Int = 1
)
