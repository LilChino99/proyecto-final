package com.example.gamevault.ui.screens.myreviews

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gamevault.ui.components.ErrorMessage
import com.example.gamevault.ui.components.LoadingIndicator
import com.example.gamevault.ui.components.ReviewCard

/**
 * Pantalla "Mis Reseñas": muestra las reseñas del usuario guardadas en Room.
 *
 * Usa LazyColumn para la lista dinámica (requisito del curso).
 * Se actualiza automáticamente cuando se agregan o eliminan reseñas
 * gracias al Flow de Room.
 */
@Composable
fun MyReviewsScreen(
    onReviewClick: (gameId: Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MyReviewsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Header
        Text(
            text = "📝 Mis Reseñas",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        when (val state = uiState) {
            is MyReviewsUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingIndicator(message = "Cargando reseñas...")
                }
            }

            is MyReviewsUiState.Success -> {
                if (state.reviews.isEmpty()) {
                    // Estado vacío
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Filled.RateReview,
                                contentDescription = null,
                                modifier = Modifier.height(64.dp),
                                tint = MaterialTheme.colorScheme.outline
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "No tienes reseñas aún",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Explora el catálogo y escribe tu primera reseña",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.outline,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = state.reviews,
                            key = { it.id }
                        ) { review ->
                            ReviewCard(
                                review = review,
                                onClick = { onReviewClick(review.gameId) },
                                onDelete = { viewModel.deleteReview(review) }
                            )
                        }
                    }
                }
            }

            is MyReviewsUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    ErrorMessage(message = state.message)
                }
            }
        }
    }
}
