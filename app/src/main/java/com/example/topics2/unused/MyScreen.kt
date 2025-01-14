package com.example.topics2.unused

import android.net.Uri
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import iconFilePicker


@Composable
fun MyScreen() {
    // Holds the URI of the selected file
    val selectedFileUri: MutableState<Uri?> = remember { mutableStateOf(null) }
    val openFileLauncher = iconFilePicker(onFileSelected = { uri->selectedFileUri.value=uri})

    // Get the file picker launcher from the FilePicker composable
    // Update the selected URI when a file is chosen

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.Red)
    ) {
        // IconButton to trigger the file picker
        IconButton(onClick = {
            // Trigger the file picker using the returned launcher
            // TODO THIS IS USED
            openFileLauncher.launch(arrayOf("*/*")) // Launch the file picker
        }) {
            Icon(Icons.Default.Add, contentDescription = "Pick a File")
        }
        // Display the selected file URI or a message if none is selected
        Text(
            text = "Selected file: ${selectedFileUri.value?.path ?: "None"}",
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}