package com.example.proyecto_app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

sealed class AppScreens(
    val route: String, 
    val title: String? = null, 
    val icon: @Composable () -> ImageVector? = { null }
) {
    // Flujo de Autenticación
    object LoginScreen : AppScreens("login_screen")
    object RegisterScreen : AppScreens("register_screen")

    // Flujo Principal (con Bottom Navigation)
    object ProductsScreen : AppScreens("products_screen", "Artículos", { Icons.Default.Home })
    object MascotasScreen : AppScreens("mascotas_screen", "Mascotas", { Icons.AutoMirrored.Filled.List })
    object CartScreen : AppScreens("cart_screen", "Carrito", { Icons.Default.ShoppingCart })
    object ProfileScreen : AppScreens("profile_screen", "Mi Perfil", { Icons.Default.Face })
}

val bottomNavItems = listOf(
    AppScreens.ProductsScreen,
    AppScreens.MascotasScreen,
    AppScreens.CartScreen,
    AppScreens.ProfileScreen
)
