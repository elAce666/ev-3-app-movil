package com.example.proyecto_app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyecto_app.data.model.Pet
import com.example.proyecto_app.ui.viewmodels.MascotaViewModel
import com.example.proyecto_app.util.MyDatePickerDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MascotasScreen() {
    val viewModel: MascotaViewModel = viewModel()
    
    // ... (recolectar todos los estados del viewModel)
    val mascotas by viewModel.mascotas.collectAsState()
    val nombreMascota by viewModel.nombreMascota.collectAsState()
    val tipoMascota by viewModel.tipoMascota.collectAsState()
    val fechaEntrada by viewModel.fechaEntrada.collectAsState()
    val fechaSalida by viewModel.fechaSalida.collectAsState()
    val selectedServicios by viewModel.selectedServicios.collectAsState()
    val editingPet by viewModel.editingPet.collectAsState()

    var showDatePickerEntrada by remember { mutableStateOf(false) }
    var showDatePickerSalida by remember { mutableStateOf(false) }

    if (showDatePickerEntrada) { MyDatePickerDialog(onDateSelected = { viewModel.onFechaEntradaChange(it) }, onDismiss = { showDatePickerEntrada = false }) }
    if (showDatePickerSalida) { MyDatePickerDialog(onDateSelected = { viewModel.onFechaSalidaChange(it) }, onDismiss = { showDatePickerSalida = false }) }

    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(if (editingPet == null) "Registrar Estadia" else "Editando Mascota", style = MaterialTheme.typography.titleLarge)
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(value = nombreMascota, onValueChange = viewModel::onNombreMascotaChange, label = { Text("Nombre") }, modifier = Modifier.weight(1f))
                        // Cambia el botón si estamos editando
                        IconButton(onClick = viewModel::saveMascota) {
                            Icon(if (editingPet == null) Icons.Default.Add else Icons.Default.Save, contentDescription = "Guardar Mascota")
                        }
                        if (editingPet != null) {
                            IconButton(onClick = viewModel::clearForm) {
                                Icon(Icons.Default.Cancel, contentDescription = "Cancelar Edición")
                            }
                        }
                    }
                    
                    var expanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                         OutlinedTextField(value = tipoMascota, onValueChange = {}, readOnly = true, label = { Text("Tipo") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }, modifier = Modifier.fillMaxWidth().menuAnchor())
                        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            viewModel.tiposDeMascota.forEach { tipo -> DropdownMenuItem(text = { Text(tipo) }, onClick = { viewModel.onTipoMascotaChange(tipo); expanded = false }) }
                        }
                    }

                    OutlinedTextField(value = fechaEntrada, onValueChange = {}, label = { Text("Fecha Entrada") }, modifier = Modifier.fillMaxWidth().clickable { showDatePickerEntrada = true }, readOnly = true, enabled = false, colors = TextFieldDefaults.colors(disabledTextColor = MaterialTheme.colorScheme.onSurface))
                    OutlinedTextField(value = fechaSalida, onValueChange = {}, label = { Text("Fecha Salida") }, modifier = Modifier.fillMaxWidth().clickable { showDatePickerSalida = true }, readOnly = true, enabled = false, colors = TextFieldDefaults.colors(disabledTextColor = MaterialTheme.colorScheme.onSurface))
                    
                    Text("Servicios Adicionales", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top=8.dp))
                    viewModel.todosLosServicios.forEach { servicio ->
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().clickable { viewModel.onServicioToggle(servicio) }) {
                            Checkbox(checked = servicio in selectedServicios, onCheckedChange = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(servicio)
                        }
                    }
                }
            }
        }

        item {
             Text("Mascotas en Estadia", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(top=16.dp))
             Divider(modifier = Modifier.padding(vertical=8.dp))
        }

        if (mascotas.isEmpty()) {
            item { Text("No hay mascotas registradas.", modifier = Modifier.padding(16.dp)) }
        } else {
            items(mascotas) { mascota ->
                MascotaListItem(
                    mascota = mascota, 
                    onEditClick = { viewModel.loadMascotaForEdit(mascota) }, 
                    onDeleteClick = { viewModel.deleteMascota(mascota) }
                )
            }
        }
    }
}

@Composable
fun MascotaListItem(mascota: Pet, onEditClick: () -> Unit, onDeleteClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(mascota.name, style = MaterialTheme.typography.titleMedium)
                Text("Tipo: ${mascota.type}", style = MaterialTheme.typography.bodySmall)
                mascota.fechaEntrada?.let { Text("Entrada: $it", style = MaterialTheme.typography.bodySmall) }
                mascota.fechaSalida?.let { Text("Salida: $it", style = MaterialTheme.typography.bodySmall) }
                if(mascota.servicios.isNotEmpty()){
                     Text("Servicios: ${mascota.servicios.joinToString()}", style = MaterialTheme.typography.bodySmall)
                }
            }
            Row {
                IconButton(onClick = onEditClick) { Icon(Icons.Default.Edit, contentDescription = "Editar") }
                IconButton(onClick = onDeleteClick) { Icon(Icons.Default.Delete, contentDescription = "Eliminar") }
            }
        }
    }
}
