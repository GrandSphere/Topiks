package com.example.topics2.ui.components.addTopic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


fun hsvToHex(hsv: FloatArray): String {
    // Convert HSV to RGB
    val rgb = android.graphics.Color.HSVToColor(hsv)

    // Format RGB as a hex string
    return String.format("#%06X", (0xFFFFFF and rgb))
}

fun colorToHsv(color: Color): FloatArray {
    val r = (color.red * 255).toInt()
    val g = (color.green * 255).toInt()
    val b = (color.blue * 255).toInt()

    // Convert RGB to HSV
    val hsv = FloatArray(3)
    android.graphics.Color.RGBToHSV(r, g, b, hsv)

    return hsv
}

fun hexToHsv(hex: String): FloatArray {
    // Remove the leading '#' if present
    val cleanedHex = hex.removePrefix("#")

    // Convert the hex string to an integer color value
    val colorInt = cleanedHex.toIntOrNull(16) ?: throw IllegalArgumentException("Invalid hex color string")

    // Extract RGB components
    val r = (colorInt shr 16) and 0xFF
    val g = (colorInt shr 8) and 0xFF
    val b = colorInt and 0xFF

    // Convert RGB to HSV
    val hsv = FloatArray(3)
    android.graphics.Color.RGBToHSV(r, g, b, hsv)

    return hsv
}


//@OptIn(ExperimentalMaterial3Api::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f, // Default to 0..1 range
    modifier: Modifier = Modifier,
    thumbColor: Color = MaterialTheme.colorScheme.tertiary, // Default thumb color
    activeTrackColor: Color = MaterialTheme.colorScheme.onSecondary, // Default active track color
    inactiveTrackColor: Color = MaterialTheme.colorScheme.secondary, // Default inactive track color
    thumbSize: Dp = 16.dp, // Default thumb size
    trackHeight: Dp = 4.dp // Default track height
) {
    val normalizedValue = (value - valueRange.start) / (valueRange.endInclusive - valueRange.start)

    Slider(
        value = normalizedValue,
        onValueChange = { newNormalizedValue ->
            // De-normalize the value before passing it back
            onValueChange(valueRange.start + newNormalizedValue * (valueRange.endInclusive - valueRange.start))
        },
        valueRange = 0f..1f, // Always use a normalized range
        modifier = modifier,
        colors = SliderDefaults.colors(
            thumbColor = thumbColor
        ),
        thumb = {
            Box(
                Modifier
                    .size(thumbSize)
                    .background(thumbColor, CircleShape)
            )
        },
        track = { sliderPositions ->
            val progressFraction = sliderPositions.value // This is normalized (0f to 1f)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(trackHeight)
            ) {
                // Active (slid) part of the track
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = progressFraction)
                        .height(trackHeight)
                        .background(activeTrackColor)
                        .align(Alignment.CenterStart)
                )
                // Inactive (un-slid) part of the track
                Box(
                    modifier = Modifier
                        .fillMaxWidth(1f - progressFraction)
                        .height(trackHeight)
                        .background(inactiveTrackColor)
                        .align(Alignment.CenterEnd)
                )
            }
        }
    )
}
