package com.GrandSphere.Topiks.ui.themes


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    background = DarkCustomBackground,
    onBackground = DarkCustomOnBackground,
    primary = DarkCustomPrimary,
    onPrimary = DarkCustomOnPrimary,
    secondary = DarkCustomSecondary,
    tertiary = DarkCustomTertiary,
    onTertiary = DarkCustomOnTertiary,
    onSecondary = DarkCustomOnSecondary,
    surface = DarkCustomSurface,
    onSurface = DarkCustomOnSurface,
    error = DarkCustomError,
    onError = DarkCustomOnError,
    outline = DarkCustomOutline,
    inversePrimary = DarkCustomInversePrimary,
    scrim = DarkCustomScrim,
    surfaceVariant = DarkCustomSurfaceVariant, // used for highlighting
    onSurfaceVariant = DarkCustomOnSurfaceVariant, //used for highlighting
    tertiaryContainer = DarkCustomTertiaryContainer,
    onTertiaryContainer = DarkCustomOnTertiaryContainer,
    surfaceTint = DarkCustomSurfaceTint,
)


private val LightColorScheme = darkColorScheme(
    background = LightCustomBackground,
    onBackground = LightCustomOnBackground,
    primary = LightCustomPrimary,
    onPrimary = LightCustomOnPrimary,
    secondary = LightCustomSecondary,
    tertiary = LightCustomTertiary,
    onTertiary = LightCustomOnTertiary,
    onSecondary = LightCustomOnSecondary,
    surface = LightCustomSurface,
    onSurface = LightCustomOnSurface,
    error = LightCustomError,
    onError = LightCustomOnError,
    outline = LightCustomOutline,
    inversePrimary = LightCustomInversePrimary,
    scrim = LightCustomScrim,
    surfaceVariant = LightCustomSurfaceVariant, // used for highlighting
    onSurfaceVariant = LightCustomOnSurfaceVariant, //used for highlighting
    tertiaryContainer = LightCustomTertiaryContainer,
    onTertiaryContainer = LightCustomOnTertiaryContainer,
    surfaceTint = LightCustomSurfaceTint,
)

@Composable
fun TopicsTheme(
    theme: Int = 0,
    content: @Composable () -> Unit
) {
    val colorScheme = when{
        theme == 1 -> LightColorScheme
        else -> DarkColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        //typography = Typography,
        content = content
    )
}