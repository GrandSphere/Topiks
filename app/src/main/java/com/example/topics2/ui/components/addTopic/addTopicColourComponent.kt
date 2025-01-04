package com.example.topics2.ui.components.addTopic

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.topics2.ui.viewmodels.TopicViewModel

@Composable
fun TopicColour(navController: NavController, viewModel: TopicViewModel = viewModel()) {
    val colors = MaterialTheme.colorScheme
    var categoryText by remember { mutableStateOf("Topics") }
    var selectedColor by remember { mutableStateOf(colors.secondary) }
    var imageUrl by remember { mutableStateOf<String?>(null) } // Add the imageUrl variable
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box( modifier = Modifier.size(60.dp)){} // used only for spacing
        Spacer(modifier = Modifier.width(16.dp)) // Spacer to add some space between the rows
        Box(
            modifier = Modifier
                .clip(CircleShape) // Clip the image into a circular shape
                .background(colors.secondary) // Optional: background color in case the image is not loaded
                .size(60.dp),
            contentAlignment = Alignment.Center
        ) {
            if (imageUrl != null) {
                // Load and display the image
                Image(
                    painter = rememberImagePainter(imageUrl),
                    contentDescription = "Circular Image",
                    contentScale = ContentScale.Crop, // Crop the image to fill the circle
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape) // Clip the image into a circular shape
                )
            } else {
                // Show an icon as a fallback if no image URL is provided
                Icon(
                    imageVector = Icons.Filled.Add, // Example icon
                    contentDescription = "Add Image",
                    tint = colors.onPrimary,
                    modifier = Modifier
                        .clip(CircleShape) // Clip the image into a circular shape
                        //.background(Color.Gray) // Optional: background color in case the image is not loaded
                        .size(24.dp)
                        .padding(0.dp) // Optional padding to adjust the icon size
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp)) // Spacer to add some space between the rows
        Box(
            modifier = Modifier .size(60.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            IconButton(
                onClick = {
                    //onNavigateColourPick()
                    // Handle color picker icon click
                    // selectedColor =
                    //     if (selectedColor == colors.secondary) Color.Red else colors.secondary // Toggle between colors
                },
                modifier = Modifier.size(40.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(selectedColor)
                )
            }
        }

    }
}
