package com.example.gamevault.ui.screens.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamevault.data.mapper.toUiModel
import com.example.gamevault.data.remote.api.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de detalle de un juego.
 *
 * En la Semana 2 consume el endpoint /games/{id} de Retrofit.
 */
class GameDetailViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val apiService = RetrofitClient.apiService
    private val apiKey = RetrofitClient.RAWG_API_KEY
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
                val detailDto = apiService.getGameDetail(
                    gameId = gameId,
                    apiKey = apiKey
                )
                val game = detailDto.toUiModel()
                _uiState.value = GameDetailUiState.Success(game = game)
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
