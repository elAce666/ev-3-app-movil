package com.example.proyecto_app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_app.data.model.producto.Producto
import com.example.proyecto_app.data.repository.ProductoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProductoViewModel(
    // El repositorio ya no necesita argumentos en el constructor
    private val repository: ProductoRepository = ProductoRepository(),
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos = _productos.asStateFlow()
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    // Lista de ejemplo para poblar el backend si está vacío
    private val productosDeEjemplo = listOf(
        Producto(nombre = "Comida Premium para Perro (3kg)", precio = 25990.0, stock = 45),
        Producto(nombre = "Rascador para Gato XL", precio = 45000.0, stock = 15),
        Producto(nombre = "Juguete Cuerda Resistente", precio = 8500.0, stock = 120),
        Producto(nombre = "Arena Sanitaria Aglomerante (5kg)", precio = 12990.0, stock = 70),
        Producto(nombre = "Antiparasitario Canino (Pack 3)", precio = 18000.0, stock = 90),
        Producto(nombre = "Champú Hipoalergénico", precio = 9990.0, stock = 155),
        Producto(nombre = "Arnés Reflectante Talla M", precio = 14500.0, stock = 60)
    )

    init {
        getProductos()
    }

    fun getProductos() {
        viewModelScope.launch(dispatcher) {
            _isLoading.value = true
            repository.getProductos()
                .catch { e -> 
                    _errorMessage.value = "Error: ${e.message}"
                    _isLoading.value = false 
                }
                .collect { productosFromApi ->
                    if (productosFromApi.isEmpty()) {
                        poblarYRefrescar()
                    } else {
                        _productos.value = productosFromApi
                        _isLoading.value = false
                    }
                }
        }
    }

    private fun poblarYRefrescar() {
        viewModelScope.launch(dispatcher) {
            try {
                productosDeEjemplo.forEach { repository.createProducto(it).collect() }
                // Después de poblar, volvemos a obtener la lista actualizada
                repository.getProductos().collect {
                    _productos.value = it
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                 _errorMessage.value = "Error al poblar: ${e.message}"
                 _isLoading.value = false
            }
        }
    }
}
