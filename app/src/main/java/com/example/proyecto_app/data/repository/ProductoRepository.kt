package com.example.proyecto_app.data.repository

import com.example.proyecto_app.data.model.producto.Producto
import com.example.proyecto_app.data.network.ApiProvider
import com.example.proyecto_app.data.network.ProductoApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class ProductoRepository() {

    // Obtenemos la instancia del servicio directamente desde nuestro ApiProvider centralizado
    private val productoApiService: ProductoApiService = ApiProvider.productoService

    fun getProductos(): Flow<List<Producto>> = flow {
        emit(productoApiService.getProductos())
    }.catch {
        emit(emptyList())
    }

    fun createProducto(producto: Producto): Flow<Producto> = flow {
        emit(productoApiService.createProducto(producto))
    }.catch { /* Manejo de error */ }

    fun updateProducto(id: Long, producto: Producto): Flow<Producto> = flow {
        emit(productoApiService.updateProducto(id, producto))
    }.catch { /* Manejo de error */ }

    fun deleteProducto(id: Long): Flow<Boolean> = flow {
        val response = productoApiService.deleteProducto(id)
        emit(response.isSuccessful)
    }.catch {
        emit(false)
    }
}
