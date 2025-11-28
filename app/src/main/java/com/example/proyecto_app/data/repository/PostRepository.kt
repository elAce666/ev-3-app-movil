package com.example.proyecto_app.data.repository

import com.example.proyecto_app.data.model.remote.Post
import com.example.proyecto_app.data.network.JsonPlaceholderApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class PostRepository(private val api: JsonPlaceholderApi) {
    
    fun getPosts(): Flow<List<Post>> = flow {
        // 1. Intentamos emitir la respuesta de la API
        emit(api.getPosts())
    }.catch {
        // 2. Si el 'flow' anterior falla, este bloque se ejecuta
        //    y emitimos un valor por defecto de forma segura.
        emit(emptyList())
    }
}
