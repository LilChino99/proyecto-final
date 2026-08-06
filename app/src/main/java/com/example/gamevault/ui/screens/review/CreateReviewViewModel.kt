package com.example.gamevault.ui.screens.review

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.gamevault.GameVaultApplication
import com.example.gamevault.domain.model.Review
import com.example.gamevault.domain.repository.ReviewRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para crear/editar una reseña.
 *
 * Cumple con Clean Architecture: depende exclusivamente de `ReviewRepository`.
 */
class CreateReviewViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val reviewRepository: ReviewRepository = (application as GameVaultApplication).reviewRepository

    val gameId: Int = savedStateHandle.get<Int>("gameId") ?: 0
    val gameName: String = savedStateHandle.get<String>("gameName") ?: ""
    val gameImageUrl: String? = savedStateHandle.get<String>("gameImageUrl")?.let {
        if (it == "none") null else it
    }

    private val _uiState = MutableStateFlow<CreateReviewUiState>(CreateReviewUiState.Idle)
    val uiState: StateFlow<CreateReviewUiState> = _uiState.asStateFlow()

    private val _reviewText = MutableStateFlow("")
    val reviewText: StateFlow<String> = _reviewText.asStateFlow()

    private val _userRating = MutableStateFlow(0f)
    val userRating: StateFlow<Float> = _userRating.asStateFlow()

    private val _photoPath = MutableStateFlow<String?>(null)
    val photoPath: StateFlow<String?> = _photoPath.asStateFlow()

    fun onReviewTextChanged(text: String) {
        _reviewText.value = text
    }

    fun onRatingChanged(rating: Float) {
        _userRating.value = rating
    }

    fun onPhotoTaken(path: String) {
        _photoPath.value = path
    }

    fun saveReview() {
        if (_reviewText.value.isBlank()) {
            _uiState.value = CreateReviewUiState.Error("Escribe algo en tu reseña")
            return
        }
        if (_userRating.value == 0f) {
            _uiState.value = CreateReviewUiState.Error("Selecciona una calificación")
            return
        }

        viewModelScope.launch {
            _uiState.value = CreateReviewUiState.Saving
            try {
                val review = Review(
                    gameId = gameId,
                    gameName = gameName,
                    gameImageUrl = gameImageUrl,
                    reviewText = _reviewText.value,
                    userRating = _userRating.value,
                    photoPath = _photoPath.value
                )
                reviewRepository.saveReview(review)
                _uiState.value = CreateReviewUiState.Saved
            } catch (e: Exception) {
                _uiState.value = CreateReviewUiState.Error(
                    e.message ?: "Error al guardar la reseña"
                )
            }
        }
    }

    fun resetError() {
        _uiState.value = CreateReviewUiState.Idle
    }
}
