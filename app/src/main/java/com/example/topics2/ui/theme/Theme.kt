package com.example.topics2.ui.themes

import android.os.Build
import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = CustomPrimary,
    secondary = CustomSecondary,
    background = CustomBackground,
    surface = CustomSurface,
    onPrimary = CustomOnPrimary,
    onSecondary = CustomOnSecondary,
    onBackground = CustomOnBackground,
    onSurface = CustomOnSurface,
    error = CustomError,
    onError = CustomOnError,
    outline = CustomOutline,
    inversePrimary = CustomInversePrimary,
    scrim = CustomScrim,
    surfaceVariant = CustomSurfaceVariant,
    onSurfaceVariant = CustomOnSurfaceVariant,
    tertiary = CustomTertiary,
    onTertiary = CustomOnTertiary,
    tertiaryContainer = CustomTertiaryContainer,
    onTertiaryContainer = CustomOnTertiaryContainer,
    surfaceTint = CustomSurfaceTint
)

//private val DarkColorScheme = darkColorScheme(
//    primary = Color(0xFFBB86FC),
//    secondary = Color(0xFF03DAC6),
//    tertiary = Color(0xFFCF6679),
//    background = Color(0xFF121212),
//    surface = Color(0xFF121212),
//    onPrimary = Color.Black,
//    onSecondary = Color.Black,
//    onTertiary = Color.Black,
//    onBackground = Color.White,
//    onSurface = Color.White
//)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6200EE), // Example purple
    secondary = Color(0xFF03DAC6), // Example teal
    background = Color.White,
    surface = Color.LightGray,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,

    //primary = Color.Black,
    //secondary = Color.Black,
    //background = Color.Black,
    //surface = Color.Black,
    //onPrimary = Color.White,
    //onSecondary = Color.Black,
    //onBackground = Color.White,
    //onSurface = Color.White,

)
@Composable
fun TopicsTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
       // typography = Typography,
        content = content
    )
}


//@Composable
//fun TopicsTheme(
//    darkTheme: Boolean = isSystemInDarkTheme(),
//
//    // Dynamic color is available on Android 12+
//    dynamicColor: Boolean = true,
//    content: @Composable () -> Unit
//) {
//    Log.d("TopicsTheme", "darkTheme: $darkTheme")
//    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }
//
//        darkTheme -> DarkColorScheme
//        else -> LightColorScheme
//    }
//
//    MaterialTheme(
//        colorScheme = colorScheme,
//        typography = Typography,
//        content = content
//    )
//}