package com.example.gamevault.data.mapper

import com.example.gamevault.data.local.entity.ReviewEntity
import com.example.gamevault.domain.model.Review

/**
 * Convierte una Entidad Room `ReviewEntity` al modelo de dominio `Review`.
 */
fun ReviewEntity.toDomain(): Review {
    return Review(
        id = id,
        gameId = gameId,
        gameName = gameName,
        gameImageUrl = gameImageUrl,
        reviewText = reviewText,
        userRating = userRating,
        photoPath = photoPath,
        createdAt = createdAt
    )
}

/**
 * Convierte un modelo de dominio `Review` a la Entidad Room `ReviewEntity`.
 */
fun Review.toEntity(): ReviewEntity {
    return ReviewEntity(
        id = id,
        gameId = gameId,
        gameName = gameName,
        gameImageUrl = gameImageUrl,
        reviewText = reviewText,
        userRating = userRating,
        photoPath = photoPath,
        createdAt = createdAt
    )
}
