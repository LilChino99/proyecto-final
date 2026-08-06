package com.example.gamevault.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Blue60,
    onPrimary = Color.White,
    primaryContainer = Blue20,
    onPrimaryContainer = Blue80,
    secondary = Purple60,
    onSecondary = Color.White,
    secondaryContainer = Purple20,
    onSecondaryContainer = Purple80,
    tertiary = Green60,
    onTertiary = Color.Black,
    tertiaryContainer = Green20,
    onTertiaryContainer = Green80,
    error = Red40,
    onError = Color.White,
    errorContainer = Red20,
    onErrorContainer = Red80,
    background = DarkSurface,
    onBackground = Color(0xFFE6E6EA),
    surface = DarkSurface,
    onSurface = Color(0xFFE6E6EA),
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFC0C0CC),
    surfaceContainer = DarkSurfaceContainer,
    outline = Color(0xFF5A5A6E)
)

private val LightColorScheme = lightColorScheme(
    primary = Blue40,
    onPrimary = Color.White,
    primaryContainer = Blue80,
    onPrimaryContainer = Blue20,
    secondary = Purple40,
    onSecondary = Color.White,
    secondaryContainer = Purple80,
    onSecondaryContainer = Purple20,
    tertiary = Green40,
    onTertiary = Color.White,
    tertiaryContainer = Green80,
    onTertiaryContainer = Green20,
    error = Red40,
    onError = Color.White,
    errorContainer = Red80,
    onErrorContainer = Red20,
    background = LightSurface,
    onBackground = Color(0xFF1A1A2E),
    surface = LightSurface,
    onSurface = Color(0xFF1A1A2E),
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = Color(0xFF44445A),
    outline = Color(0xFF9090A0)
)

/**
 * GameVault theme that supports dark/light mode.
 * The [darkTheme] parameter is controlled by DataStore preferences (not just system default).
 */
@Composable
fun GameVaultTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
