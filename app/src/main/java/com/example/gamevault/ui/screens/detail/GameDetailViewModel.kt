package com.example.gamevault.ui.screens.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.gamevault.GameVaultApplication
import com.example.gamevault.domain.repository.GameRepository
import com.example.gamevault.ui.components.MockGame
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para el detalle de un videojuego.
 *
 * Depende exclusivamente de `GameRepository` de la capa Domain.
 */
class GameDetailViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val gameRepository: GameRepository = (application as GameVaultApplication).gameRepository
    private val gameId: Int = savedStateHandle.get<Int>("gameId") ?: 0

    private val _uiState = MutableStateFlow<GameDetailUiState>(GameDetailUiState.Loading)
    val uiState: StateFlow<GameDetailUiState> = _uiState.asStateFlow()

    init {
        loadGameDetail()
    }

    fun loadGameDetail() {
        viewModelScope.launch {
            _uiState.value = GameDetailUiState.Loading
            try {
                val detail = gameRepository.getGameDetail(gameId)
                val uiModel = MockGame(
                    id = detail.id,
                    name = detail.name,
                    backgroundImage = detail.backgroundImage,
                    rating = detail.rating,
                    metacritic = detail.metacritic,
                    genres = detail.genres,
                    platforms = detail.platforms,
                    released = detail.released,
                    description = detail.description,
                    developers = detail.developers,
                    publishers = detail.publishers,
                    website = detail.website
                )
                _uiState.value = GameDetailUiState.Success(game = uiModel)
            } catch (e: Exception) {
                val errorMsg = when {
                    e is java.net.UnknownHostException -> "Sin conexión a Internet."
                    e is java.net.SocketTimeoutException -> "Tiempo de espera agotado."
                    else -> e.message ?: "Error al obtener el detalle del juego"
                }
                _uiState.value = GameDetailUiState.Error(errorMsg)
            }
        }
    }
}
