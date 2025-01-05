package com.example.topics.utilities

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.topics2.db.AppDatabase
import com.example.topics2.ui.viewmodels.TopicViewModel
import java.io.File
import java.io.IOException

@Composable
fun ImportImageWithPicker(onImportComplete: () -> Unit, topicViewModel: TopicViewModel) {
    var fileUri by remember { mutableStateOf<Uri?>(null) }

    if (fileUri == null) {
        FilePicker(onFileSelected = { selectedUri ->
            fileUri = selectedUri
        }, fileTypes = arrayOf("*/*")) // Restrict to image types
    }

    val context = LocalContext.current
    LaunchedEffect(fileUri) {
        fileUri?.let { uri ->
            importImageFromUri(context, uri, topicViewModel)
            fileUri = null // Reset state after import
            onImportComplete()
        }
    }
}

fun importImageFromUri(context: Context, uri: Uri, topicViewModel: TopicViewModel) {
    try {
        val iconsDir = File(context.filesDir, "icons")
        if (!iconsDir.exists()) iconsDir.mkdirs()

        // Extract the file name from the URI using ContentResolver
        val resolver: ContentResolver = context.contentResolver
        val imageName = getFileNameFromUri(context, uri)
    Log.d("ZZZ IMAGE NAME", imageName)

        val targetImageFile = File(iconsDir, imageName)

        resolver.openInputStream(uri)?.use { inputStream ->
            targetImageFile.outputStream().use { outputStream ->
                copyStream(inputStream, outputStream)
                // Use absolute path to ensure correct file path
                topicViewModel.setURI(targetImageFile.absolutePath)
                Toast.makeText(context, "Image imported successfully to /icons/ directory!", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(context, "Unable to open the selected file for import.", Toast.LENGTH_SHORT).show()
        }
    } catch (e: IOException) {
        Toast.makeText(context, "Error importing image: ${e.message}", Toast.LENGTH_LONG).show()
    }
}

// Function to get the file name from URI using ContentResolver
fun getFileNameFromUri(context: Context, uri: Uri): String {
    var fileName = "default_image.png"
    val resolver: ContentResolver = context.contentResolver
    val cursor = resolver.query(uri, null, null, null, null)

    cursor?.use {
        val columnIndex = it.getColumnIndexOrThrow("_display_name")
        if (it.moveToFirst()) {
            fileName = it.getString(columnIndex)
        }
    }
    return fileName
}