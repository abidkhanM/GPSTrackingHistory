package com.example.gpstracking.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.gpstracking.ui.screens.airbnb.AirBnbScreen
import com.example.gpstracking.ui.screens.history.TripHistoryScreen
import com.example.gpstracking.ui.screens.settings.SettingsScreen
import com.example.gpstracking.ui.screens.tracking.TrackingScreen
import kotlinx.serialization.Serializable

@Serializable data object TrackingRoute
@Serializable data object TripHistoryRoute
@Serializable data object SettingsRoute
@Serializable data object AirBnbRoute

private data class TopLevelRoute<T : Any>(
    val route: T,
    val label: String,
    val icon: ImageVector
)

private val topLevelRoutes = listOf(
    TopLevelRoute(TrackingRoute, "Track", Icons.Filled.LocationOn),
    TopLevelRoute(TripHistoryRoute, "History", Icons.Filled.History),
    TopLevelRoute(SettingsRoute, "Settings", Icons.Filled.Settings),
    TopLevelRoute(AirBnbRoute, "AirBnb", Icons.Filled.Home)
)

@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { AppBottomBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = TrackingRoute,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<TrackingRoute> { TrackingScreen() }
            composable<TripHistoryRoute> { TripHistoryScreen() }
            composable<SettingsRoute> { SettingsScreen() }
            composable<AirBnbRoute> { AirBnbScreen() }
        }
    }
}

@Composable
private fun AppBottomBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        topLevelRoutes.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentDestination?.hierarchy?.any { it.hasRoute(item.route::class) } == true,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
