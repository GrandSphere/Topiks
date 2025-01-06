package com.example.topics2.ui.screens


import androidx.compose.material3.ColorScheme
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.util.lerp
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Surface
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.draw.clip
import androidx.navigation.NavController
import androidx.navigation.Navigator
import com.example.topics2.ui.viewmodels.TopicViewModel

@Composable
fun ColorGridScreen(colors: List<Color>, navController: NavController, viewModel: TopicViewModel) {
    // Get screen width and calculate block size (5 blocks per row
    val combinedColors = getColorsWithMaterialColors(MaterialTheme.colorScheme, colors)
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val blockSize = screenWidth / 7 // 5 blocks in a row

    Box(
        modifier = Modifier.fillMaxSize() // Make sure the Box fills the screen
    ) {
        // LazyVerticalGrid for displaying colors
        LazyVerticalGrid(
            columns = GridCells.Fixed(7), // 5 blocks per row
            modifier = Modifier
                .fillMaxWidth() // Ensure the grid takes up the full width
                //.weight(1f) // Take up the remaining space
                .fillMaxSize()
        ) {
            items(combinedColors.size) { index ->
                Surface(
                    modifier = Modifier
                        .size(blockSize) // Ensure each block is a square (width = height)
                        .padding(4.dp) // Add padding around each color block
                        .clip(CircleShape) // Clip the surface into a circle
                        .clickable { /* Handle tap event */ }
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {
                                    Log.d("zzzSelectedColour", "Selected color: ${combinedColors[index]}")
                                }
                            )
                        },
                    color = combinedColors[index], // Set the background color
                    shape = CircleShape // Circle shape for the surface
                ) {
                    // No content inside, just the color and clickable area
                }
            }
        }

        // IconButton placed at the bottom center of the screen
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.BottomCenter) // Align the button at the bottom center
                .padding(16.dp) // Add padding around the button
                .size(30.dp) // Adjust the size of the button as needed
        ) {
            Icon(
                modifier = Modifier.size(30.dp),
                imageVector = Icons.Filled.Close,
                contentDescription = "Cancel",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}


// Helper function to prepend all colors from MaterialTheme's ColorScheme
fun getColorsWithMaterialColors(colorScheme: ColorScheme, existingColors: List<Color>): List<Color> {
    // Prepend all colors from the provided ColorScheme
    Log.d("zzz My original array size",existingColors.size.toString())
    return listOf <Color>(
        colorScheme.primary,
        colorScheme.onPrimary,
        colorScheme.secondary,
        colorScheme.onSecondary,
        colorScheme.background,
        colorScheme.onBackground,
        colorScheme.surface,
        colorScheme.onSurface,
        colorScheme.error,
        colorScheme.onError,
        colorScheme.outline,
        colorScheme.inversePrimary,
        colorScheme.scrim,
        colorScheme.surfaceVariant,
        colorScheme.onSurfaceVariant,
        colorScheme.tertiary,
        colorScheme.onTertiary,
        colorScheme.tertiaryContainer,
        colorScheme.onTertiaryContainer
    ) + existingColors
}