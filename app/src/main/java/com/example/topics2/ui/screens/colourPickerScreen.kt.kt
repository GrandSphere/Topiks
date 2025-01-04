package com.example.topics2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.topics2.ui.viewmodels.topicViewModel

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.topics2.ui.components.addTopic.CustomSlider
import com.example.topics2.ui.components.addTopic.colorToHsv


@Composable
fun ColorPickerScreen(navController: NavController, viewModel: topicViewModel = viewModel()) {
        //initialColor: Color = MaterialTheme.colorScheme.tertiary
    var initialColor: Color = Color.Red
    //val colors = MaterialTheme.colorScheme
    val hsv = colorToHsv(initialColor)
    val initialHue = hsv[0] // Hue
    val initialSaturation = hsv[1] // Saturation
    val initialValue = hsv[2] // Value
    val initialAlpha = initialColor.alpha
    // State to track the HSV components
    var hue by remember { mutableStateOf(initialHue) } // Use the preset value if provided
    var saturation by remember { mutableStateOf(initialSaturation) }
    var value by remember { mutableStateOf(initialValue) }
    var alpha by remember { mutableStateOf(initialAlpha) }
    // Calculate the selected color based on HSV
    val color = Color.hsv(hue, saturation, value, alpha)

    // Main UI
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        // Color preview Box
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape) // Make the box circular
                .background(color)
        )

        // Hue Slider
        Text(text = "Hue: ${hue.toInt()}")
        CustomSlider(
            value = hue / 360f, // Normalize hue to 0..1
            onValueChange = { hue = it * 360f }, // De-normalize back to 0..360
            valueRange = 0f..1f, // Use normalized range
        )

        // Saturation Slider
        Text(text = "Saturation: ${(saturation * 100).toInt()}%")
        CustomSlider(
            value = saturation,
            onValueChange = { saturation = it },
            valueRange = 0f..1f,
        )

        // Value Slider
        Text(text = "Value: ${(value * 100).toInt()}%")
        CustomSlider(
            value = value,
            onValueChange = { value = it },
            valueRange = 0f..1f,
        )

        // Alpha Slider
        Text(text = "Alpha: ${(alpha * 100).toInt()}%")
        CustomSlider(
            value = alpha,
            onValueChange = { alpha = it },
            valueRange = 0f..1f,
        )
    }
}
