package com.example.gamevault.ui.screens.myreviews

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamevault.GameVaultApplication
import com.example.gamevault.data.local.entity.ReviewEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla "Mis Reseñas".
 *
 * Observa la lista de reseñas desde Room usando Flow.
 * Cuando se agrega/elimina una reseña, el Flow emite automáticamente
 * y la UI se actualiza sin necesidad de recargar manualmente.
 */
class MyReviewsViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as GameVaultApplication
    private val reviewDao = app.database.reviewDao()

    /**
     * Estado de la UI derivado del Flow de Room.
     *
     * stateIn convierte el Flow en un StateFlow (hot flow) que:
     * - Comparte la misma suscripción entre múltiples observadores
     * - Mantiene el último valor emitido
     * - Se cancela automáticamente cuando no hay observadores (WhileSubscribed)
     */
    val uiState: StateFlow<MyReviewsUiState> = reviewDao.getAllReviews()
        .map<List<ReviewEntity>, MyReviewsUiState> { reviews ->
            MyReviewsUiState.Success(reviews)
        }
        .catch { e ->
            emit(MyReviewsUiState.Error(e.message ?: "Error al cargar reseñas"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MyReviewsUiState.Loading
        )

    /**
     * Elimina una reseña.
     * El Flow de Room se actualizará automáticamente.
     */
    fun deleteReview(review: ReviewEntity) {
        viewModelScope.launch {
            try {
                reviewDao.deleteReview(review)
                // No necesitamos actualizar el estado manualmente:
                // Room emite un nuevo valor en el Flow automáticamente.
            } catch (e: Exception) {
                // En producción, manejaríamos este error
            }
        }
    }
}
