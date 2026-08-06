package com.example.gamevault.ui.screens.detail

import com.example.gamevault.ui.components.MockGame

/**
 * Estado de la UI para la pantalla de detalle de un juego.
 */
sealed interface GameDetailUiState {
    data object Loading : GameDetailUiState
    data class Success(val game: MockGame, val hasReview: Boolean = false) : GameDetailUiState
    data class Error(val message: String) : GameDetailUiState
}
