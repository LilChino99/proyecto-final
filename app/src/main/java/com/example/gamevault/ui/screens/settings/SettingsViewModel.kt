package com.example.gamevault.ui.screens.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamevault.GameVaultApplication
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de ajustes.
 *
 * Lee y escribe las preferencias del usuario usando DataStore.
 * El Flow de DataStore se convierte en StateFlow para la UI.
 */
class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as GameVaultApplication
    private val preferencesDataStore = app.userPreferencesDataStore

    val uiState: StateFlow<SettingsUiState> = preferencesDataStore.userPreferencesFlow
        .map { prefs -> SettingsUiState(preferences = prefs) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SettingsUiState()
        )

    fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            preferencesDataStore.updateDarkMode(enabled)
        }
    }

    fun updateSortOrder(sortOrder: String) {
        viewModelScope.launch {
            preferencesDataStore.updateSortOrder(sortOrder)
        }
    }
}
