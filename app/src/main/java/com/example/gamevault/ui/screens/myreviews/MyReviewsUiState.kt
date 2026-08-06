package com.example.gamevault.ui.screens.myreviews

import com.example.gamevault.data.local.entity.ReviewEntity

/**
 * Estado de la UI para la pantalla de "Mis Reseñas".
 */
sealed interface MyReviewsUiState {
    data object Loading : MyReviewsUiState
    data class Success(val reviews: List<ReviewEntity>) : MyReviewsUiState
    data class Error(val message: String) : MyReviewsUiState
}
