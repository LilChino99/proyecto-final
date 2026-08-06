package com.example.gamevault.data.mapper

import com.example.gamevault.data.remote.dto.GameDetailDto
import com.example.gamevault.data.remote.dto.GameDto
import com.example.gamevault.domain.model.Game
import com.example.gamevault.domain.model.GameDetail

/**
 * Convierte un DTO de juego de RAWG al modelo de dominio puros `Game`.
 */
fun GameDto.toDomain(): Game {
    return Game(
        id = id,
        name = name,
        backgroundImage = backgroundImage,
        rating = rating,
        metacritic = metacritic,
        genres = genres?.map { it.name } ?: emptyList(),
        platforms = platforms?.map { it.platform.name } ?: emptyList(),
        released = released
    )
}

/**
 * Convierte un DTO de detalle de juego al modelo de dominio `GameDetail`.
 */
fun GameDetailDto.toDomain(): GameDetail {
    val descriptionText = when {
        !descriptionRaw.isNullOrBlank() -> descriptionRaw
        !description.isNullOrBlank() -> description.replace(Regex("<[^>]*>"), "")
        else -> "Sin descripción disponible."
    }

    return GameDetail(
        id = id,
        name = name,
        backgroundImage = backgroundImage,
        rating = rating,
        metacritic = metacritic,
        genres = genres?.map { it.name } ?: emptyList(),
        platforms = platforms?.map { it.platform.name } ?: emptyList(),
        released = released,
        description = descriptionText,
        developers = developers?.map { it.name } ?: emptyList(),
        publishers = publishers?.map { it.name } ?: emptyList(),
        website = website
    )
}
