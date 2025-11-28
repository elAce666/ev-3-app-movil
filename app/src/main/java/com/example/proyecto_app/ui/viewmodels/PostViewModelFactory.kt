package com.example.proyecto_app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.proyecto_app.data.network.JsonPlaceholderApiClient
import com.example.proyecto_app.data.repository.PostRepository

class PostViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostViewModel::class.java)) {
            // Construye las dependencias necesarias aquí
            val repository = PostRepository(JsonPlaceholderApiClient.instance)
            // Crea y devuelve la instancia del ViewModel
            @Suppress("UNCHECKED_CAST")
            return PostViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
