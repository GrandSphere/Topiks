package com.example.topics.utilities

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext

@Composable
fun FilePicker(onFileSelected: (Uri?) -> Unit) {
    val context = LocalContext.current

    // Register the launcher for opening a document picker (file picker)
    val openFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri: Uri? ->
            uri?.let { selectedUri ->
                // Persist access permission to the URI
                val takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                context.contentResolver.takePersistableUriPermission(selectedUri, takeFlags)

                // Return the selected file URI to the caller
                // Invoke the callback in the composable context
                onFileSelected(selectedUri)
            }
        }
    )

    // Trigger the file picker on first composition or state change
    LaunchedEffect(Unit) {
        openFileLauncher.launch(arrayOf("*/*")) // You can specify the file types you need here
    }
}
