package com.example.legacyframeapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Color palette for the light theme.
private val LightColorScheme = lightColorScheme(
    primary = PrimaryBrown,
    onPrimary = White,
    primaryContainer = PrimaryContainerBrown,
    onPrimaryContainer = OnPrimaryContainerBrown,
    secondary = SecondaryBrown,
    onSecondary = White,
    secondaryContainer = SecondaryContainerBrown,
    onSecondaryContainer = OnSecondaryContainerBrown,
    tertiary = AccentBrown,
    onTertiary = TextDark,
    tertiaryContainer = AccentContainerBrown,
    onTertiaryContainer = OnAccentContainerBrown,
    background = LightBackground, // light cream
    onBackground = TextDark,
    surface = LightBackground, // unify surface to light coffee cream
    onSurface = TextDark,
    surfaceVariant = Color(0xFFF2E7DB), // slightly darker variant
    outline = DarkBrown,
    error = ErrorRed,
    onError = White,
    inverseSurface = DarkBrown,
    inverseOnSurface = White
)

// Color palette for the dark theme.
private val DarkColorScheme = darkColorScheme(
    primary = DarkBrown,
    onPrimary = White,
    primaryContainer = PrimaryContainerBrown,
    onPrimaryContainer = OnPrimaryContainerBrown,
    secondary = SecondaryBrown,
    onSecondary = White,
    secondaryContainer = SecondaryContainerBrown,
    onSecondaryContainer = OnSecondaryContainerBrown,
    tertiary = AccentBrown,
    onTertiary = White,
    tertiaryContainer = AccentContainerBrown,
    onTertiaryContainer = OnAccentContainerBrown,
    background = Color(0xFF1A140E), // very dark brown background (instead of pure black)
    onBackground = White,
    surface = Color(0xFF241A12), // slightly lighter surface than background
    onSurface = White,
    surfaceVariant = Color(0xFF2E2219), // variant for list items
    outline = DarkOutline,
    error = ErrorRed,
    onError = White,
    inverseSurface = InverseSurface,
    inverseOnSurface = InverseOnSurface
)

/**
 * Main theme function for the app.
 * Allows configuring dark/light theme, dynamic color (Android 12+),
 * a custom accent color, and font scaling.
 */
@Composable
fun UINavegacionTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true, // Enable system dynamic colors on Android 12+
    accentHex: String? = null,    // Optional: override the primary color with a hex value
    fontScale: Float = 1f,        // Optional: scale the size of all fonts
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    // Determine the base color scheme.
    var colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Override the primary color if a valid accentHex is provided.
    accentHex?.let { hex ->
        try {
            val accentColor = Color(android.graphics.Color.parseColor(hex))
            colorScheme = colorScheme.copy(primary = accentColor)
        } catch (e: IllegalArgumentException) {
            // Invalid hex color; do nothing.
        }
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        // Side effect to change system bar colors (status and navigation).
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()

            // Configure system bar icon colors (light or dark).
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !darkTheme
            insetsController.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    // Scale typography based on the 'fontScale' factor.
    val scaledTypography = Typography.copy(
        displayLarge = Typography.displayLarge.copy(fontSize = Typography.displayLarge.fontSize * fontScale),
        displayMedium = Typography.displayMedium.copy(fontSize = Typography.displayMedium.fontSize * fontScale),
        displaySmall = Typography.displaySmall.copy(fontSize = Typography.displaySmall.fontSize * fontScale),
        headlineLarge = Typography.headlineLarge.copy(fontSize = Typography.headlineLarge.fontSize * fontScale),
        headlineMedium = Typography.headlineMedium.copy(fontSize = Typography.headlineMedium.fontSize * fontScale),
        headlineSmall = Typography.headlineSmall.copy(fontSize = Typography.headlineSmall.fontSize * fontScale),
        titleLarge = Typography.titleLarge.copy(fontSize = Typography.titleLarge.fontSize * fontScale),
        titleMedium = Typography.titleMedium.copy(fontSize = Typography.titleMedium.fontSize * fontScale),
        titleSmall = Typography.titleSmall.copy(fontSize = Typography.titleSmall.fontSize * fontScale),
        bodyLarge = Typography.bodyLarge.copy(fontSize = Typography.bodyLarge.fontSize * fontScale),
        bodyMedium = Typography.bodyMedium.copy(fontSize = Typography.bodyMedium.fontSize * fontScale),
        bodySmall = Typography.bodySmall.copy(fontSize = Typography.bodySmall.fontSize * fontScale),
        labelLarge = Typography.labelLarge.copy(fontSize = Typography.labelLarge.fontSize * fontScale),
        labelMedium = Typography.labelMedium.copy(fontSize = Typography.labelMedium.fontSize * fontScale),
        labelSmall = Typography.labelSmall.copy(fontSize = Typography.labelSmall.fontSize * fontScale)
    )

    // Apply the Material 3 theme to the app content.
    MaterialTheme(
        colorScheme = colorScheme,
        typography = scaledTypography,
        content = content
    )
}
