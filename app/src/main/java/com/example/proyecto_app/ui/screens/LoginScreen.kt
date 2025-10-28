package com.example.proyecto_app.ui.screens

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyecto_app.ui.viewmodels.LoginViewModel
import com.example.proyecto_app.ui.viewmodels.ViewModelFactory
import com.example.proyecto_app.util.hasLocationPermission
import com.example.proyecto_app.util.rememberPermissionLauncher

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val loginViewModel: LoginViewModel = viewModel(factory = ViewModelFactory(application))

    val email by loginViewModel.email.collectAsState()
    val password by loginViewModel.password.collectAsState()
    val loginSuccess by loginViewModel.loginSuccess.collectAsState()
    val errorMessage by loginViewModel.errorMessage.collectAsState()
    val locationMessage by loginViewModel.locationMessage.collectAsState()

    val permissionLauncher = rememberPermissionLauncher { isGranted ->
        if (isGranted) {
            loginViewModel.onGetLocation()
        } else {
            Toast.makeText(context, "Permiso de ubicación denegado.", Toast.LENGTH_SHORT).show()
        }
    }

    if (loginSuccess) {
        LaunchedEffect(Unit) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Login GUAU&MIAU", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(48.dp))

        // Campo de Correo Electrónico
        OutlinedTextField(
            value = email,
            onValueChange = { loginViewModel.onEmailChange(it) },
            label = { Text("Correo Electrónico") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = errorMessage != null,
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Campo de Contraseña
        OutlinedTextField(
            value = password,
            onValueChange = { loginViewModel.onPasswordChange(it) },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = errorMessage != null,
            singleLine = true
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Botón de Iniciar Sesión
        Button(
            onClick = { loginViewModel.login() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Iniciar Sesión")
        }

        // Botón de Olvidó su contraseña
        TextButton(onClick = { loginViewModel.onForgotPassword() }) {
            Text("¿Olvidó su contraseña?")
        }

        // Botón de Obtener Ubicación
        Button(
            onClick = {
                if (hasLocationPermission(context)) {
                    loginViewModel.onGetLocation()
                } else {
                    permissionLauncher()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Obtener Ubicación de Registro")
        }

        val message = errorMessage ?: locationMessage
        if (message != null) {
            Text(
                text = message,
                color = if (errorMessage != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Botón para ir al Registro
        TextButton(onClick = onNavigateToRegister) {
            Text("¿No tienes cuenta? Regístrate")
        }
    }
}

