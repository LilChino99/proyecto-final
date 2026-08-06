package com.example.gamevault.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamevault.data.mapper.toUiModel
import com.example.gamevault.data.remote.api.RetrofitClient
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla Home (catálogo de videojuegos).
 *
 * En la Semana 2 consume la API pública de RAWG a través de Retrofit.
 * Expone un StateFlow<HomeUiState> para la UI con los 3 estados: Loading, Success y Error.
 */
class HomeViewModel : ViewModel() {

    private val apiService = RetrofitClient.apiService
    private val apiKey = RetrofitClient.RAWG_API_KEY

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadGames()
    }

    /**
     * Carga la lista de videojuegos desde la API de RAWG.
     * Si [query] no está vacío, filtra por nombre de videojuego.
     */
    fun loadGames(query: String? = null) {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            try {
                val response = apiService.getGames(
                    apiKey = apiKey,
                    search = if (query.isNullOrBlank()) null else query,
                    pageSize = 20
                )
                val games = response.results.map { it.toUiModel() }
                _uiState.value = HomeUiState.Success(games)
            } catch (e: Exception) {
                val errorMsg = when {
                    e is java.net.UnknownHostException -> "Sin conexión a Internet. Revisa tu red."
                    e is java.net.SocketTimeoutException -> "La conexión tardó demasiado. Reintenta."
                    else -> e.message ?: "Error al conectar con la API de RAWG"
                }
                _uiState.value = HomeUiState.Error(errorMsg)
            }
        }
    }

    /**
     * Maneja los cambios en la barra de búsqueda con debounce (retardo de 500ms)
     * para evitar saturar la API con llamadas excesivas mientras el usuario escribe.
     */
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500) // Debounce
            loadGames(query)
        }
    }
}
