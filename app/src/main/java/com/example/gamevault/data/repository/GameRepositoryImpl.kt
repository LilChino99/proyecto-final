package com.example.gamevault.data.repository

import com.example.gamevault.data.mapper.toDomain
import com.example.gamevault.data.remote.api.RawgApiService
import com.example.gamevault.data.remote.api.RetrofitClient
import com.example.gamevault.domain.model.Game
import com.example.gamevault.domain.model.GameDetail
import com.example.gamevault.domain.repository.GameRepository

/**
 * Implementación del Repositorio de Juegos en la capa de Datos (Data Layer).
 *
 * Utiliza `RawgApiService` (Retrofit) para comunicarse con la red.
 */
class GameRepositoryImpl(
    private val apiService: RawgApiService = RetrofitClient.apiService,
    private val apiKey: String = RetrofitClient.RAWG_API_KEY
) : GameRepository {

    override suspend fun getGames(search: String?, page: Int): List<Game> {
        val response = apiService.getGames(
            apiKey = apiKey,
            search = search,
            pageSize = 20,
            page = page
        )
        return response.results.map { it.toDomain() }
    }

    override suspend fun getGameDetail(gameId: Int): GameDetail {
        val detailDto = apiService.getGameDetail(
            gameId = gameId,
            apiKey = apiKey
        )
        return detailDto.toDomain()
    }
}
