package com.example.topics2.unused


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
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


