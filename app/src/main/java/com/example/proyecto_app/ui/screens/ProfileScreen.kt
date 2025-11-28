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

// EXPLICACIÓN (Punto H): Esta es una función de extensión para crear un archivo temporal.
// Es necesaria para que la cámara del dispositivo tenga una ubicación donde guardar la foto de forma segura,
// utilizando el FileProvider que configuramos en el AndroidManifest.
fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    return File.createTempFile(imageFileName, ".jpg", externalCacheDir)
}

@Composable
fun ProfileScreen(onLogout: () -> Unit) {
    // EXPLICACIÓN (Punto B y E): Aquí conectamos la UI con el ViewModel.
    // `viewModel()` obtiene la instancia del ProfileViewModel, que contiene toda la lógica.
    // `collectAsState()` observa el StateFlow del ViewModel. Cuando los datos del usuario cambian en el ViewModel,
    // esta pantalla se "recompone" automáticamente para reflejar los nuevos datos. Esto es la UI dinámica.
    val viewModel: ProfileViewModel = viewModel()
    val user by viewModel.user.collectAsState()
    
    val genero by viewModel.genero.collectAsState()
    val edad by viewModel.edad.collectAsState()
    val direccion by viewModel.direccion.collectAsState()
    val telefono by viewModel.telefono.collectAsState()

    val context = LocalContext.current
    var fileUri by remember { mutableStateOf<Uri?>(null) }
    var showImageDialog by remember { mutableStateOf(false) }

    // EXPLICACIÓN (Punto H): Estos son los "lanzadores" de actividades. Son la forma moderna en Compose
    // de pedir permisos o de iniciar una actividad externa (como la cámara o la galería) y recibir un resultado.
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { viewModel.updateProfilePicture(it.toString()) } // Cuando recibimos la URI de la galería, actualizamos el perfil.
    }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) fileUri?.let { viewModel.updateProfilePicture(it.toString()) } // Si la foto se tomó con éxito, usamos la URI que habíamos creado.
    }
    val cameraPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) { // Si el usuario concede el permiso de cámara...
            val newFile = context.createImageFile()
            fileUri = FileProvider.getUriForFile(context, "com.example.proyecto_app.provider", newFile)
            cameraLauncher.launch(fileUri) // ...lanzamos la cámara.
        }
    }
    val galleryPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) { galleryLauncher.launch("image/*") } // Si el usuario concede el permiso de galería, la lanzamos.
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
            CircularProgressIndicator()
        } else {
            Text("Mi Perfil", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = rememberAsyncImagePainter(model = user?.profilePictureUri ?: R.drawable.guau_miau),
                contentDescription = "Foto de perfil",
                modifier = Modifier.size(150.dp).clip(CircleShape).border(2.dp, MaterialTheme.colorScheme.primary, CircleShape).clickable { showImageDialog = true },
                contentScale = ContentScale.Crop
            )
            // ... (Resto de la UI)
        }
    }
}
