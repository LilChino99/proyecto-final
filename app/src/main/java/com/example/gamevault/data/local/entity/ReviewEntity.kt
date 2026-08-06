package com.example.gamevault.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad Room para las reseñas del usuario.
 *
 * Cada reseña está vinculada a un juego de la API RAWG (por gameId).
 * La foto (photoPath) es opcional y almacena la ruta local del archivo
 * tomado con la cámara del dispositivo.
 */
@Entity(tableName = "reviews")
data class ReviewEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val gameId: Int,              // ID del juego en RAWG API
    val gameName: String,         // Nombre del juego (cache local para mostrar sin red)
    val gameImageUrl: String?,    // URL de la imagen del juego (cache para mostrar)
    val reviewText: String,       // Texto de la reseña escrita por el usuario
    val userRating: Float,        // Calificación del usuario (1.0 - 5.0 estrellas)
    val photoPath: String?,       // Ruta local de la foto tomada con la cámara (nullable)
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
