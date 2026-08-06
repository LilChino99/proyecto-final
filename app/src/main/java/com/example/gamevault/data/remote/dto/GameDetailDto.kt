package com.example.gamevault.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO para la respuesta detallada del endpoint /games/{id} de RAWG API.
 */
data class GameDetailDto(
    val id: Int,
    val name: String,
    val slug: String?,
    @SerializedName("description_raw") val descriptionRaw: String?,
    val description: String?,
    @SerializedName("background_image") val backgroundImage: String?,
    @SerializedName("background_image_additional") val backgroundImageAdditional: String?,
    val released: String?,
    val rating: Double,
    val metacritic: Int?,
    val website: String?,
    val genres: List<GenreDto>? = emptyList(),
    val platforms: List<PlatformWrapperDto>? = emptyList(),
    val developers: List<DeveloperDto>? = emptyList(),
    val publishers: List<PublisherDto>? = emptyList(),
    @SerializedName("esrb_rating") val esrbRating: EsrbRatingDto?
)

data class DeveloperDto(
    val id: Int,
    val name: String,
    val slug: String?
)

data class PublisherDto(
    val id: Int,
    val name: String,
    val slug: String?
)

data class EsrbRatingDto(
    val id: Int,
    val name: String,
    val slug: String?
)
