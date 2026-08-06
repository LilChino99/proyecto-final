package com.example.gamevault

import android.app.Application
import com.example.gamevault.data.local.datastore.UserPreferencesDataStore
import com.example.gamevault.data.local.db.GameVaultDatabase

/**
 * Application class de GameVault.
 *
 * Actúa como un ServiceLocator manual (sin Hilt/Dagger).
 * Inicializa las dependencias que necesitan Context y las expone
 * como singletons a través de lazy initialization.
 *
 * ¿Por qué no usamos Hilt?
 * - El curso no cubre inyección de dependencias formal.
 * - Para la escala de esta app, un ServiceLocator manual es suficiente.
 * - En la sustentación puedes explicar que conoces Hilt pero elegiste
 *   mantener la complejidad manejable.
 */
class GameVaultApplication : Application() {

    /** Base de datos Room - inicialización lazy (se crea al primer acceso) */
    val database: GameVaultDatabase by lazy {
        GameVaultDatabase.getDatabase(this)
    }

    /** DataStore de preferencias del usuario */
    val userPreferencesDataStore: UserPreferencesDataStore by lazy {
        UserPreferencesDataStore(this)
    }
}
