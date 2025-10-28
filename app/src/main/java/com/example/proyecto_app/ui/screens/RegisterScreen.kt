package com.example.proyecto_app.ui.screens

import android.app.Application
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.proyecto_app.ui.viewmodels.RegisterViewModel
import com.example.proyecto_app.ui.viewmodels.ViewModelFactory


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit
) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val registerViewModel: RegisterViewModel = viewModel(factory = ViewModelFactory(application))

    val fullName by registerViewModel.fullName.collectAsState()
    val email by registerViewModel.email.collectAsState()
    val password by registerViewModel.password.collectAsState()
    val confirmPassword by registerViewModel.confirmPassword.collectAsState()
    val profilePictureUri by registerViewModel.profilePictureUri.collectAsState()
    val pets by registerViewModel.pets.collectAsState()
    val petName by registerViewModel.petName.collectAsState()
    val petType by registerViewModel.petType.collectAsState()
    val petTypes = registerViewModel.petTypes
    val registrationSuccess by registerViewModel.registrationSuccess.collectAsState()

    val fullNameError by registerViewModel.fullNameError.collectAsState()
    val emailError by registerViewModel.emailError.collectAsState()
    val passwordErrors by registerViewModel.passwordErrors.collectAsState()
    val confirmPasswordError by registerViewModel.confirmPasswordError.collectAsState()
    val petError by registerViewModel.petError.collectAsState()

    var showPetTypeMenu by remember { mutableStateOf(false) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        registerViewModel.onProfilePictureChange(uri)
    }

    if (registrationSuccess) {
        LaunchedEffect(Unit) {
            Toast.makeText(context, "¡Registro exitoso!", Toast.LENGTH_SHORT).show()
            onRegisterSuccess()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text("Registro", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
                    .clickable { imagePickerLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = profilePictureUri),
                    contentDescription = "Foto de perfil",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = fullName,
                onValueChange = { registerViewModel.onFullNameChange(it) },
                label = { Text("Nombre Completo") },
                isError = fullNameError != null,
                supportingText = { fullNameError?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email,
                onValueChange = { registerViewModel.onEmailChange(it) },
                label = { Text("Email Institucional") },
                isError = emailError != null,
                supportingText = { emailError?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = password,
                onValueChange = { registerViewModel.onPasswordChange(it) },
                label = { Text("Contraseña") },
                isError = passwordErrors.isNotEmpty(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                supportingText = {
                    Column {
                        passwordErrors.forEach { error -> Text(error, color = MaterialTheme.colorScheme.error) }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { registerViewModel.onConfirmPasswordChange(it) },
                label = { Text("Confirmar Contraseña") },
                isError = confirmPasswordError != null,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                supportingText = { confirmPasswordError?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            Text("Mascotas", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(top = 16.dp))

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = petName,
                    onValueChange = { registerViewModel.onPetNameChange(it) },
                    label = { Text("Nombre Mascota") },
                    modifier = Modifier.weight(1f),
                    isError = petError != null
                )
                IconButton(onClick = { registerViewModel.addPet() }) {
                    Icon(Icons.Default.Add, contentDescription = "Añadir Mascota")
                }
            }

            petError?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            ExposedDropdownMenuBox(
                expanded = showPetTypeMenu,
                onExpandedChange = { showPetTypeMenu = !showPetTypeMenu }
            ) {
                OutlinedTextField(
                    value = petType,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo de Mascota") },
                    trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor() // Importante para que el menú se ancle correctamente
                )
                ExposedDropdownMenu(
                    expanded = showPetTypeMenu,
                    onDismissRequest = { showPetTypeMenu = false }
                ) {
                    petTypes.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type) },
                            onClick = {
                                registerViewModel.onPetTypeChange(type)
                                showPetTypeMenu = false
                            }
                        )
                    }
                }
            }
        }

        items(pets) { pet ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("${pet.name} (${pet.type})")
                IconButton(onClick = { registerViewModel.removePet(pet) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar Mascota")
                }
            }
        }

        item {
            Button(
                onClick = { registerViewModel.register() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Registrarse")
            }
        }
    }
}
