package com.example.gamevault.ui.screens.review

/**
 * Estado de la UI para la pantalla de crear/editar reseña.
 */
sealed interface CreateReviewUiState {
    data object Idle : CreateReviewUiState
    data object Saving : CreateReviewUiState
    data object Saved : CreateReviewUiState
    data class Error(val message: String) : CreateReviewUiState
}
