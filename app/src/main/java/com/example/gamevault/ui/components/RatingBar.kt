package com.example.gamevault.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.gamevault.ui.theme.StarEmpty
import com.example.gamevault.ui.theme.StarGold

/**
 * Barra de calificación con estrellas.
 *
 * Puede ser interactiva (para crear reseñas) o de solo lectura (para mostrar ratings).
 *
 * @param rating Calificación actual (0.0 - 5.0)
 * @param onRatingChanged Callback cuando el usuario toca una estrella (null = solo lectura)
 * @param maxStars Número máximo de estrellas
 * @param starSize Tamaño de cada estrella
 */
@Composable
fun RatingBar(
    rating: Float,
    modifier: Modifier = Modifier,
    onRatingChanged: ((Float) -> Unit)? = null,
    maxStars: Int = 5,
    starSize: Dp = 24.dp
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        for (i in 1..maxStars) {
            val icon = when {
                i <= rating -> Icons.Filled.Star
                i - 0.5f <= rating -> Icons.Filled.StarHalf
                else -> Icons.Filled.StarBorder
            }
            val tint = if (i <= rating || i - 0.5f <= rating) StarGold else StarEmpty

            if (onRatingChanged != null) {
                IconButton(
                    onClick = { onRatingChanged(i.toFloat()) },
                    modifier = Modifier.size(starSize)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = "Estrella $i",
                        tint = tint,
                        modifier = Modifier.size(starSize)
                    )
                }
            } else {
                Icon(
                    imageVector = icon,
                    contentDescription = "Estrella $i de $maxStars",
                    tint = tint,
                    modifier = Modifier.size(starSize)
                )
            }
        }
    }
}
