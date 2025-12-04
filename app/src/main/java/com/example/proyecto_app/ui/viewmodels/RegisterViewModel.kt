package com.example.proyecto_app.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_app.data.model.User
import com.example.proyecto_app.data.repository.UserRepository
import com.example.proyecto_app.util.Validator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserRepository(application)

    private val _fullName = MutableStateFlow("")
    val fullName = _fullName.asStateFlow()
    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()
    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()
    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword = _confirmPassword.asStateFlow()

    private val _fullNameError = MutableStateFlow<String?>(null)
    val fullNameError = _fullNameError.asStateFlow()
    private val _emailError = MutableStateFlow<String?>(null)
    val emailError = _emailError.asStateFlow()
    private val _passwordErrors = MutableStateFlow<List<String>>(emptyList())
    val passwordErrors = _passwordErrors.asStateFlow()
    private val _confirmPasswordError = MutableStateFlow<String?>(null)
    val confirmPasswordError = _confirmPasswordError.asStateFlow()

    private val _registrationSuccess = MutableStateFlow(false)
    val registrationSuccess = _registrationSuccess.asStateFlow()

    fun onFullNameChange(name: String) { _fullName.value = name }
    fun onEmailChange(email: String) { _email.value = email }
    fun onPasswordChange(password: String) { _password.value = password }
    fun onConfirmPasswordChange(password: String) { _confirmPassword.value = password }

    fun register() {
        _fullNameError.value = Validator.validateFullName(_fullName.value)
        _emailError.value = Validator.validateEmail(_email.value)
        _passwordErrors.value = Validator.validatePassword(_password.value)
        _confirmPasswordError.value = Validator.confirmPassword(_password.value, _confirmPassword.value)

        val hasErrors = _fullNameError.value != null || _emailError.value != null || _passwordErrors.value.isNotEmpty() || _confirmPasswordError.value != null

        if (!hasErrors) {
            viewModelScope.launch {
                val user = User(fullName = _fullName.value, email = _email.value)
                repository.saveNewUser(user, _password.value)
                _registrationSuccess.value = true
            }
        }
    }
}
