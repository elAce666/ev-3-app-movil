package com.example.proyecto_app.data.repository

import android.content.Context
import com.example.proyecto_app.data.getUserData
import com.example.proyecto_app.data.getUserPassword
import com.example.proyecto_app.data.model.Pet
import com.example.proyecto_app.data.model.User
import com.example.proyecto_app.data.saveUserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class UserRepository(private val context: Context) {

    fun getUser(): Flow<User?> {
        return context.getUserData()
    }

    suspend fun saveUser(user: User) {
        val currentPassword = context.getUserPassword().first() ?: ""
        context.saveUserData(user, currentPassword)
    }

    suspend fun saveNewUser(user: User, password: String) {
        context.saveUserData(user, password)
    }

    // --- FUNCIÓN AÑADIDA ---
    suspend fun addPetToCurrentUser(pet: Pet) {
        // 1. Obtenemos el usuario actual de DataStore.
        val currentUser = getUser().first()
        if (currentUser != null) {
            // 2. Creamos una nueva lista de mascotas, añadiendo la nueva.
            val updatedPets = currentUser.pets.toMutableList().apply {
                add(pet)
            }
            // 3. Creamos una copia del usuario con la lista de mascotas actualizada.
            val updatedUser = currentUser.copy(pets = updatedPets)
            // 4. Guardamos el usuario actualizado en DataStore.
            saveUser(updatedUser)
        }
    }
}
