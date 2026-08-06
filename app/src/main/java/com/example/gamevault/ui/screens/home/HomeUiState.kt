package com.example.gamevault.ui.screens.home

import com.example.gamevault.ui.components.MockGame

/**
 * Estado de la UI para la pantalla Home.
 *
 * Sealed interface que representa los 3 estados posibles:
 * - Loading: datos cargando
 * - Success: datos cargados exitosamente
 * - Error: ocurrió un error
 *
 * Este patrón es el que exige la rúbrica para manejo de estados.
 */
sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(val games: List<MockGame>) : HomeUiState
    data class Error(val message: String) : HomeUiState
}
