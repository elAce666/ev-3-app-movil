package com.example.proyecto_app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyecto_app.data.model.cart.CartItem
import com.example.proyecto_app.ui.viewmodels.CartViewModel

@Composable
fun CartScreen(cartViewModel: CartViewModel) {
    val cartItems by cartViewModel.cartItems.collectAsState()
    
    val total = cartItems.sumOf { it.producto.precio * it.quantity }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Carrito de Compras", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))

        if (cartItems.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Tu carrito está vacío.")
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(cartItems) { item ->
                    CartItemView(
                        item = item,
                        onIncrement = { cartViewModel.incrementQuantity(item) },
                        onDecrement = { cartViewModel.decrementQuantity(item) },
                        onRemove = { cartViewModel.removeFromCart(item) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Total: $${String.format("%.2f", total)}", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))
            
            // Conectamos el botón a la lógica del ViewModel
            Button(
                onClick = { cartViewModel.realizarCompra() }, 
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Proceder al Pago")
            }
        }
    }
}

@Composable
fun CartItemView(item: CartItem, onIncrement: () -> Unit, onDecrement: () -> Unit, onRemove: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(item.producto.nombre, style = MaterialTheme.typography.titleMedium)
                Text("Precio: $${item.producto.precio}")
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(onClick = onDecrement) { Icon(Icons.Default.Remove, "Decrementar") }
                Text(item.quantity.toString())
                IconButton(onClick = onIncrement) { Icon(Icons.Default.Add, "Incrementar") }
                IconButton(onClick = onRemove) { Icon(Icons.Default.Delete, "Eliminar") }
            }
        }
    }
}
