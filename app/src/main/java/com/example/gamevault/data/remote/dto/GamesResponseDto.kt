package com.example.gamevault.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO para la respuesta paginada del endpoint /games de RAWG API.
 */
data class GamesResponseDto(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<GameDto>
)

/**
 * DTO para un videojuego en la lista del catálogo.
 */
data class GameDto(
    val id: Int,
    val name: String,
    val slug: String,
    @SerializedName("background_image") val backgroundImage: String?,
    val released: String?,
    val rating: Double,
    @SerializedName("rating_top") val ratingTop: Int?,
    val metacritic: Int?,
    val genres: List<GenreDto>? = emptyList(),
    val platforms: List<PlatformWrapperDto>? = emptyList()
)

/**
 * DTO para género de videojuego.
 */
data class GenreDto(
    val id: Int,
    val name: String,
    val slug: String?
)

/**
 * DTO envoltorio de plataforma en la API RAWG.
 */
data class PlatformWrapperDto(
    val platform: PlatformDto
)

/**
 * DTO para la información de una plataforma.
 */
data class PlatformDto(
    val id: Int,
    val name: String,
    val slug: String?
)
