package com.example.gamevault.data.repository

import com.example.gamevault.data.mapper.toDomainGame
import com.example.gamevault.data.mapper.toDomainGameDetail
import com.example.gamevault.data.remote.api.IgdbApiService
import com.example.gamevault.data.remote.api.RetrofitClient
import com.example.gamevault.domain.model.Game
import com.example.gamevault.domain.model.GameDetail
import com.example.gamevault.domain.repository.GameRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * Implementación del Repositorio de Juegos en la capa de Datos (Data Layer) para la API de IGDB v4.
 *
 * Administra el token de autenticación de Twitch OAuth2 y realiza consultas Apicalypse a IGDB.
 */
class GameRepositoryImpl(
    private val apiService: IgdbApiService = RetrofitClient.apiService,
    private val clientId: String = RetrofitClient.TWITCH_CLIENT_ID
) : GameRepository {

    private var cachedAccessToken: String? = null

    /**
     * Obtiene de forma segura el Bearer Token de Twitch OAuth.
     */
    private suspend fun getAuthHeader(): String {
        cachedAccessToken?.let { return "Bearer $it" }

        val clientSecret = RetrofitClient.TWITCH_CLIENT_SECRET
        if (clientSecret.isBlank()) {
            // Si aún no hay un client_secret configurado, arrojamos un mensaje amigable
            throw IllegalStateException("Se requiere el Client Secret de Twitch para la API de IGDB.")
        }

        val authResponse = apiService.getTwitchAccessToken(
            clientId = clientId,
            clientSecret = clientSecret
        )
        cachedAccessToken = authResponse.accessToken
        return "Bearer ${authResponse.accessToken}"
    }

    override suspend fun getGames(search: String?, page: Int): List<Game> {
        val authHeader = getAuthHeader()

        val queryText = if (!search.isNullOrBlank()) {
            """
                search "$search";
                fields name, summary, rating, total_rating, first_release_date, cover.url, genres.name, platforms.name;
                limit 20;
            """.trimIndent()
        } else {
            """
                fields name, summary, rating, total_rating, first_release_date, cover.url, genres.name, platforms.name;
                where rating != null & cover != null;
                sort rating desc;
                limit 20;
            """.trimIndent()
        }

        val body = queryText.toRequestBody("text/plain".toMediaTypeOrNull())
        val dtos = apiService.getGames(
            clientId = clientId,
            authorization = authHeader,
            query = body
        )
        return dtos.map { it.toDomainGame() }
    }

    override suspend fun getGameDetail(gameId: Int): GameDetail {
        val authHeader = getAuthHeader()

        val queryText = """
            fields name, summary, rating, total_rating, first_release_date, cover.url, genres.name, platforms.name, involved_companies.company.name;
            where id = $gameId;
        """.trimIndent()

        val body = queryText.toRequestBody("text/plain".toMediaTypeOrNull())
        val dtos = apiService.getGames(
            clientId = clientId,
            authorization = authHeader,
            query = body
        )

        val detailDto = dtos.firstOrNull()
            ?: throw IllegalArgumentException("No se encontró el videojuego con ID $gameId")

        return detailDto.toDomainGameDetail()
    }
}
