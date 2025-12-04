package com.example.proyecto_app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_app.data.model.Pet
import com.example.proyecto_app.data.repository.PetRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class PetViewModel(
    // Hacemos el repositorio y el dispatcher inyectables para facilitar las pruebas
    private val repository: PetRepository = PetRepository(),
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _pets = MutableStateFlow<List<Pet>>(emptyList())
    val pets = _pets.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    init {
        loadPets()
    }

    fun loadPets() {
        viewModelScope.launch(dispatcher) {
            repository.getAllPets()
                .catch { e -> _error.value = "Error al cargar mascotas: ${e.message}" }
                .collect { petList ->
                    _pets.value = petList
                }
        }
    }

    fun addPet(name: String, type: String, fechaEntrada: String, fechaSalida: String, servicios: List<String>) {
        viewModelScope.launch(dispatcher) {
            val newPet = Pet(name = name, type = type, fechaEntrada = fechaEntrada, fechaSalida = fechaSalida, servicios = servicios)
            repository.addPet(newPet)
                .catch { e -> _error.value = "Error al añadir mascota: ${e.message}" }
                .collect { 
                    loadPets()
                }
        }
    }
}
