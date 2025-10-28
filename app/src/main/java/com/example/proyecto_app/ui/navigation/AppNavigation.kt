package com.example.proyecto_app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyecto_app.ui.screens.LoginScreen
import com.example.proyecto_app.ui.screens.MainScreen
import com.example.proyecto_app.ui.screens.RegisterScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                }
            )
        }
        composable("register") {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.popBackStack() // Vuelve a la pantalla de login
                }
            )
        }
        composable("main") {
            // Tu pantalla principal después de iniciar sesión
            MainScreen()
        }
    }
}
