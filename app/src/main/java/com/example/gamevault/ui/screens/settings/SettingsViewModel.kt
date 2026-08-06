package com.example.gamevault.ui.screens.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamevault.GameVaultApplication
import com.example.gamevault.data.local.datastore.UserPreferences
import com.example.gamevault.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de Ajustes.
 *
 * Cumple con Clean Architecture: consume la interfaz `UserPreferencesRepository` de la capa Domain.
 */
class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val userPreferencesRepository: UserPreferencesRepository =
        (application as GameVaultApplication).userPreferencesRepository

    val uiState: StateFlow<SettingsUiState> = userPreferencesRepository.userPreferencesFlow
        .map { domainPrefs ->
            SettingsUiState(
                preferences = UserPreferences(
                    isDarkMode = domainPrefs.isDarkMode,
                    sortOrder = domainPrefs.sortOrder
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SettingsUiState()
        )

    fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updateDarkMode(enabled)
        }
    }

    fun updateSortOrder(sortOrder: String) {
        viewModelScope.launch {
            userPreferencesRepository.updateSortOrder(sortOrder)
        }
    }
}
