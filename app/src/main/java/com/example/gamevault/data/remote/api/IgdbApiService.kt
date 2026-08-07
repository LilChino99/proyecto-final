package com.example.gamevault.data.remote.api

import com.example.gamevault.data.remote.dto.IgdbGameDto
import com.example.gamevault.data.remote.dto.TwitchAuthResponseDto
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Interfaz de Retrofit para interactuar con Twitch OAuth y la API de IGDB v4.
 */
interface IgdbApiService {

    /**
     * Obtiene un token de acceso OAuth2 desde Twitch mediante Client Credentials Grant.
     */
    @POST("https://id.twitch.tv/oauth2/token")
    suspend fun getTwitchAccessToken(
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String,
        @Query("grant_type") grantType: String = "client_credentials"
    ): TwitchAuthResponseDto

    /**
     * Consulta videojuegos en la API de IGDB v4 usando el lenguaje Apicalypse en el cuerpo de la petición.
     */
    @Headers("Accept: application/json")
    @POST("https://api.igdb.com/v4/games")
    suspend fun getGames(
        @Header("Client-ID") clientId: String,
        @Header("Authorization") authorization: String,
        @Body query: RequestBody
    ): List<IgdbGameDto>
}
