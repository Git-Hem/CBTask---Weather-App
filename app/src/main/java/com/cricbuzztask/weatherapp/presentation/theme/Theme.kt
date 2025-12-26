package com.cricbuzztask.weatherapp.presentation.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = BackgroundWhite,
    primaryContainer = PrimaryLight,
    onPrimaryContainer = TextPrimary,
    secondary = ClearSkyBlue,
    onSecondary = BackgroundWhite,
    secondaryContainer = RainyBlue,
    onSecondaryContainer = TextPrimary,
    tertiary = SunnyYellow,
    onTertiary = TextPrimary,
    error = ErrorRed,
    onError = BackgroundWhite,
    background = BackgroundLight,
    onBackground = TextPrimary,
    surface = BackgroundWhite,
    onSurface = TextPrimary,
    surfaceVariant = BackgroundGray,
    onSurfaceVariant = TextSecondary,
    outline = DividerColor
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryLight,
    onPrimary = TextPrimary,
    primaryContainer = PrimaryDark,
    onPrimaryContainer = BackgroundWhite,
    secondary = ClearSkyBlue,
    onSecondary = TextPrimary,
    secondaryContainer = RainyBlue,
    onSecondaryContainer = BackgroundWhite,
    tertiary = SunnyYellow,
    onTertiary = TextPrimary,
    error = ErrorRed,
    onError = BackgroundWhite,
    background = TextPrimary,
    onBackground = BackgroundWhite,
    surface = TextPrimary,
    onSurface = BackgroundWhite,
    surfaceVariant = TextSecondary,
    onSurfaceVariant = BackgroundGray,
    outline = DividerColor
)

@Composable
fun WeatherForecastTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}