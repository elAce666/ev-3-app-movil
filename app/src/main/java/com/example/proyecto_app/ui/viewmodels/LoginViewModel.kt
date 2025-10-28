package com.example.proyecto_app.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_app.data.UserDataKeys
import com.example.proyecto_app.data.dataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    // --- Estados para los inputs ---
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    // --- Estados para la UI ---
    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess.asStateFlow()

    // --- Estados para los mensajes (ESTOS ERAN LOS QUE FALTABAN) ---
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _locationMessage = MutableStateFlow<String?>(null)
    val locationMessage: StateFlow<String?> = _locationMessage.asStateFlow()

    // --- Funciones que la UI puede llamar ---

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
        _errorMessage.value = null // Limpia el error al escribir
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
        _errorMessage.value = null // Limpia el error al escribir
    }

    fun onForgotPassword() {
        _errorMessage.value = "Funcionalidad 'Olvidó contraseña' no implementada."
    }

    fun onGetLocation() {
        viewModelScope.launch {
            val prefs = getApplication<Application>().dataStore.data.first()
            val location = prefs[UserDataKeys.USER_LOCATION] ?: "Ubicación no encontrada."
            _locationMessage.value = location
            _errorMessage.value = null // Limpia el mensaje de error si había uno
        }
    }

    fun login() {
        viewModelScope.launch {
            val preferences = getApplication<Application>().dataStore.data.first()
            val storedEmail = preferences[UserDataKeys.USER_EMAIL]
            val storedPassword = preferences[UserDataKeys.USER_PASSWORD]

            val inputEmail = _email.value
            val inputPassword = _password.value

            if (inputEmail.isBlank() || inputPassword.isBlank()) {
                _errorMessage.value = "Email y contraseña no pueden estar vacíos."
                return@launch
            }

            if (inputEmail == storedEmail && inputPassword == storedPassword) {
                _loginSuccess.value = true
                _errorMessage.value = null
            } else {
                _errorMessage.value = "Email o contraseña incorrectos."
            }
        }
    }
}
