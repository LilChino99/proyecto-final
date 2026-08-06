package com.example.gamevault.domain.model

/**
 * Modelo de dominio para una reseña local escrita por el usuario.
 */
data class Review(
    val id: Int = 0,
    val gameId: Int,
    val gameName: String,
    val gameImageUrl: String?,
    val reviewText: String,
    val userRating: Float,
    val photoPath: String?,
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * Modelo de dominio para las preferencias del usuario.
 */
data class UserPreferences(
    val isDarkMode: Boolean = false,
    val sortOrder: String = "relevance"
)
