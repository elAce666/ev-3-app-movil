package com.example.proyecto_app.data.repository

import com.example.proyecto_app.data.model.producto.Producto
import com.example.proyecto_app.data.network.ProductoApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.emptyFlow

class ProductoRepository(private val apiService: ProductoApiService) {

    fun getProductos(): Flow<List<Producto>> = flow {
        emit(apiService.getProductos())
    }.catch {
        emit(emptyList())
    }

    // Si la creación falla, el catch ahora devuelve un Flow vacío
    fun createProducto(producto: Producto): Flow<Producto> = flow {
        emit(apiService.createProducto(producto))
    }.catch { 
        // Al emitir un flow vacío, el `collect` en el ViewModel no se ejecutará,
        // lo cual es un comportamiento predecible.
        emptyFlow<Producto>()
    }

    // Lo mismo para la actualización
    fun updateProducto(id: Long, producto: Producto): Flow<Producto> = flow {
        emit(apiService.updateProducto(id, producto))
    }.catch { 
        emptyFlow<Producto>()
     }

    fun deleteProducto(id: Long): Flow<Boolean> = flow {
        val response = apiService.deleteProducto(id)
        emit(response.isSuccessful)
    }.catch {
        emit(false)
    }
}
