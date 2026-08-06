package com.example.gamevault.domain.model

/**
 * Modelo de dominio puro para un juego en la lista del catálogo.
 * Sin dependencias de Android, Retrofit ni Room.
 */
data class Game(
    val id: Int,
    val name: String,
    val backgroundImage: String?,
    val rating: Double,
    val metacritic: Int?,
    val genres: List<String>,
    val platforms: List<String>,
    val released: String?
)

/**
 * Modelo de dominio puro para el detalle completo de un juego.
 */
data class GameDetail(
    val id: Int,
    val name: String,
    val backgroundImage: String?,
    val rating: Double,
    val metacritic: Int?,
    val genres: List<String>,
    val platforms: List<String>,
    val released: String?,
    val description: String?,
    val developers: List<String>,
    val publishers: List<String>,
    val website: String?
)
