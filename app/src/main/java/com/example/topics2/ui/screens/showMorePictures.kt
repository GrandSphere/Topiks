package com.example.topics2.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.example.topics2.unused.getFileNameFromString
import com.example.topics2.unused.getTestImagePaths

@Composable
fun ShowMorePictures( // State 2
    navController: NavController,
) {
    // get these values in:
    val topicColor: Color = MaterialTheme.colorScheme.tertiary
    val topicFontColor: Color = MaterialTheme.colorScheme.onTertiary
    val imagePaths: List<String> = getTestImagePaths()
    //
    Box(
        modifier = Modifier
            //.background(Color.White) // Only useful for png's
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
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
            onClick = {navController.popBackStack() },
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