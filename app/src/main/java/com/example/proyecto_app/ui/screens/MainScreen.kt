package com.example.proyecto_app.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
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
import com.example.proyecto_app.ui.viewmodels.WeatherViewModel

@Composable
fun MainScreen(onLogout: () -> Unit) {
    val navController = rememberNavController()
    val cartViewModel: CartViewModel = viewModel()

    // Usamos un Box para superponer el clima sobre el Scaffold
    Box(modifier = Modifier.fillMaxSize()) {
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
                composable(AppScreens.ProductsScreen.route) { ProductoScreen(cartViewModel = cartViewModel) }
                composable(AppScreens.MascotasScreen.route) { MascotasScreen() }
                composable(AppScreens.CartScreen.route) { CartScreen(cartViewModel = cartViewModel) }
                composable(AppScreens.ProfileScreen.route) { ProfileScreen(onLogout = onLogout) }
            }
        }

        // Colocamos el Composable del clima en la esquina superior izquierda del Box
        WeatherInfo(modifier = Modifier.align(Alignment.TopStart).padding(16.dp))
    }
}

// --- LÓGICA DEL CLIMA MOVIDA AQUÍ ---
@Composable
private fun WeatherInfo(modifier: Modifier = Modifier) {
    val weatherViewModel: WeatherViewModel = viewModel()
    val weatherState by weatherViewModel.weatherState.collectAsState()

    weatherState?.let {
        Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = getWeatherIcon(it.currentWeather.weatherCode), contentDescription = "Weather Icon")
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Santiago: ${it.currentWeather.temperature}°C", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

private fun getWeatherIcon(code: Int): ImageVector {
    return when (code) {
        0 -> Icons.Default.WbSunny
        1, 2, 3 -> Icons.Default.Cloud
        else -> Icons.Default.Thermostat
    }
}
