package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = SkyPrimary,
    onPrimary = CleanWhite,
    primaryContainer = SkyLight,
    onPrimaryContainer = SkyPrimaryDeep,
    secondary = SkyPrimaryDark,
    onSecondary = CleanWhite,
    secondaryContainer = SkySoft,
    onSecondaryContainer = SkyPrimaryDeep,
    tertiary = MintSuccess,
    onTertiary = CleanWhite,
    tertiaryContainer = MintSuccessLight,
    onTertiaryContainer = MintSuccess,
    background = SkyUltraLight,
    onBackground = TextPrimary,
    surface = CleanWhite,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceLight,
    onSurfaceVariant = TextSecondary,
    outline = CardBorder
)

private val DarkColorScheme = darkColorScheme(
    primary = SkySoft,
    onPrimary = SkyPrimaryDeep,
    primaryContainer = SkyPrimaryDark,
    onPrimaryContainer = SkyLight,
    secondary = BubbleCyan,
    onSecondary = TextPrimary,
    background = Color(0xFF0C1929),
    onBackground = Color(0xFFF1F5F9),
    surface = Color(0xFF132338),
    onSurface = Color(0xFFF1F5F9),
    surfaceVariant = Color(0xFF1E324D),
    onSurfaceVariant = Color(0xFFCBD5E1),
    outline = Color(0xFF334E68)
)

@Composable
fun FreshFoldTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = colorScheme.background.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
