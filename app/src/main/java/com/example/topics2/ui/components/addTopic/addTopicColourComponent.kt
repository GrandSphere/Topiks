package com.example.topics2.ui.components.addTopic

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter

import com.example.topics.utilities.SelectImageWithPicker
import com.example.topics2.ui.viewmodels.TopicViewModel
import kotlinx.coroutines.coroutineScope

@Composable
fun TopicColour(navController: NavController, viewModel: TopicViewModel ) {
    val noteColour by viewModel.colour.collectAsState()
    val showFilePicker = viewModel.showPicker.collectAsState().value
    val colors = MaterialTheme.colorScheme
    var imageUrl = viewModel.fileURI.collectAsState().value

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box( modifier = Modifier.size(60.dp)){} // used only for spacing
        Spacer(modifier = Modifier.width(16.dp)) // Spacer to add some space between the rows
        Box(
            modifier = Modifier
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        // Clear focus when tapping outside
                        viewModel.setShowPicker(true)
                    })
                }
                .clip(CircleShape)
                .background(colors.secondary)
                .size(60.dp),
                 contentAlignment = Alignment.Center
        ) {
            if (showFilePicker) {
                SelectImageWithPicker(topicViewModel = viewModel)
            }

            if (imageUrl.length > 4) { // Load and display the image
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = "Circular Image",
                    contentScale = ContentScale.Crop, // Crop the image to fill the circle
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            } else {  // Show an icon as a fallback if no image URL is provided
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add Image",
                    tint = colors.onPrimary,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(24.dp)
                        .padding(0.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Box(
            modifier = Modifier .size(60.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            IconButton(
                onClick = {
                    navController.navigate("navcolourpicker")
                },
                modifier = Modifier.size(40.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(noteColour)
                )
            }
        }
    }
}