package com.example.proyecto_app.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_app.data.AppPreferences
import com.example.proyecto_app.data.getUserByEmail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess = _loginSuccess.asStateFlow()

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError = _loginError.asStateFlow()

    fun onEmailChange(email: String) {
        _email.value = email
    }

    fun onPasswordChange(password: String) {
        _password.value = password
    }

    fun login() {
        viewModelScope.launch {
            // Usamos la lógica correcta que habíamos perdido
            val user = getApplication<Application>().getUserByEmail(_email.value).first()
            if (user != null) {
                val storedPassword = AppPreferences.getPasswordForUser(getApplication(), _email.value)
                if (password.value == storedPassword) {
                    _loginSuccess.value = true
                    _loginError.value = null
                } else {
                    _loginError.value = "Contraseña incorrecta."
                }
            } else {
                _loginError.value = "Usuario no encontrado."
            }
        }
    }
}
