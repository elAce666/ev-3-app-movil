package com.example.proyecto_app.data.repository

import com.example.proyecto_app.data.model.Pet
import com.example.proyecto_app.data.network.ApiProvider
import com.example.proyecto_app.data.network.PetApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PetRepository() {

    // Obtenemos la instancia del servicio desde nuestro ApiProvider centralizado
    private val petApiService: PetApiService = ApiProvider.petService

    fun getAllPets(): Flow<List<Pet>> = flow {
        emit(petApiService.getAllPets())
    }

    fun addPet(pet: Pet): Flow<Pet> = flow {
        emit(petApiService.createPet(pet))
    }
}
