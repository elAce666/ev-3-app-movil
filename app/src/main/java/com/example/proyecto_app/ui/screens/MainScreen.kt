package com.example.proyecto_app.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.proyecto_app.ui.navigation.AppScreens
import com.example.proyecto_app.ui.navigation.bottomNavItems
import com.example.proyecto_app.ui.viewmodels.CartViewModel

@Composable
fun MainScreen(onLogout: () -> Unit) {
    val navController = rememberNavController()
    // 1. Creamos el CartViewModel aquí para que se comparta entre las pantallas hijas
    val cartViewModel: CartViewModel = viewModel()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                
                bottomNavItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(requireNotNull(screen.icon()), contentDescription = screen.title) },
                        label = { Text(screen.title!!) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppScreens.ProductsScreen.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // 2. Pasamos el ViewModel compartido a cada pantalla que lo necesita
            composable(AppScreens.ProductsScreen.route) { ProductoScreen(cartViewModel = cartViewModel) }
            composable(AppScreens.MascotasScreen.route) { MascotasScreen() }
            composable(AppScreens.CartScreen.route) { CartScreen(cartViewModel = cartViewModel) }
            composable(AppScreens.ProfileScreen.route) { ProfileScreen(onLogout = onLogout) }
        }
    }
}
