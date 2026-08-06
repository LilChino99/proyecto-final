package com.example.gamevault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.gamevault.data.local.datastore.UserPreferences
import com.example.gamevault.ui.navigation.NavGraph
import com.example.gamevault.ui.theme.GameVaultTheme

/**
 * Actividad principal de GameVault.
 *
 * Solo tiene una responsabilidad: configurar el tema y el punto de entrada
 * de la navegación. Todo lo demás se delega a los Composables y ViewModels.
 *
 * El modo oscuro se controla desde DataStore, no desde el sistema.
 * Esto permite al usuario elegir su preferencia independientemente
 * del tema del sistema operativo.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as GameVaultApplication
        val preferencesFlow = app.userPreferencesDataStore.userPreferencesFlow

        enableEdgeToEdge()
        setContent {
            // Observar las preferencias del usuario para el tema
            val preferences by preferencesFlow.collectAsState(
                initial = UserPreferences()
            )

            GameVaultTheme(darkTheme = preferences.isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph()
                }
            }
        }
    }
}
