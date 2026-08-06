package com.example.gamevault.ui.screens.settings

import com.example.gamevault.data.local.datastore.UserPreferences

/**
 * Estado de la UI para la pantalla de ajustes.
 */
data class SettingsUiState(
    val preferences: UserPreferences = UserPreferences()
)
