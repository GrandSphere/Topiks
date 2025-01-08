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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter

@Composable
fun ImageGridScreenBackup() {
    val colors = MaterialTheme.colorScheme

    // Get image paths by calling the separate function
    val imagePaths = getTestImagePaths()

    // State to handle the visibility of additional images
    var showMore by remember { mutableStateOf(false) }

    // Display the images in a grid
    Column(
        modifier = Modifier
            .fillMaxHeight()
            //.fillMaxSize()
            .width(150.dp)
            .padding(16.dp)
            .background(Color.DarkGray)

    ) {
        // Image grid (2 images per row)
        LazyColumn {
            items(imagePaths.take(if (showMore) imagePaths.size else 4)) { imagePath ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Display two images per row
                    Image(
                        painter = rememberAsyncImagePainter(imagePath),
                        contentDescription = "Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .weight(1f)
                            .height(200.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // Spacer between images
                }
            }

            // Show More button if there are more than 4 images
            item {
                if (imagePaths.size > 4) {
                    Button(
                        onClick = { showMore = !showMore },
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                    ) {
                        Text(text = if (showMore) "Show Less" else "Show More")
                    }
                }
            }
        }
    }
}

@Composable
fun ImageGridScreenBackup2() {
    val colors = MaterialTheme.colorScheme

    // Get image paths by calling the separate function
    val imagePaths = getTestImagePaths()

    // State to handle the visibility of additional images
    var showMore by remember { mutableStateOf(false) }

    // Calculate the number of images that fit in a row based on screen width and image size
    val imageSize = 150.dp // Fixed size for each image preview
    val maxImagesPerRow = 2
    val maxImagesVisible = maxImagesPerRow * 2 // Initially show 4 images

    // Display the images in a grid
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(16.dp)
            .background(Color.DarkGray)
    ) {
        // Image grid (2 images per row)
        LazyColumn {
            // Limit the number of images shown based on showMore state
            items(imagePaths.take(if (showMore) imagePaths.size else maxImagesVisible)) { imagePath ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Display each image in a fixed size container
                    Image(
                        painter = rememberAsyncImagePainter(imagePath),
                        contentDescription = "Image",
                        contentScale = ContentScale.Fit, // Ensures aspect ratio is maintained
                        modifier = Modifier
                            .weight(1f)
                            .height(imageSize)
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // Spacer between images
                }
            }

            // Show More button if there are more than 4 images
            item {
                if (imagePaths.size > maxImagesVisible) {
                    Button(
                        onClick = { showMore = !showMore },
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                    ) {
                        Text(text = if (showMore) "Show Less" else "Show More")
                    }
                }
            }
        }
    }
}

@Composable
fun ImageGridScreen3() {
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

    Box(
        modifier = Modifier
            .padding(8.dp)
            .background(Color.DarkGray)
    ) {
        if (!showMore) {
            // LazyGrid for initial image preview
            LazyVerticalGrid(
                columns = GridCells.Fixed(2), // 2 images per row
                modifier = Modifier
                    .width(columnWidth)
                    .height(columnHeight)
                //.fillMaxWidth()
                //.fillMaxHeight()
            ) {

               items(imagePaths.take(if (showMore) imagePaths.size else 4)) { imagePath ->
                //items(imagePaths.take(if (showMore) imagePaths.size else 4)) { imagePath ->
                Image(
                    painter = rememberAsyncImagePainter(imagePath),
                    contentDescription = "Image",
                    contentScale = ContentScale.Fit, // Ensures aspect ratio is maintained
                    modifier = Modifier
                        .size(imageSize)
                        .padding(end = imageSpacing, bottom = imageSpacing)
                        .align(Alignment.TopStart)
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
        } else {
            // LazyColumn with full width and height after Show More
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
                            .fillMaxHeight () // Full width for the images
                            //.height(imageSize) // Fixed height for each image
                            .padding(end = imageSpacing, bottom = imageSpacing)
                            .align(Alignment.TopStart)
                    )
                }
            }
        }
    }
}
@Composable
fun ImageGridScreen6() {
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

@Composable
fun testScreen2a() { // Image Grid
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

@Composable
fun testScreen2Backup(
    topicColor: Color = MaterialTheme.colorScheme.tertiary,
    topicFontColor: Color = MaterialTheme.colorScheme.onTertiary,
) { // Main screen
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
            //.background(Color.DarkGray)
            .fillMaxSize() // Takes up all available space
    ) {
        if (!showMore) {
            DisplayState1(
                imagePaths = imagePaths,
                //imageSize = imageSize,
                onShowMore = { showMore = true }, // Update state when "Show More" is clicked
                topicColor=topicColor,
                topicFontColor=topicFontColor,
            )
        } else {
            //DisplayState2(
            //    imagePaths = imagePaths,
            //    topicColor=topicColor,
            //    topicFontColor=topicFontColor,
            //    //imageSize = imageSize,
            //    //imageSpacing = imageSpacing,
            //    onBack = { showMore = false } // Update state when "Back" is clicked
            //)
        }
    }
}

@Composable
fun DisplayState1Backup(
    topicColor: Color,
    topicFontColor: Color,
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
fun DisplayState2Backup( // State 2
    topicColor: Color,
    topicFontColor: Color,
    imagePaths: List<String>,
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
//            .background(Color.Yellow)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
            //.background(Color.Red)
        ) {
            items(imagePaths) { imagePath ->
                SubcomposeAsyncImage(
                    model = imagePath,
                    contentDescription = getFileNameFromString(imagePath),
                    contentScale = ContentScale.Fit, // Maintains aspect ratio
                    loading = { // Show a placeholder while the image is loading
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth() // Take full width
                        .padding(bottom = 2.dp)
                    //.border(2.dp, Color.Black) // Add border around the image
                )
            }
        }

        FloatingActionButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.BottomEnd) // Align to the bottom end
                .padding(16.dp), // Add padding to the edge
            //containerColor = topicColor,
            //shape = RoundedCornerShape(16.dp), // Change the shape to rounded corners
            shape = CircleShape, // Change the shape to rounded corners

        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack, // Example icon
                contentDescription = "Add",
                tint = topicFontColor
            )
        }
    }
}