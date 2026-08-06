package com.example.gamevault.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamevault.ui.components.MockGame
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla Home (catálogo de videojuegos).
 *
 * En la Semana 1 usa datos mock. En la Semana 2 se conectará a la API RAWG
 * a través de un Repository.
 *
 * Patrón: expone un StateFlow<UiState> que la UI observa.
 * La UI nunca modifica el estado directamente, solo envía eventos al ViewModel.
 */
class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        loadGames()
    }

    fun loadGames() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            try {
                // Semana 1: datos mock. Semana 2: llamada a la API RAWG.
                val games = getMockGames()
                _uiState.value = HomeUiState.Success(games)
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(
                    e.message ?: "Error desconocido al cargar juegos"
                )
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            try {
                val allGames = getMockGames()
                val filtered = if (query.isBlank()) {
                    allGames
                } else {
                    allGames.filter {
                        it.name.contains(query, ignoreCase = true)
                    }
                }
                _uiState.value = HomeUiState.Success(filtered)
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.message ?: "Error al buscar")
            }
        }
    }

    /**
     * Datos mock para la Semana 1.
     * Estos serán reemplazados por llamadas a RAWG API en la Semana 2.
     */
    private fun getMockGames(): List<MockGame> = listOf(
        MockGame(
            id = 3498,
            name = "Grand Theft Auto V",
            backgroundImage = "https://media.rawg.io/media/games/20a/20aa03a10cda45239fe73d0f12f57571.jpg",
            rating = 4.47,
            metacritic = 92,
            genres = listOf("Action", "Adventure"),
            platforms = listOf("PC", "PlayStation 5", "Xbox Series S/X"),
            released = "2013-09-17"
        ),
        MockGame(
            id = 3328,
            name = "The Witcher 3: Wild Hunt",
            backgroundImage = "https://media.rawg.io/media/games/618/618c2031a07bbff6b4f611f10b6f to fix.jpg",
            rating = 4.66,
            metacritic = 92,
            genres = listOf("Action", "RPG"),
            platforms = listOf("PC", "PlayStation 4", "Nintendo Switch"),
            released = "2015-05-18"
        ),
        MockGame(
            id = 4200,
            name = "Portal 2",
            backgroundImage = "https://media.rawg.io/media/games/328/3283617cb7d75d67257fc58339188571.jpg",
            rating = 4.61,
            metacritic = 95,
            genres = listOf("Shooter", "Puzzle"),
            platforms = listOf("PC", "PlayStation 3", "Xbox 360"),
            released = "2011-04-18"
        ),
        MockGame(
            id = 5286,
            name = "Tomb Raider (2013)",
            backgroundImage = "https://media.rawg.io/media/games/021/021c4e21a1824d2526f925eff6324653.jpg",
            rating = 4.05,
            metacritic = 86,
            genres = listOf("Action", "Adventure"),
            platforms = listOf("PC", "PlayStation 4", "Xbox One"),
            released = "2013-03-05"
        ),
        MockGame(
            id = 4291,
            name = "Counter-Strike: Global Offensive",
            backgroundImage = "https://media.rawg.io/media/games/736/73619bd336c894d6941d926bfd563946.jpg",
            rating = 3.57,
            metacritic = 81,
            genres = listOf("Action", "Shooter"),
            platforms = listOf("PC", "Xbox 360", "PlayStation 3"),
            released = "2012-08-21"
        ),
        MockGame(
            id = 13536,
            name = "Portal",
            backgroundImage = "https://media.rawg.io/media/games/7fa/7fa0b586293c5861ee32b6b463cd8494.jpg",
            rating = 4.51,
            metacritic = 90,
            genres = listOf("Shooter", "Puzzle"),
            platforms = listOf("PC", "PlayStation 3", "Xbox 360"),
            released = "2007-10-09"
        ),
        MockGame(
            id = 12020,
            name = "Left 4 Dead 2",
            backgroundImage = "https://media.rawg.io/media/games/d58/d588947d4286e7b5e0e12e1bea7d9844.jpg",
            rating = 4.09,
            metacritic = 89,
            genres = listOf("Action", "Shooter"),
            platforms = listOf("PC", "Xbox 360"),
            released = "2009-11-17"
        ),
        MockGame(
            id = 5679,
            name = "The Elder Scrolls V: Skyrim",
            backgroundImage = "https://media.rawg.io/media/games/7cf/7cfc9220b401b7a300e409e539c9afd5.jpg",
            rating = 4.42,
            metacritic = 94,
            genres = listOf("Action", "RPG"),
            platforms = listOf("PC", "PlayStation 5", "Nintendo Switch"),
            released = "2011-11-11"
        )
    )
}
