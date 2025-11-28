package com.example.proyecto_app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyecto_app.ui.screens.LoginScreen
import com.example.proyecto_app.ui.screens.MainScreen
import com.example.proyecto_app.ui.screens.RegisterScreen
import com.example.proyecto_app.ui.viewmodels.AuthViewModel
import com.example.proyecto_app.ui.viewmodels.LoginViewModel

@Composable
fun AppNavigation() {
    val authViewModel: AuthViewModel = viewModel()
    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()

    if (isAuthenticated) {
        // 1. Le pasamos la función de logout a la MainScreen
        MainScreen(onLogout = { authViewModel.logout() })
    } else {
        AuthFlow(authViewModel = authViewModel)
    }
}

@Composable
private fun AuthFlow(authViewModel: AuthViewModel) {
    val authNavController = rememberNavController()
    NavHost(navController = authNavController, startDestination = AppScreens.LoginScreen.route) {
        composable(AppScreens.LoginScreen.route) {
            val loginViewModel: LoginViewModel = viewModel()
            val loginSuccess by loginViewModel.loginSuccess.collectAsState()

            LaunchedEffect(loginSuccess) {
                if (loginSuccess) {
                    authViewModel.login()
                }
            }
            
            LoginScreen(
                loginViewModel = loginViewModel,
                onLoginSuccess = {},
                onRegisterClick = { authNavController.navigate(AppScreens.RegisterScreen.route) }
            )
        }
        composable(AppScreens.RegisterScreen.route) {
            RegisterScreen(
                onRegisterSuccess = { authNavController.popBackStack() }
            )
        }
    }
}
