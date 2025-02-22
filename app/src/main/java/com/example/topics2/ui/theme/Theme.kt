package com.example.topics2.ui.themes


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    background = CustomBackground,
    onBackground = CustomOnBackground,
    primary = CustomPrimary,
    onPrimary = CustomOnPrimary,
    secondary = CustomSecondary,
    tertiary = CustomTertiary,
    onTertiary = CustomOnTertiary,
    onSecondary = CustomOnSecondary,
    surface = CustomSurface,
    onSurface = CustomOnSurface,
    error = CustomError,
    onError = CustomOnError,
    outline = CustomOutline,
    inversePrimary = CustomInversePrimary,
    scrim = CustomScrim,
    surfaceVariant = CustomSurfaceVariant, // used for highlighting
    onSurfaceVariant = CustomOnSurfaceVariant, //used for highlighting
    tertiaryContainer = CustomTertiaryContainer,
    onTertiaryContainer = CustomOnTertiaryContainer,
    surfaceTint = CustomSurfaceTint,



)


private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6200EE), // Example purple
    secondary = Color(0xFF03DAC6), // Example teal
    background = Color.White,
    surface = Color.LightGray,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
)
@Composable
fun TopicsTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        //typography = Typography,
        content = content
    )
}