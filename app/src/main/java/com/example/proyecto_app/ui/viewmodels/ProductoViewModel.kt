package com.example.proyecto_app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_app.data.model.producto.Producto
import com.example.proyecto_app.data.network.ProductoApiClient
import com.example.proyecto_app.data.repository.ProductoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProductoViewModel(
    private val repository: ProductoRepository = ProductoRepository(ProductoApiClient.instance),
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos = _productos.asStateFlow()
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

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
            repository.getProductos()
                .onStart { _isLoading.value = true }
                .catch { e -> _errorMessage.value = "Error: ${e.message}" }
                .collect { productosFromApi ->
                    if (productosFromApi.isEmpty()) {
                        // Si la API no devuelve nada, se auto-pobla
                        poblarYRefrescar()
                    } else {
                        _productos.value = productosFromApi
                        _isLoading.value = false // Detenemos la carga solo si hay productos
                    }
                }
        }
    }

    private fun poblarYRefrescar() {
        viewModelScope.launch(dispatcher) {
            // Esta función ahora solo se encarga de crear y luego refrescar
            productosDeEjemplo.forEach { repository.createProducto(it).collect() }
            // Volvemos a llamar a getProductos, que esta vez SÍ encontrará datos.
            repository.getProductos().collect{
                _productos.value = it
                _isLoading.value = false // Detenemos la carga al final del proceso
            }
        }
    }
}
