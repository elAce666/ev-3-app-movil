package com.example.proyecto_app.data.network

import com.example.proyecto_app.data.model.Pet
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PetApiService {

    @GET("pets")
    suspend fun getAllPets(): List<Pet>

    @POST("pets")
    suspend fun createPet(@Body pet: Pet): Pet
}
