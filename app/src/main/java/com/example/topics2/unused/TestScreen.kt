package com.example.topics2.unused


import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import myFilePicker


import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.selects.select

@Composable
fun testScreen() {
    val colors = MaterialTheme.colorScheme

    // Holds the URI of the selected file
    val selectedFileUri: MutableState<Uri?> = remember { mutableStateOf(null) }

    // File picker launcher
    val openFileLauncher = myFilePicker(onFileSelected = { uri ->
        selectedFileUri.value = uri
    })

    val selectedFilePath: String = selectedFileUri.value?.toString() ?: "Wrong"
    //val selectedFilePath = "content://com.android.externalstorage.documents/document/primary%3ADocuments%2FtopicsContent%2F24.jpg"
   //val selectedFilePath = getTestImagePaths()[2]


    //val selectedFilePath =   getTestImagePaths()[0]
    Log.d("aabbcc",selectedFilePath)


    //val selectedFilePath: String = selectedFileUri.value.toString()?: ""

    Column(
        modifier = Modifier
            .fillMaxSize()
            //.padding(16.dp)
            .background(Color.DarkGray)
    ) {
        // IconButton to trigger the file picker
        IconButton(onClick = {
            openFileLauncher.launch(arrayOf("*/*")) // Launch the file picker
        })
        {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Attach",
                tint = colors.onBackground,
                modifier = Modifier
            )
        }

        // Display the selected file URI or a message if none is selected
        Text(
            text = selectedFilePath,
            color = colors.onBackground,
            modifier = Modifier.padding(top = 16.dp)
        )
        // Check if selectedFilePath has a valid file path and is not the fallback value ("nothing")
        // Display the image
        Image(
            //painter = rememberAsyncImagePainter(selectedFilePath),
            painter = rememberAsyncImagePainter(selectedFilePath),
            contentDescription = "Selected Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}