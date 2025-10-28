package com.example.proyecto_app.ui.viewmodels

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_app.data.UserDataKeys
import com.example.proyecto_app.data.dataStore
import com.example.proyecto_app.data.model.Pet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import androidx.datastore.preferences.core.edit


class RegisterViewModel(application: Application) : AndroidViewModel(application) {


    private val _fullName = MutableStateFlow("")
    val fullName: StateFlow<String> = _fullName.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword.asStateFlow()

    private val _profilePictureUri = MutableStateFlow<Uri?>(null)
    val profilePictureUri: StateFlow<Uri?> = _profilePictureUri.asStateFlow()

    private val _petName = MutableStateFlow("")
    val petName: StateFlow<String> = _petName.asStateFlow()

    private val _petType = MutableStateFlow("Perro") // Valor por defecto
    val petType: StateFlow<String> = _petType.asStateFlow()

    val petTypes = listOf("Perro", "Gato", "Pájaro", "Otro")

    private val _pets = MutableStateFlow<List<Pet>>(emptyList())
    val pets: StateFlow<List<Pet>> = _pets.asStateFlow()

    private val _registrationSuccess = MutableStateFlow(false)
    val registrationSuccess: StateFlow<Boolean> = _registrationSuccess.asStateFlow()

    private val _fullNameError = MutableStateFlow<String?>(null)
    val fullNameError: StateFlow<String?> = _fullNameError.asStateFlow()

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError.asStateFlow()

    private val _passwordErrors = MutableStateFlow<List<String>>(emptyList())
    val passwordErrors: StateFlow<List<String>> = _passwordErrors.asStateFlow()

    private val _confirmPasswordError = MutableStateFlow<String?>(null)
    val confirmPasswordError: StateFlow<String?> = _confirmPasswordError.asStateFlow()

    private val _petError = MutableStateFlow<String?>(null)
    val petError: StateFlow<String?> = _petError.asStateFlow()

    fun onFullNameChange(newName: String) {
        _fullName.value = newName
        if (newName.isNotBlank()) _fullNameError.value = null
    }

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
        if (newEmail.isNotBlank()) _emailError.value = null
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
        validatePasswordRealtime() // Validar mientras se escribe
    }

    fun onConfirmPasswordChange(newConfirm: String) {
        _confirmPassword.value = newConfirm
        if (newConfirm == _password.value) _confirmPasswordError.value = null
    }

    fun onProfilePictureChange(uri: Uri?) {
        _profilePictureUri.value = uri
    }

    fun onPetNameChange(newName: String) {
        _petName.value = newName
        if (newName.isNotBlank()) _petError.value = null
    }

    fun onPetTypeChange(newType: String) {
        _petType.value = newType
    }

    fun addPet() {
        val name = _petName.value.trim()
        if (name.isBlank()) {
            _petError.value = "El nombre de la mascota no puede estar vacío."
            return
        }
        val newPet = Pet(name = name, type = _petType.value)
        _pets.update { currentPets -> currentPets + newPet }
        // Limpiar campos después de añadir
        _petName.value = ""
        _petError.value = null
    }

    fun removePet(petToRemove: Pet) {
        _pets.update { currentPets -> currentPets.filterNot { it == petToRemove } }
    }

    private fun validatePasswordRealtime() {
        val currentPassword = _password.value
        val errors = mutableListOf<String>()
        if (currentPassword.length < 6) {
            errors.add("Debe tener al menos 6 caracteres.")
        }
        if (!currentPassword.any { it.isDigit() }) {
            errors.add("Debe contener al menos un número.")
        }
        if (!currentPassword.any { it.isUpperCase() }) {
            errors.add("Debe contener al menos una mayúscula.")
        }
        _passwordErrors.value = errors
    }

    fun register() {
        // Validación final al pulsar el botón
        val isFullNameValid = _fullName.value.isNotBlank()
        val isEmailValid = _email.value.endsWith("@duoc.cl", ignoreCase = true)
        val doPasswordsMatch = _password.value == _confirmPassword.value
        validatePasswordRealtime() // Re-valida la contraseña

        _fullNameError.value = if (!isFullNameValid) "El nombre no puede estar vacío" else null
        _emailError.value = if (!isEmailValid) "El email debe ser institucional (@duoc.cl)" else null
        _confirmPasswordError.value = if (!doPasswordsMatch) "Las contraseñas no coinciden" else null

        // Comprobar si hay algún error
        if (isFullNameValid && isEmailValid && doPasswordsMatch && _passwordErrors.value.isEmpty()) {
            // Si todo está bien, procedemos con el registro
            viewModelScope.launch {
                // Usa el Context de la aplicación para guardar en DataStore
                getApplication<Application>().dataStore.edit { preferences ->
                    preferences[UserDataKeys.USER_EMAIL] = _email.value
                    preferences[UserDataKeys.USER_PASSWORD] = _password.value
                    // Aquí podrías guardar más datos si lo necesitas
                }
                _registrationSuccess.value = true
            }
        }
    }
}
