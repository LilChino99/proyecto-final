package com.example.gamevault

import android.app.Application
import com.example.gamevault.data.local.datastore.UserPreferencesDataStore
import com.example.gamevault.data.local.db.GameVaultDatabase
import com.example.gamevault.data.repository.GameRepositoryImpl
import com.example.gamevault.data.repository.ReviewRepositoryImpl
import com.example.gamevault.data.repository.UserPreferencesRepositoryImpl
import com.example.gamevault.domain.repository.GameRepository
import com.example.gamevault.domain.repository.ReviewRepository
import com.example.gamevault.domain.repository.UserPreferencesRepository

/**
 * Application class de GameVault.
 *
 * Actúa como ServiceLocator centralizado para proveer las instancias de los
 * repositorios de la capa de dominio a los ViewModels.
 */
class GameVaultApplication : Application() {

    /** Base de datos Room */
    val database: GameVaultDatabase by lazy {
        GameVaultDatabase.getDatabase(this)
    }

    /** DataStore de preferencias */
    val userPreferencesDataStore: UserPreferencesDataStore by lazy {
        UserPreferencesDataStore(this)
    }

    /** Repositorio de Juegos (Domain Layer) */
    val gameRepository: GameRepository by lazy {
        GameRepositoryImpl()
    }

    /** Repositorio de Reseñas (Domain Layer) */
    val reviewRepository: ReviewRepository by lazy {
        ReviewRepositoryImpl(database.reviewDao())
    }

    /** Repositorio de Preferencias de Usuario (Domain Layer) */
    val userPreferencesRepository: UserPreferencesRepository by lazy {
        UserPreferencesRepositoryImpl(userPreferencesDataStore)
    }
}
