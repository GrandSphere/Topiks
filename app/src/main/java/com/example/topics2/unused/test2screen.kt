package com.example.topics2.unused
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import coil.compose.rememberAsyncImagePainter


@Composable
fun ImageGridScreen() {
    val colors = MaterialTheme.colorScheme

    // Get image paths by calling the separate function
    val imagePaths = getTestImagePaths()

    // State to handle the visibility of additional images
    var showMore by remember { mutableStateOf(false) }

    // Column dimensions to be used
    val columnWidth = 300.dp
    val columnHeight = 300.dp

    // Image size for preview
    val imageSize = 120.dp
    val imageSpacing = 2.dp // Spacing between images

    // Calculate the maximum number of images to show
    val maxImagesVisible = 4 // Initially show only 4 images

    // Main container for switching between display states
    Column(
        modifier = Modifier
            .padding(8.dp)
            .background(Color.DarkGray)
            .fillMaxSize() // Takes up all available space
    ) {
        // Display State 1: Image grid with a limited number of images
        if (!showMore) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2), // 2 images per row
                modifier = Modifier
                    .width(columnWidth)
                    .height(columnHeight)
            ) {
                items(imagePaths.take(maxImagesVisible)) { imagePath ->
                    Image(
                        painter = rememberAsyncImagePainter(imagePath),
                        contentDescription = "Image",
                        contentScale = ContentScale.Fit, // Ensures aspect ratio is maintained
                        modifier = Modifier
                            .size(imageSize)
                            .padding(end = imageSpacing, bottom = imageSpacing)
                            //.align(Alignment.TopStart)
                            //.align(Alignment.Bottom)
                    )
                }

                // Show More button if there are more than 4 images
                item {
                    if (imagePaths.size > maxImagesVisible) {
                        Button(
                            onClick = { showMore = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        ) {
                            Text("Show More")
                        }
                    }
                }
            }
        }

        // Display State 2: Lazy column showing all images
        if (showMore) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                items(imagePaths) { imagePath ->
                    Image(
                        painter = rememberAsyncImagePainter(imagePath),
                        contentDescription = "Image",
                        contentScale = ContentScale.Fit, // Maintains aspect ratio
                        modifier = Modifier
                            .fillMaxWidth() // Full width for the images
                            .height(imageSize) // Fixed height for each image
                            .padding(end = imageSpacing, bottom = imageSpacing)
                            //.align(Alignment.TopStart)
                    )
                }

                // Back button at the bottom of the screen
                item {
                    Button(
                        onClick = { showMore = false },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Text("Back")
                    }
                }
            }
        }
    }
}