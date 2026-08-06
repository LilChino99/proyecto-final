package com.example.gamevault.ui.navigation

/**
 * Sealed class que define todas las rutas de navegación de la app.
 *
 * ¿Por qué una sealed class?
 * - Nos da type-safety: el compilador nos avisa si olvidamos manejar una ruta.
 * - Centraliza todas las rutas en un solo lugar.
 * - Es el patrón estándar usado en CineMatch y en la documentación oficial.
 */
sealed class Screen(val route: String) {

    /** Pantalla principal: catálogo de juegos populares con búsqueda */
    data object Home : Screen("home")

    /** Detalle de un juego específico (recibe el ID del juego como argumento) */
    data object GameDetail : Screen("game_detail/{gameId}") {
        fun createRoute(gameId: Int) = "game_detail/$gameId"
    }

    /** Formulario para crear o editar una reseña */
    data object CreateReview : Screen("create_review/{gameId}/{gameName}/{gameImageUrl}") {
        fun createRoute(gameId: Int, gameName: String, gameImageUrl: String?) =
            "create_review/$gameId/${gameName}/${gameImageUrl ?: "none"}"
    }

    /** Lista de reseñas del usuario guardadas localmente */
    data object MyReviews : Screen("my_reviews")

    /** Pantalla de ajustes (modo oscuro, orden por defecto) */
    data object Settings : Screen("settings")
}
