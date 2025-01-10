package com.example.topics2.unused


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter

@Composable
fun ImageGridScreen() {
  //  val imagePaths = viewModel.imagePaths
    Log.d("DEBUG_", "aaaaaaaaaaaaaaaaaaaaa")
    val imagePaths: List<String> by remember { mutableStateOf(listOf("content://com.android.externalstorage.documents/document/primary%3ADCIM%2FScreenshots%2FScreenshot_20210205-172826_Firefox.jpg")) }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // 2 columns for grid
        modifier = Modifier.fillMaxSize()
    ) {
        items(imagePaths.size) { index ->
            val imagePath = imagePaths[index]
            // Use Coil to load images or use your custom image loader
            Image(
                painter = rememberImagePainter(imagePath),
                contentDescription = "Image $index",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}


