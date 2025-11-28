package com.example.proyecto_app.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.proyecto_app.data.model.Pet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MascotaViewModel : ViewModel() {

    private val _mascotas = MutableStateFlow<List<Pet>>(emptyList())
    val mascotas = _mascotas.asStateFlow()

    // --- Formulario ---
    private val _nombreMascota = MutableStateFlow("")
    val nombreMascota = _nombreMascota.asStateFlow()
    private val _tipoMascota = MutableStateFlow("Perro")
    val tipoMascota = _tipoMascota.asStateFlow()
    private val _fechaEntrada = MutableStateFlow("")
    val fechaEntrada = _fechaEntrada.asStateFlow()
    private val _fechaSalida = MutableStateFlow("")
    val fechaSalida = _fechaSalida.asStateFlow()
    private val _selectedServicios = MutableStateFlow<Set<String>>(emptySet())
    val selectedServicios = _selectedServicios.asStateFlow()

    // --- Modo Edición ---
    private val _editingPet = MutableStateFlow<Pet?>(null)
    val editingPet = _editingPet.asStateFlow()

    val tiposDeMascota = listOf("Perro", "Gato", "Ave", "Otro")
    val todosLosServicios = listOf("Cuidado del animal", "Peluquería", "Control veterinario básico")

    fun onNombreMascotaChange(nombre: String) { _nombreMascota.value = nombre }
    fun onTipoMascotaChange(tipo: String) { _tipoMascota.value = tipo }
    fun onFechaEntradaChange(fecha: String) { _fechaEntrada.value = fecha }
    fun onFechaSalidaChange(fecha: String) { _fechaSalida.value = fecha }
    fun onServicioToggle(servicio: String) { _selectedServicios.update { if (servicio in it) it - servicio else it + servicio } }

    fun saveMascota() {
        if (_nombreMascota.value.isBlank()) return

        val mascotaToSave = Pet(
            name = _nombreMascota.value,
            type = _tipoMascota.value,
            fechaEntrada = _fechaEntrada.value.ifEmpty { null },
            fechaSalida = _fechaSalida.value.ifEmpty { null },
            servicios = _selectedServicios.value.toList()
        )

        if (_editingPet.value == null) {
            // Añadir nueva mascota
            _mascotas.value = _mascotas.value + mascotaToSave
        } else {
            // Actualizar mascota existente
            _mascotas.update { list ->
                list.map { if (it == _editingPet.value) mascotaToSave else it }
            }
        }
        clearForm()
    }

    fun deleteMascota(mascota: Pet) {
        _mascotas.update { it - mascota }
    }

    fun loadMascotaForEdit(mascota: Pet) {
        _editingPet.value = mascota
        _nombreMascota.value = mascota.name
        _tipoMascota.value = mascota.type
        _fechaEntrada.value = mascota.fechaEntrada ?: ""
        _fechaSalida.value = mascota.fechaSalida ?: ""
        _selectedServicios.value = mascota.servicios.toSet()
    }

    fun clearForm() {
        _editingPet.value = null
        _nombreMascota.value = ""
        _tipoMascota.value = "Perro"
        _fechaEntrada.value = ""
        _fechaSalida.value = ""
        _selectedServicios.value = emptySet()
    }
}
