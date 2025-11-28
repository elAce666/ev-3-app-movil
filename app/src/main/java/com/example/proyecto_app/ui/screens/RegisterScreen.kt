package com.example.proyecto_app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyecto_app.ui.viewmodels.RegisterViewModel

@Composable
fun RegisterScreen(onRegisterSuccess: () -> Unit) {
    val viewModel: RegisterViewModel = viewModel()
    
    val fullName by viewModel.fullName.collectAsState()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val confirmPassword by viewModel.confirmPassword.collectAsState()
    
    val fullNameError by viewModel.fullNameError.collectAsState()
    val emailError by viewModel.emailError.collectAsState()
    val passwordErrors by viewModel.passwordErrors.collectAsState()
    val confirmPasswordError by viewModel.confirmPasswordError.collectAsState()
    
    val registrationSuccess by viewModel.registrationSuccess.collectAsState()

    LaunchedEffect(registrationSuccess) {
        if (registrationSuccess) {
            onRegisterSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Crear Cuenta", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(32.dp))

        // ... (Campos de Nombre y Email)
        OutlinedTextField(
            value = fullName,
            onValueChange = { newName -> viewModel.onFullNameChange(newName) },
            label = { Text("Nombre Completo") },
            modifier = Modifier.fillMaxWidth(),
            isError = fullNameError != null,
            singleLine = true
        )
        AnimatedVisibility(visible = fullNameError != null) {
            Text(fullNameError ?: "", color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { newEmail -> viewModel.onEmailChange(newEmail) },
            label = { Text("Correo Electrónico (@duoc.cl)") },
            modifier = Modifier.fillMaxWidth(),
            isError = emailError != null,
            singleLine = true
        )
        AnimatedVisibility(visible = emailError != null) {
            Text(emailError ?: "", color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Campo Contraseña y sus errores
        OutlinedTextField(
            value = password,
            onValueChange = { newPassword -> viewModel.onPasswordChange(newPassword) },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = passwordErrors.isNotEmpty()
        )
        AnimatedVisibility(visible = passwordErrors.isNotEmpty()) {
            Column(modifier = Modifier.padding(start = 16.dp, top = 4.dp)) {
                passwordErrors.forEach { error ->
                    Text(text = "• $error", color = MaterialTheme.colorScheme.error)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // ... (Campo Confirmar Contraseña y Botón)
         OutlinedTextField(
            value = confirmPassword,
            onValueChange = { newConfirm -> viewModel.onConfirmPasswordChange(newConfirm) },
            label = { Text("Confirmar Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = confirmPasswordError != null
        )
        AnimatedVisibility(visible = confirmPasswordError != null) {
            Text(confirmPasswordError ?: "", color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { viewModel.register() },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Registrarse")
        }
    }
}
