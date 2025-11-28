package com.example.proyecto_app.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel : ViewModel() {

    private val _isAuthenticated = MutableStateFlow(false) // Por defecto, el usuario no está autenticado
    val isAuthenticated = _isAuthenticated.asStateFlow()

    fun login() {
        _isAuthenticated.value = true
    }

    fun logout() {
        _isAuthenticated.value = false
    }
}
