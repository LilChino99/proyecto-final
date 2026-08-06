package com.example.gamevault.ui.screens.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamevault.GameVaultApplication
import com.example.gamevault.domain.repository.GameRepository
import com.example.gamevault.ui.components.MockGame
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla Home (catálogo).
 *
 * Cumple con Clean Architecture: solo interactúa con `GameRepository` de la capa de dominio.
 * No sabe de dónde vienen los datos (Retrofit, DB, etc.).
 */
class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val gameRepository: GameRepository = (application as GameVaultApplication).gameRepository

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadGames()
    }

    fun loadGames(query: String? = null) {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            try {
                val domainGames = gameRepository.getGames(
                    search = if (query.isNullOrBlank()) null else query
                )
                // Mapear modelo de dominio a modelo de presentación UI
                val uiGames = domainGames.map { domainGame ->
                    MockGame(
                        id = domainGame.id,
                        name = domainGame.name,
                        backgroundImage = domainGame.backgroundImage,
                        rating = domainGame.rating,
                        metacritic = domainGame.metacritic,
                        genres = domainGame.genres,
                        platforms = domainGame.platforms,
                        released = domainGame.released
                    )
                }
                _uiState.value = HomeUiState.Success(uiGames)
            } catch (e: Exception) {
                val errorMsg = when {
                    e is java.net.UnknownHostException -> "Sin conexión a Internet. Revisa tu red."
                    e is java.net.SocketTimeoutException -> "La conexión tardó demasiado. Reintenta."
                    else -> e.message ?: "Error al obtener los videojuegos"
                }
                _uiState.value = HomeUiState.Error(errorMsg)
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            loadGames(query)
        }
    }
}
