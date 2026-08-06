package com.example.gamevault.data.local.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.gamevault.data.local.entity.ReviewEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object) para las reseñas.
 *
 * Usa Flow para observar cambios en tiempo real (patrón reactivo).
 * Las funciones de escritura son suspend para ejecutarse en corrutinas.
 */
@Dao
interface ReviewDao {

    /**
     * Obtiene todas las reseñas ordenadas por fecha de creación (más recientes primero).
     * Retorna Flow para que la UI se actualice automáticamente cuando hay cambios.
     */
    @Query("SELECT * FROM reviews ORDER BY createdAt DESC")
    fun getAllReviews(): Flow<List<ReviewEntity>>

    /**
     * Obtiene una reseña por su ID.
     * Útil para la pantalla de edición.
     */
    @Query("SELECT * FROM reviews WHERE id = :reviewId")
    fun getReviewById(reviewId: Int): Flow<ReviewEntity?>

    /**
     * Obtiene la reseña de un juego específico (si existe).
     * Un usuario solo puede tener una reseña por juego.
     */
    @Query("SELECT * FROM reviews WHERE gameId = :gameId LIMIT 1")
    fun getReviewByGameId(gameId: Int): Flow<ReviewEntity?>

    /**
     * Verifica si ya existe una reseña para un juego.
     */
    @Query("SELECT EXISTS(SELECT 1 FROM reviews WHERE gameId = :gameId)")
    suspend fun hasReviewForGame(gameId: Int): Boolean

    /**
     * Inserta una nueva reseña. Si hay conflicto, reemplaza la existente.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: ReviewEntity): Long

    /**
     * Actualiza una reseña existente.
     */
    @Update
    suspend fun updateReview(review: ReviewEntity)

    /**
     * Elimina una reseña.
     */
    @Delete
    suspend fun deleteReview(review: ReviewEntity)

    /**
     * Elimina una reseña por su ID.
     */
    @Query("DELETE FROM reviews WHERE id = :reviewId")
    suspend fun deleteReviewById(reviewId: Int)

    /**
     * Cuenta el total de reseñas del usuario.
     */
    @Query("SELECT COUNT(*) FROM reviews")
    fun getReviewCount(): Flow<Int>
}
