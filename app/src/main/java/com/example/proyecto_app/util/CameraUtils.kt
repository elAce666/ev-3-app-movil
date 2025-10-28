package com.example.proyecto_app.util

import android.Manifest
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.FileProvider
import com.example.proyecto_app.BuildConfig
import java.io.File

fun createImageUri(context: Context): Uri {
    val imageFolder = File(context.cacheDir, "images")
    imageFolder.mkdirs()
    val file = File(imageFolder, "profile_picture.jpg")
    return FileProvider.getUriForFile(
        context,
        "${BuildConfig.APPLICATION_ID}.provider",
        file
    )
}

@Composable
fun rememberCameraLauncher(onImageTaken: (Uri) -> Unit): () -> Unit {
    val context = androidx.compose.ui.platform.LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                imageUri?.let { onImageTaken(it) }
            }
        }
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {
            if (it) {
                val newUri = createImageUri(context)
                imageUri = newUri
                cameraLauncher.launch(newUri)
            }
        }
    )

    return { permissionLauncher.launch(Manifest.permission.CAMERA) }
}
