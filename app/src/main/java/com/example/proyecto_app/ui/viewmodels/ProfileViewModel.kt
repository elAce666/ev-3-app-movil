package com.example.proyecto_app.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_app.data.model.User
import com.example.proyecto_app.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserRepository(application)

    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    private val _genero = MutableStateFlow("")
    val genero = _genero.asStateFlow()
    private val _edad = MutableStateFlow("")
    val edad = _edad.asStateFlow()
    private val _direccion = MutableStateFlow("")
    val direccion = _direccion.asStateFlow()
    private val _telefono = MutableStateFlow("")
    val telefono = _telefono.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            repository.getUser().collect { currentUser ->
                _user.value = currentUser
                currentUser?.let {
                    _genero.value = it.genero ?: ""
                    _edad.value = it.edad?.toString() ?: ""
                    _direccion.value = it.direccion ?: ""
                    _telefono.value = it.telefono ?: ""
                }
            }
        }
    }

    fun onGeneroChange(value: String) { _genero.value = value }
    fun onEdadChange(value: String) { _edad.value = value }
    fun onDireccionChange(value: String) { _direccion.value = value }
    fun onTelefonoChange(value: String) { _telefono.value = value }

    fun saveProfile() {
        viewModelScope.launch {
            val currentUser = _user.value ?: return@launch
            val updatedUser = currentUser.copy(
                genero = _genero.value,
                edad = _edad.value.toIntOrNull(),
                direccion = _direccion.value,
                telefono = _telefono.value
            )
            repository.saveUser(updatedUser)
        }
    }

    fun updateProfilePicture(uri: String) {
        viewModelScope.launch {
            val currentUser = _user.value ?: return@launch
            val updatedUser = currentUser.copy(profilePictureUri = uri)
            repository.saveUser(updatedUser)
        }
    }
}
