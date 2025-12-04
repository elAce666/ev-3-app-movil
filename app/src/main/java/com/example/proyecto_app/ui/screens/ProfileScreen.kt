package com.example.proyecto_app.ui.screens

import android.Manifest
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.proyecto_app.R
import com.example.proyecto_app.ui.viewmodels.ProfileViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// --- FUNCIÓN RESTAURADA ---
fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    return File.createTempFile(imageFileName, ".jpg", externalCacheDir)
}

@Composable
fun ProfileScreen(onLogout: () -> Unit) {
    val viewModel: ProfileViewModel = viewModel()
    val user by viewModel.user.collectAsState()
    
    val genero by viewModel.genero.collectAsState()
    val edad by viewModel.edad.collectAsState()
    val direccion by viewModel.direccion.collectAsState()
    val telefono by viewModel.telefono.collectAsState()

    val context = LocalContext.current
    var fileUri by remember { mutableStateOf<Uri?>(null) }
    var showImageDialog by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { viewModel.updateProfilePicture(it.toString()) }
    }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) fileUri?.let { viewModel.updateProfilePicture(it.toString()) }
    }
    val cameraPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            val newFile = context.createImageFile()
            fileUri = FileProvider.getUriForFile(context, "com.example.proyecto_app.provider", newFile)
            cameraLauncher.launch(fileUri)
        }
    }
    val galleryPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) galleryLauncher.launch("image/*")
    }

    if (showImageDialog) {
        AlertDialog(
            onDismissRequest = { showImageDialog = false },
            title = { Text("Cambiar Foto de Perfil") },
            confirmButton = { TextButton(onClick = { showImageDialog = false; cameraPermissionLauncher.launch(Manifest.permission.CAMERA) }) { Text("Tomar Foto") } },
            dismissButton = { TextButton(onClick = { showImageDialog = false; galleryPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES) }) { Text("Elegir de Galería") } }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (user == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
                Text("Cargando perfil...")
            }
        } else {
            Text("Mi Perfil", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = rememberAsyncImagePainter(model = user?.profilePictureUri ?: R.drawable.guau_miau),
                contentDescription = "Foto de perfil",
                modifier = Modifier.size(150.dp).clip(CircleShape).border(2.dp, MaterialTheme.colorScheme.primary, CircleShape).clickable { showImageDialog = true },
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(text = user!!.fullName, style = MaterialTheme.typography.titleLarge)
            Text(text = user!!.email, style = MaterialTheme.typography.bodyMedium)
            
            HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp))

            OutlinedTextField(value = genero, onValueChange = viewModel::onGeneroChange, label = { Text("Género") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = edad, onValueChange = viewModel::onEdadChange, label = { Text("Edad") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = direccion, onValueChange = viewModel::onDireccionChange, label = { Text("Dirección") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = telefono, onValueChange = viewModel::onTelefonoChange, label = { Text("Teléfono") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { viewModel.saveProfile() }, modifier = Modifier.fillMaxWidth()) { Text("Guardar Cambios") }

            HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp))
            
            Text("Historial de Compras", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
            if (user!!.historialCompras.isEmpty()) {
                Text("No has realizado compras.")
            } else {
                user!!.historialCompras.forEach { compra ->
                    Card(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Compra del ${compra.fecha}", style = MaterialTheme.typography.titleMedium)
                            Text("Total: $${String.format("%.2f", compra.total)}", style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f, fill = true))

            Button(onClick = onLogout, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
                Text("Cerrar Sesión")
            }
        }
    }
}
