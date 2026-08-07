package com.example.gamevault.data.mapper

import com.example.gamevault.data.remote.dto.IgdbGameDto
import com.example.gamevault.domain.model.Game
import com.example.gamevault.domain.model.GameDetail
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Convierte un DTO de IGDB v4 al modelo de dominio `Game`.
 */
fun IgdbGameDto.toDomainGame(): Game {
    return Game(
        id = id,
        name = name,
        backgroundImage = formatIgdbImageUrl(cover?.url),
        rating = (rating ?: totalRating ?: 0.0) / 20.0, // Convertir rating 0-100 a escala 0-5
        metacritic = rating?.toInt(),
        genres = genres?.map { it.name } ?: emptyList(),
        platforms = platforms?.map { it.name } ?: emptyList(),
        released = formatUnixTimestamp(firstReleaseDate)
    )
}

/**
 * Convierte un DTO de IGDB v4 al modelo de dominio `GameDetail`.
 */
fun IgdbGameDto.toDomainGameDetail(): GameDetail {
    val developersList = involvedCompanies
        ?.mapNotNull { it.company?.name }
        ?: emptyList()

    return GameDetail(
        id = id,
        name = name,
        backgroundImage = formatIgdbImageUrl(cover?.url, size = "t_1080p"),
        rating = (rating ?: totalRating ?: 0.0) / 20.0,
        metacritic = rating?.toInt(),
        genres = genres?.map { it.name } ?: emptyList(),
        platforms = platforms?.map { it.name } ?: emptyList(),
        released = formatUnixTimestamp(firstReleaseDate),
        description = summary ?: "Sin descripción disponible.",
        developers = developersList,
        publishers = emptyList(),
        website = null
    )
}

/**
 * Formatea las URLs relativas de imágenes de IGDB a HTTPS de alta resolución.
 */
private fun formatIgdbImageUrl(rawUrl: String?, size: String = "t_cover_big"): String? {
    if (rawUrl.isNullOrBlank()) return null
    val fullUrl = if (rawUrl.startsWith("//")) "https:$rawUrl" else rawUrl
    return fullUrl.replace("t_thumb", size)
}

/**
 * Formatea un timestamp Unix de IGDB en segundos a una fecha legible YYYY-MM-DD.
 */
private fun formatUnixTimestamp(timestamp: Long?): String? {
    if (timestamp == null || timestamp <= 0) return null
    val date = Date(timestamp * 1000L)
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sdf.format(date)
}
