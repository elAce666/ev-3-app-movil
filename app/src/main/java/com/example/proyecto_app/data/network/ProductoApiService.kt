package com.example.proyecto_app.data.network

import com.example.proyecto_app.data.model.producto.Producto
import retrofit2.Response
import retrofit2.http.*

interface ProductoApiService {

    // --- Operaciones CRUD para Productos ---

    // GET (obtener todos)
    @GET("productos")
    suspend fun getProductos(): List<Producto>

    // GET (obtener uno por ID)
    @GET("productos/{id}")
    suspend fun getProductoById(@Path("id") id: Long): Producto

    // POST (crear uno nuevo)
    @POST("productos")
    suspend fun createProducto(@Body producto: Producto): Producto

    // PUT (actualizar uno existente)
    @PUT("productos/{id}")
    suspend fun updateProducto(@Path("id") id: Long, @Body producto: Producto): Producto

    // DELETE (eliminar uno)
    @DELETE("productos/{id}")
    suspend fun deleteProducto(@Path("id") id: Long): Response<Void> // Usamos Response<Void> para operaciones sin cuerpo de respuesta
}
