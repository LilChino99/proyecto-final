package com.example.gamevault.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gamevault.ui.screens.detail.GameDetailScreen
import com.example.gamevault.ui.screens.home.HomeScreen
import com.example.gamevault.ui.screens.myreviews.MyReviewsScreen
import com.example.gamevault.ui.screens.review.CreateReviewScreen
import com.example.gamevault.ui.screens.settings.SettingsScreen
import java.net.URLDecoder
import java.net.URLEncoder

/**
 * Items de la barra de navegación inferior.
 */
data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

val bottomNavItems = listOf(
    BottomNavItem("Catálogo", Icons.Filled.Home, Screen.Home.route),
    BottomNavItem("Mis Reseñas", Icons.Filled.RateReview, Screen.MyReviews.route),
    BottomNavItem("Ajustes", Icons.Filled.Settings, Screen.Settings.route)
)

/**
 * Grafo de navegación principal de la app.
 *
 * Usa Navigation Compose clásica (NavHost + NavController), el mismo
 * patrón que se usó en CineMatch en clase.
 *
 * Incluye:
 * - Bottom Navigation Bar con 3 destinos principales
 * - Navegación interna: Home → Detalle → Crear Reseña
 * - Mis Reseñas → Detalle del juego
 */
@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Mostrar bottom bar solo en las pantallas principales
    val showBottomBar = currentDestination?.route in bottomNavItems.map { it.route }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ) {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.label
                                )
                            },
                            label = { Text(item.label) },
                            selected = currentDestination?.hierarchy?.any {
                                it.route == item.route
                            } == true,
                            onClick = {
                                navController.navigate(item.route) {
                                    // Evitar acumular destinos en el back stack
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            // Pantalla Home (catálogo)
            composable(Screen.Home.route) {
                HomeScreen(
                    onGameClick = { gameId ->
                        navController.navigate(Screen.GameDetail.createRoute(gameId))
                    }
                )
            }

            // Detalle del juego
            composable(
                route = Screen.GameDetail.route,
                arguments = listOf(
                    navArgument("gameId") { type = NavType.IntType }
                )
            ) {
                GameDetailScreen(
                    onBackClick = { navController.popBackStack() },
                    onWriteReviewClick = { gameId, gameName, gameImageUrl ->
                        val encodedName = URLEncoder.encode(gameName, "UTF-8")
                        val encodedUrl = URLEncoder.encode(gameImageUrl ?: "none", "UTF-8")
                        navController.navigate(
                            Screen.CreateReview.createRoute(gameId, encodedName, encodedUrl)
                        )
                    }
                )
            }

            // Crear reseña
            composable(
                route = Screen.CreateReview.route,
                arguments = listOf(
                    navArgument("gameId") { type = NavType.IntType },
                    navArgument("gameName") { type = NavType.StringType },
                    navArgument("gameImageUrl") { type = NavType.StringType }
                )
            ) {
                CreateReviewScreen(
                    onBackClick = { navController.popBackStack() },
                    onReviewSaved = {
                        // Volver a la pantalla anterior después de guardar
                        navController.popBackStack()
                    }
                )
            }

            // Mis Reseñas
            composable(Screen.MyReviews.route) {
                MyReviewsScreen(
                    onReviewClick = { gameId ->
                        navController.navigate(Screen.GameDetail.createRoute(gameId))
                    }
                )
            }

            // Ajustes
            composable(Screen.Settings.route) {
                SettingsScreen()
            }
        }
    }
}
