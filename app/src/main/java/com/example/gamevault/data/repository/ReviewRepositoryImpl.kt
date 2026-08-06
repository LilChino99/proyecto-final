package com.example.gamevault.data.repository

import com.example.gamevault.data.local.datastore.UserPreferencesDataStore
import com.example.gamevault.data.local.db.ReviewDao
import com.example.gamevault.data.mapper.toDomain
import com.example.gamevault.data.mapper.toEntity
import com.example.gamevault.domain.model.Review
import com.example.gamevault.domain.model.UserPreferences
import com.example.gamevault.domain.repository.ReviewRepository
import com.example.gamevault.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementación del Repositorio de Reseñas en la capa de Datos.
 *
 * Encapsula el acceso a Room Database (`ReviewDao`).
 */
class ReviewRepositoryImpl(
    private val reviewDao: ReviewDao
) : ReviewRepository {

    override fun getAllReviews(): Flow<List<Review>> {
        return reviewDao.getAllReviews().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getReviewById(reviewId: Int): Flow<Review?> {
        return reviewDao.getReviewById(reviewId).map { entity ->
            entity?.toDomain()
        }
    }

    override fun getReviewByGameId(gameId: Int): Flow<Review?> {
        return reviewDao.getReviewByGameId(gameId).map { entity ->
            entity?.toDomain()
        }
    }

    override suspend fun saveReview(review: Review): Long {
        return reviewDao.insertReview(review.toEntity())
    }

    override suspend fun deleteReview(review: Review) {
        reviewDao.deleteReview(review.toEntity())
    }

    override suspend fun deleteReviewById(reviewId: Int) {
        reviewDao.deleteReviewById(reviewId)
    }
}

/**
 * Implementación del Repositorio de Preferencias de Usuario en la capa de Datos.
 *
 * Encapsula el acceso a DataStore (`UserPreferencesDataStore`).
 */
class UserPreferencesRepositoryImpl(
    private val dataStore: UserPreferencesDataStore
) : UserPreferencesRepository {

    override val userPreferencesFlow: Flow<UserPreferences> = dataStore.userPreferencesFlow.map { prefs ->
        UserPreferences(
            isDarkMode = prefs.isDarkMode,
            sortOrder = prefs.sortOrder
        )
    }

    override suspend fun updateDarkMode(isDarkMode: Boolean) {
        dataStore.updateDarkMode(isDarkMode)
    }

    override suspend fun updateSortOrder(sortOrder: String) {
        dataStore.updateSortOrder(sortOrder)
    }
}
