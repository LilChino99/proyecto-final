package com.example.gamevault.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Respuesta de autenticación OAuth2 de Twitch (Client Credentials Flow).
 */
data class TwitchAuthResponseDto(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("expires_in") val expiresIn: Long,
    @SerializedName("token_type") val tokenType: String
)

/**
 * DTO para la respuesta de videojuegos de la API de IGDB.
 */
data class IgdbGameDto(
    val id: Int,
    val name: String,
    val summary: String?,
    val rating: Double?,
    @SerializedName("total_rating") val totalRating: Double?,
    @SerializedName("first_release_date") val firstReleaseDate: Long?,
    val cover: IgdbImageDto?,
    val genres: List<IgdbNamedItemDto>? = emptyList(),
    val platforms: List<IgdbNamedItemDto>? = emptyList(),
    @SerializedName("involved_companies") val involvedCompanies: List<IgdbInvolvedCompanyDto>? = emptyList()
)

data class IgdbImageDto(
    val id: Int,
    val url: String?
)

data class IgdbNamedItemDto(
    val id: Int,
    val name: String
)

data class IgdbInvolvedCompanyDto(
    val company: IgdbNamedItemDto?
)
