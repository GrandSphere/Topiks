/*******************************************************************************
 * Copyright (C) <2025> GrandSphere <GrandSphereStudios@protonmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.GrandSphere.Topiks.ui.components.addTopic

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
    val cleanedHex = hex.removePrefix("#")
    val colorInt = cleanedHex.toIntOrNull(16) ?: throw IllegalArgumentException("Invalid hex color string")
    val r = (colorInt shr 16) and 0xFF
    val g = (colorInt shr 8) and 0xFF
    val b = colorInt and 0xFF
    val hsv = FloatArray(3)
    android.graphics.Color.RGBToHSV(r, g, b, hsv)
    return hsv
}

fun colorToArgb(color: Color): Int {
    val alpha = (color.alpha * 255).toInt() and 0xFF
    val red = (color.red * 255).toInt() and 0xFF
    val green = (color.green * 255).toInt() and 0xFF
    val blue = (color.blue * 255).toInt() and 0xFF

    return (alpha shl 24) or (red shl 16) or (green shl 8) or blue
}
fun argbToColor(argb: Int): Color {
    val alpha = ((argb shr 24) and 0xFF) / 255f
    val red = ((argb shr 16) and 0xFF) / 255f
    val green = ((argb shr 8) and 0xFF) / 255f
    val blue = (argb and 0xFF) / 255f
    return Color(red, green, blue, alpha)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    modifier: Modifier = Modifier,
    thumbColor: Color = MaterialTheme.colorScheme.tertiary,
    activeTrackColor: Color = MaterialTheme.colorScheme.onSecondary,
    inactiveTrackColor: Color = MaterialTheme.colorScheme.secondary,
    thumbSize: Dp = 16.dp,
    trackHeight: Dp = 4.dp
) {
    val normalizedValue = (value - valueRange.start) / (valueRange.endInclusive - valueRange.start)

    Slider(
        value = normalizedValue,
        onValueChange = { newNormalizedValue ->
            onValueChange(valueRange.start + newNormalizedValue * (valueRange.endInclusive - valueRange.start))
        },
        valueRange = 0f..1f,
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
            val progressFraction = sliderPositions.value
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
    // Remove '#' if it exists, filter out any invalid hex digits and take first 6 characters
    val cleanHex = hex.removePrefix("#").filter { it.isDigit() || it in 'A'..'F' || it in 'a'..'f' }
    val sanitizedHex = cleanHex.take(6).padEnd(6, '0')
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
    val red = inputColor.red
    val green = inputColor.green
    val blue = inputColor.blue
    // Compute luminance using the standard formula
    var luminance = 0.2126 * red + 0.7152 * green + 0.0722 * blue
    return when { // Determine the output color based on luminance
        luminance < 0.5 -> Color(0xf0f0f0FF)
        else -> Color.Black
    }
}
