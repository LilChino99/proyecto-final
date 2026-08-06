package com.example.gamevault.ui.screens.review

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.gamevault.GameVaultApplication
import com.example.gamevault.data.local.entity.ReviewEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para crear/editar una reseña.
 *
 * Usa AndroidViewModel porque necesita acceso al Application para obtener
 * la instancia de la base de datos (ServiceLocator pattern).
 *
 * Recibe gameId, gameName y gameImageUrl como argumentos de navegación.
 */
class CreateReviewViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val app = application as GameVaultApplication
    private val reviewDao = app.database.reviewDao()

    val gameId: Int = savedStateHandle.get<Int>("gameId") ?: 0
    val gameName: String = savedStateHandle.get<String>("gameName") ?: ""
    val gameImageUrl: String? = savedStateHandle.get<String>("gameImageUrl")?.let {
        if (it == "none") null else it
    }

    private val _uiState = MutableStateFlow<CreateReviewUiState>(CreateReviewUiState.Idle)
    val uiState: StateFlow<CreateReviewUiState> = _uiState.asStateFlow()

    // Campos del formulario como StateFlow
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

    /**
     * Guarda la reseña en Room.
     */
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
                val review = ReviewEntity(
                    gameId = gameId,
                    gameName = gameName,
                    gameImageUrl = gameImageUrl,
                    reviewText = _reviewText.value,
                    userRating = _userRating.value,
                    photoPath = _photoPath.value
                )
                reviewDao.insertReview(review)
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
