package com.example.proyecto_app.data.network

import com.example.proyecto_app.data.model.remote.Post
import retrofit2.http.GET

interface JsonPlaceholderApi {
    @GET("posts")
    suspend fun getPosts(): List<Post>
}
