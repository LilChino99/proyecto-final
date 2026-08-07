package com.example.gamevault.ui.screens.review

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.gamevault.ui.components.RatingBar
import com.example.gamevault.ui.util.PhotoFileProvider
import com.example.gamevault.ui.util.PhotoFileResult

/**
 * Pantalla para crear/editar una reseña de un videojuego.
 *
 * Incluye:
 * - Selección de calificación (estrellas)
 * - Texto de reseña
 * - Captura de foto con la cámara usando FileProvider + ActivityResultContracts
 * - Solicitud y manejo del permiso Manifest.permission.CAMERA en tiempo de ejecución
 * - Manejo de rechazo de permiso (permite guardar la reseña sin foto)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateReviewScreen(
    onBackClick: () -> Unit,
    onReviewSaved: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateReviewViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val reviewText by viewModel.reviewText.collectAsStateWithLifecycle()
    val userRating by viewModel.userRating.collectAsStateWithLifecycle()
    val photoPath by viewModel.photoPath.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Estado para guardar temporalmente la Uri y ruta de la foto a capturar
    var currentPhotoResult by remember { mutableStateOf<PhotoFileResult?>(null) }
    var showPermissionDeniedDialog by remember { mutableStateOf(false) }

    // Launcher para tomar la foto con la app de cámara
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && currentPhotoResult != null) {
            viewModel.onPhotoTaken(currentPhotoResult!!.absolutePath)
        }
    }

    // Launcher para solicitar el permiso CAMERA en tiempo de ejecución
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Permiso concedido: crear archivo temporal y abrir cámara
            val result = PhotoFileProvider.createPhotoFile(context)
            currentPhotoResult = result
            takePictureLauncher.launch(result.uri)
        } else {
            // Permiso denegado: mostrar diálogo explicativo
            showPermissionDeniedDialog = true
        }
    }

    // Función auxiliar para iniciar la toma de foto comprobando permisos
    fun launchCameraFlow() {
        val permissionCheck = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        )
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            val result = PhotoFileProvider.createPhotoFile(context)
            currentPhotoResult = result
            takePictureLauncher.launch(result.uri)
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // Navegar de vuelta al guardar exitosamente
    LaunchedEffect(uiState) {
        when (uiState) {
            is CreateReviewUiState.Saved -> onReviewSaved()
            is CreateReviewUiState.Error -> {
                snackbarHostState.showSnackbar(
                    (uiState as CreateReviewUiState.Error).message
                )
                viewModel.resetError()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Escribir Reseña") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Info del juego
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = viewModel.gameImageUrl,
                        contentDescription = "Portada del juego",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = viewModel.gameName,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Calificación
            Text(
                text = "Tu calificación",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            RatingBar(
                rating = userRating,
                onRatingChanged = { viewModel.onRatingChanged(it) },
                starSize = 40.dp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Texto de la reseña
            Text(
                text = "Tu reseña personal",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = reviewText,
                onValueChange = { viewModel.onReviewTextChanged(it) },
                placeholder = { Text("Escribe tu opinión sobre este juego...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                shape = RoundedCornerShape(12.dp),
                maxLines = 8
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Sección de foto con la Cámara
            Text(
                text = "Foto de tu juego (Cámara)",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (!photoPath.isNullOrBlank()) {
                // Vista previa de la foto capturada
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    AsyncImage(
                        model = photoPath,
                        contentDescription = "Foto capturada del juego",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    IconButton(
                        onClick = { viewModel.onPhotoTaken("") },
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Eliminar foto",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            OutlinedButton(
                onClick = { launchCameraFlow() },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.CameraAlt,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (photoPath.isNullOrBlank()) "Tomar foto con la Cámara" else "Volver a tomar foto")
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botón guardar
            Button(
                onClick = { viewModel.saveReview() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = uiState !is CreateReviewUiState.Saving,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                if (uiState is CreateReviewUiState.Saving) {
                    Text("Guardando...")
                } else {
                    Icon(
                        imageVector = Icons.Filled.Save,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Guardar Reseña",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Diálogo de manejo de caso en que el usuario rechaza el permiso de cámara
        if (showPermissionDeniedDialog) {
            AlertDialog(
                onDismissRequest = { showPermissionDeniedDialog = false },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Warning,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                },
                title = { Text("Permiso de Cámara Denegado") },
                text = {
                    Text(
                        "Se denegó el permiso para usar la cámara. " +
                                "Aun así puedes guardar tu reseña normalmente sin foto, " +
                                "o intentar nuevamente concediendo el permiso."
                    )
                },
                confirmButton = {
                    TextButton(onClick = { showPermissionDeniedDialog = false }) {
                        Text("Entendido, continuar sin foto")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showPermissionDeniedDialog = false
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }) {
                        Text("Reintentar Permiso")
                    }
                }
            )
        }
    }
}
