package com.example.proyecto_app.data.repository

import android.content.Context
import com.example.proyecto_app.data.getUserData
import com.example.proyecto_app.data.getUserPassword
import com.example.proyecto_app.data.model.User
import com.example.proyecto_app.data.saveUserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

// EXPLICACIÓN (Punto G): Esta clase es un Repositorio. Su única responsabilidad es manejar los datos del usuario.
// Implementa el patrón de Repositorio, que actúa como una única fuente de verdad para los datos.
class UserRepository(private val context: Context) {

    // EXPLICACIÓN: Esta función expone los datos del usuario como un Flow. Los ViewModels (como ProfileViewModel)
    // observarán este Flow para obtener los datos del usuario.
    fun getUser(): Flow<User?> {
        return context.getUserData()
    }

    // EXPLICACIÓN: Esta función centraliza la lógica para guardar un usuario. Nótese que el ViewModel no sabe
    // que los datos se están guardando en DataStore. Solo le dice al repositorio "guarda este usuario".
    // Esta abstracción (modularización entre capas) nos permitiría cambiar DataStore por Room en el futuro
    // sin tener que modificar ninguna línea de código en los ViewModels.
    suspend fun saveUser(user: User) {
        val currentPassword = context.getUserPassword().first() ?: ""
        context.saveUserData(user, currentPassword)
    }

    suspend fun saveNewUser(user: User, password: String) {
        context.saveUserData(user, password)
    }
}
