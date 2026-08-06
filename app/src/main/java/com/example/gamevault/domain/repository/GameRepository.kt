package com.example.gamevault.domain.repository

import com.example.gamevault.domain.model.Game
import com.example.gamevault.domain.model.GameDetail

/**
 * Contrato de interfaz del Repositorio de Juegos (Domain Layer).
 *
 * Desacopla la fuente de datos (Retrofit, Room, etc.) de la capa de presentación.
 */
interface GameRepository {
    suspend fun getGames(search: String? = null, page: Int = 1): List<Game>
    suspend fun getGameDetail(gameId: Int): GameDetail
}
