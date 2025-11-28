package com.example.proyecto_app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyecto_app.data.model.producto.Producto
import com.example.proyecto_app.ui.viewmodels.CartViewModel
import com.example.proyecto_app.ui.viewmodels.ProductoViewModel

@Composable
fun ProductoScreen(cartViewModel: CartViewModel) {
    val viewModel: ProductoViewModel = viewModel()
    val productos by viewModel.productos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Ya no hay diálogo ni botón flotante

    Scaffold {
        padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (productos.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No se encontraron productos.")
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(productos) { producto ->
                        ProductoItem(producto = producto, onAddToCart = { cartViewModel.addToCart(producto) })
                    }
                }
            }
        }
    }
}

@Composable
fun ProductoItem(producto: Producto, onAddToCart: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = producto.nombre, style = MaterialTheme.typography.titleMedium)
                Text(text = "Precio: $${String.format("%.2f", producto.precio)}", style = MaterialTheme.typography.bodySmall)
                Text(text = "Stock: ${producto.stock}", style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = onAddToCart) {
                Icon(Icons.Default.AddShoppingCart, contentDescription = "Añadir al carrito")
            }
        }
    }
}
