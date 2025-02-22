package com.example.topics2.ui.components.addTopic

import android.content.ClipboardManager
import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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

fun colorToArgb(color: Color): Int {
    val alpha = (color.alpha * 255).toInt() and 0xFF
    val red = (color.red * 255).toInt() and 0xFF
    val green = (color.green * 255).toInt() and 0xFF
    val blue = (color.blue * 255).toInt() and 0xFF

    // Combine into ARGB format
    return (alpha shl 24) or (red shl 16) or (green shl 8) or blue
}
fun argbToColor(argb: Int): Color {
    val alpha = ((argb shr 24) and 0xFF) / 255f
    val red = ((argb shr 16) and 0xFF) / 255f
    val green = ((argb shr 8) and 0xFF) / 255f
    val blue = (argb and 0xFF) / 255f
    return Color(red, green, blue, alpha)
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

// Convert Color to Hex (ARGB)
fun colorToHex(color: Color): String {
    val red = (color.red * 255).toInt()
    val green = (color.green * 255).toInt()
    val blue = (color.blue * 255).toInt()

    return String.format("#%02X%02X%02X", red, green, blue)
}


// Convert Hex to Color (RGB), with input sanitization
fun hexToColor(hex: String): Color {
    // Step 1: Remove '#' if it exists
    // Step 2: Filter out any characters that are not valid hex digits
    val cleanHex = hex.removePrefix("#").filter { it.isDigit() || it in 'A'..'F' || it in 'a'..'f' }

    // Step 3: Take the first 6 characters
    // Step 4: If the string is shorter than 6 characters, pad with '0's
    val sanitizedHex = cleanHex.take(6).padEnd(6, '0')

    // Step 5: Convert the hex string to RGB values
    val red = Integer.valueOf(sanitizedHex.substring(0, 2), 16) / 255f
    val green = Integer.valueOf(sanitizedHex.substring(2, 4), 16) / 255f
    val blue = Integer.valueOf(sanitizedHex.substring(4, 6), 16) / 255f

    return Color(red, green, blue)
}

@Composable
fun getClipboardText(): String? {
    val context = LocalContext.current
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    // Get the primary clip (the first item on the clipboard)
    val clip = clipboardManager.primaryClip
    return clip?.getItemAt(0)?.text?.toString()  // Retrieve the text and convert it to String
}

fun chooseColorBasedOnLuminance(inputColor: Color): Color {
    // Extract normalized RGB values
    val red = inputColor.red
    val green = inputColor.green
    val blue = inputColor.blue
    // Compute luminance using the standard formula
    var luminance = 0.2126 * red + 0.7152 * green + 0.0722 * blue
    return when { // Determine the output color based on luminance
//        luminance < 0.5 -> Color.White
        luminance < 0.5 -> Color(0xf0f0f0FF)
//        luminance < 0.5 -> Color(0xFFFFF0FF)
        //luminance < 0.25 -> Color.White
        //luminance < 0.5 -> Color.Gray
        //luminance < 0.75 -> Color.DarkGray
        else -> Color.Black
    }
}
