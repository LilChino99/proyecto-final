package com.example.gamevault.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * DataStore para las preferencias del usuario.
 *
 * DataStore es el reemplazo moderno de SharedPreferences.
 * Ventajas sobre SharedPreferences:
 * - Es asíncrono (no bloquea el hilo principal)
 * - Es type-safe con claves tipadas
 * - Usa Flow para observar cambios reactivamente
 * - Garantiza consistencia (transacciones atómicas)
 */

// Extensión que crea una instancia singleton de DataStore vinculada al Context
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "user_preferences"
)

/**
 * Modelo de datos para las preferencias del usuario.
 */
data class UserPreferences(
    val isDarkMode: Boolean = false,
    val sortOrder: String = "relevance" // "relevance", "rating", "released", "name"
)

class UserPreferencesDataStore(private val context: Context) {

    // Claves para cada preferencia
    private object PreferencesKeys {
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        val SORT_ORDER = stringPreferencesKey("sort_order")
    }

    /**
     * Flow que emite las preferencias del usuario cada vez que cambian.
     * La UI observa este Flow para reaccionar a cambios en tiempo real.
     */
    val userPreferencesFlow: Flow<UserPreferences> = context.dataStore.data
        .map { preferences ->
            UserPreferences(
                isDarkMode = preferences[PreferencesKeys.IS_DARK_MODE] ?: false,
                sortOrder = preferences[PreferencesKeys.SORT_ORDER] ?: "relevance"
            )
        }

    /**
     * Actualiza el modo oscuro.
     * Es una función suspend porque la escritura en DataStore es asíncrona.
     */
    suspend fun updateDarkMode(isDarkMode: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_DARK_MODE] = isDarkMode
        }
    }

    /**
     * Actualiza el orden de clasificación por defecto.
     */
    suspend fun updateSortOrder(sortOrder: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SORT_ORDER] = sortOrder
        }
    }
}
