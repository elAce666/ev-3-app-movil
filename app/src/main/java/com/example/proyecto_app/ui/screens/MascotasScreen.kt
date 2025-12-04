package com.example.proyecto_app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyecto_app.data.model.Pet
import com.example.proyecto_app.ui.viewmodels.PetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MascotasScreen() {
    val viewModel: PetViewModel = viewModel()
    val pets by viewModel.pets.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Text("+", style = MaterialTheme.typography.headlineMedium)
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)) {

            Text("Mis Mascotas Registradas", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(16.dp))

            if (pets.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No tienes mascotas registradas.")
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(pets) { pet ->
                        PetItem(pet = pet)
                    }
                }
            }
        }
    }

    if (showDialog) {
        AddPetDialog(
            onDismiss = { showDialog = false },
            onConfirm = { name, type, entrada, salida, servicios ->
                viewModel.addPet(name, type, entrada, salida, servicios)
                showDialog = false
            }
        )
    }
}

@Composable
fun PetItem(pet: Pet) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = pet.name, style = MaterialTheme.typography.titleLarge)
            Text(text = "Tipo: ${pet.type}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Estadía: ${pet.fechaEntrada} al ${pet.fechaSalida}", style = MaterialTheme.typography.bodyMedium)
            if (pet.servicios.isNotEmpty()) {
                Text(text = "Servicios: ${pet.servicios.joinToString()}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun AddPetDialog(onDismiss: () -> Unit, onConfirm: (String, String, String, String, List<String>) -> Unit) {
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var fechaEntrada by remember { mutableStateOf("") }
    var fechaSalida by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Registrar Nueva Mascota") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") })
                OutlinedTextField(value = type, onValueChange = { type = it }, label = { Text("Tipo (ej. Perro, Gato)") })
                OutlinedTextField(value = fechaEntrada, onValueChange = { fechaEntrada = it }, label = { Text("Fecha Entrada (YYYY-MM-DD)") })
                OutlinedTextField(value = fechaSalida, onValueChange = { fechaSalida = it }, label = { Text("Fecha Salida (YYYY-MM-DD)") })
            }
        },
        confirmButton = {
            Button(onClick = {
                // Para simplificar, los servicios se dejan vacíos. Se podrían añadir checkboxes aquí.
                onConfirm(name, type, fechaEntrada, fechaSalida, emptyList())
            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
