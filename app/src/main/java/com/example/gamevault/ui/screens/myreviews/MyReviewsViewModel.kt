package com.example.gamevault.ui.screens.myreviews

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamevault.GameVaultApplication
import com.example.gamevault.data.mapper.toEntity
import com.example.gamevault.data.local.entity.ReviewEntity
import com.example.gamevault.domain.model.Review
import com.example.gamevault.domain.repository.ReviewRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla "Mis Reseñas".
 *
 * Cumple con Clean Architecture: consume la interfaz `ReviewRepository` de la capa Domain.
 */
class MyReviewsViewModel(application: Application) : AndroidViewModel(application) {

    private val reviewRepository: ReviewRepository = (application as GameVaultApplication).reviewRepository

    val uiState: StateFlow<MyReviewsUiState> = reviewRepository.getAllReviews()
        .map<List<Review>, MyReviewsUiState> { domainReviews ->
            val entities = domainReviews.map { it.toEntity() }
            MyReviewsUiState.Success(entities)
        }
        .catch { e ->
            emit(MyReviewsUiState.Error(e.message ?: "Error al cargar reseñas"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MyReviewsUiState.Loading
        )

    fun deleteReview(reviewEntity: ReviewEntity) {
        viewModelScope.launch {
            try {
                reviewRepository.deleteReviewById(reviewEntity.id)
            } catch (e: Exception) {
                // Manejar error si fuera necesario
            }
        }
    }
}
