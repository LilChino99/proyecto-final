package com.example.gamevault.data.mapper

import com.example.gamevault.data.remote.dto.GameDetailDto
import com.example.gamevault.data.remote.dto.GameDto
import com.example.gamevault.ui.components.MockGame

/**
 * Convierte un DTO de juego de la API de RAWG al modelo usado por la UI.
 */
fun GameDto.toUiModel(): MockGame {
    return MockGame(
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
 * Convierte un DTO de detalle de juego de RAWG al modelo usado por la UI.
 */
fun GameDetailDto.toUiModel(): MockGame {
    val descriptionText = when {
        !descriptionRaw.isNullOrBlank() -> descriptionRaw
        !description.isNullOrBlank() -> description.replace(Regex("<[^>]*>"), "")
        else -> "Sin descripción disponible."
    }

    return MockGame(
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
