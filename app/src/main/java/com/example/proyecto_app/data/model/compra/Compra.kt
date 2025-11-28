package com.example.proyecto_app.data.model.compra

import com.example.proyecto_app.data.model.cart.CartItem
import kotlinx.serialization.Serializable

@Serializable
data class Compra(
    val items: List<CartItem>,
    val fecha: String,
    val total: Double
)
