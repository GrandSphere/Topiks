package com.example.topics2.unused
import android.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter


@Composable
fun testScreen2() { // Main screen
    val colors = MaterialTheme.colorScheme

    // Get image paths by calling the separate function
    val imagePaths = getTestImagePaths()

    // State to handle the visibility of additional images
    var showMore by remember { mutableStateOf(false) }

    // Column dimensions to be used
    val columnWidth = 300.dp
    val columnHeight = 300.dp

    // Image size for preview
    val imageSize = 30.dp
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
        if (showMore) {
            DisplayState1(
                imagePaths = imagePaths,
                maxImagesVisible = maxImagesVisible,
                imageSize = imageSize,
                imageSpacing = imageSpacing,
                columnWidth = columnWidth,
                columnHeight = columnHeight,
                onShowMore = { showMore = true } // Update state when "Show More" is clicked
            )
        } else {
            DisplayState2(
                imagePaths = imagePaths,
                imageSize = imageSize,
                imageSpacing = imageSpacing,
                onBack = { showMore = false } // Update state when "Back" is clicked
            )
        }
    }
}

@Composable
fun DisplayState1(
    imagePaths: List<String>,
    maxImagesVisible: Int,
    imageSize: Dp,
    imageSpacing: Dp,
    columnWidth: Dp,
    columnHeight: Dp,
    onShowMore: () -> Unit // Pass a lambda to update the state
) {
    Box(

        modifier = Modifier
            //.fillMaxSize()
            .width(columnWidth)
            //.height(columnHeight)
            .background(Color.Red)

    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // 2 images per row
            modifier = Modifier
                //.fillMaxSize()
                .width(columnWidth)
                //.height(columnHeight)
                .background(Color.Red)
        ) {
            items(imagePaths.take(maxImagesVisible)) { imagePath ->
                Image(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .width(imageSize)
                        //.height(50.dp)

                        //.size(imageSize)
                        .padding(end = imageSpacing, bottom = imageSpacing),
                    painter = rememberAsyncImagePainter(imagePath),
                    contentDescription = "Image",
                    //contentScale = ContentScale.Fit, // Ensures aspect ratio is maintained
                    contentScale = ContentScale.Crop, // Ensures aspect ratio is maintained
                )
            }

            // Show More button if there are more than 4 images
            item {
                if (imagePaths.size > maxImagesVisible) {
                    Button(
                        onClick = onShowMore, // Call the lambda passed from the parent
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
}

@Composable
fun DisplayState2(
    imagePaths: List<String>,
    imageSize: Dp,
    imageSpacing: Dp,
    onBack: () -> Unit // Pass a lambda to update the state
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()

    ) {
        LazyColumn(
            modifier = Modifier
                //.fillMaxWidth()
                //.fillMaxHeight()
                //.weight(1f)
                .fillMaxWidth()
                .background(Color.Blue)
        ) {
            items(imagePaths) { imagePath ->
                Image(
                    modifier = Modifier
                        .background(Color.Yellow)
                        //.fillMaxWidth() // Full width for the images
                        //.weight(1f)
                        //.width(300.dp)
                        .size(200.dp)
                        //.fillMaxWidth()
                        //.height(200.dp)
                        //.fillMaxHeight()
                        //.aspectRatio(0.5f)
                        //.height(imageSize) // Fixed height for each image
                        ,
                        //.padding(end = imageSpacing, bottom = imageSpacing),
                    painter = rememberAsyncImagePainter(imagePath),
                    contentScale = ContentScale.FillWidth
                    ,
                    contentDescription = "Image",
                    //contentScale = ContentScale.Fit, // Maintains aspect ratio
                )
            }
        }
        Button(
            onClick = onBack, // Call the lambda passed from the parent
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text("Back")
        }
    }
}