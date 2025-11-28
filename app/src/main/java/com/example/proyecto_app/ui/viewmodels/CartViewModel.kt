package com.example.proyecto_app.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_app.data.model.cart.CartItem
import com.example.proyecto_app.data.model.compra.Compra
import com.example.proyecto_app.data.model.producto.Producto
import com.example.proyecto_app.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CartViewModel(application: Application) : AndroidViewModel(application) {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems = _cartItems.asStateFlow()

    private val userRepository = UserRepository(application)

    // ... (addToCart, removeFromCart, etc. no cambian)
    fun addToCart(producto: Producto) {
        _cartItems.update { currentItems ->
            val existingItem = currentItems.find { it.producto.id == producto.id }
            if (existingItem != null) {
                currentItems.map { if (it.producto.id == producto.id) it.copy(quantity = it.quantity + 1) else it }
            } else {
                currentItems + CartItem(producto)
            }
        }
    }

    fun removeFromCart(cartItem: CartItem) { _cartItems.update { it - cartItem } }
    fun incrementQuantity(cartItem: CartItem) { addToCart(cartItem.producto) } // Simplificado
    fun decrementQuantity(cartItem: CartItem) {
        _cartItems.update { currentItems ->
            if (cartItem.quantity > 1) {
                currentItems.map { if (it.producto.id == cartItem.producto.id) it.copy(quantity = it.quantity - 1) else it }
            } else {
                currentItems.filter { it.producto.id != cartItem.producto.id }
            }
        }
    }

    // --- Lógica para finalizar la compra ---
    fun realizarCompra() {
        viewModelScope.launch {
            val currentUser = userRepository.getUser().first()
            if (currentUser != null && _cartItems.value.isNotEmpty()) {
                val total = _cartItems.value.sumOf { it.producto.precio * it.quantity }
                val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                val nuevaCompra = Compra(
                    items = _cartItems.value,
                    fecha = formatter.format(Date()),
                    total = total
                )

                val updatedUser = currentUser.copy(
                    historialCompras = currentUser.historialCompras + nuevaCompra
                )

                userRepository.saveUser(updatedUser)
                
                // Limpiar el carrito
                _cartItems.value = emptyList()
            }
        }
    }
}
