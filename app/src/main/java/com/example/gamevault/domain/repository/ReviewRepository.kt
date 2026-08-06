package com.example.gamevault.domain.repository

import com.example.gamevault.domain.model.Review
import com.example.gamevault.domain.model.UserPreferences
import kotlinx.coroutines.flow.Flow

/**
 * Contrato de interfaz del Repositorio de Reseñas (Domain Layer).
 */
interface ReviewRepository {
    fun getAllReviews(): Flow<List<Review>>
    fun getReviewById(reviewId: Int): Flow<Review?>
    fun getReviewByGameId(gameId: Int): Flow<Review?>
    suspend fun saveReview(review: Review): Long
    suspend fun deleteReview(review: Review)
    suspend fun deleteReviewById(reviewId: Int)
}

/**
 * Contrato de interfaz del Repositorio de Preferencias de Usuario (Domain Layer).
 */
interface UserPreferencesRepository {
    val userPreferencesFlow: Flow<UserPreferences>
    suspend fun updateDarkMode(isDarkMode: Boolean)
    suspend fun updateSortOrder(sortOrder: String)
}
