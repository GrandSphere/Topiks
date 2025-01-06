package com.example.topics.utilities

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.topics2.ui.components.global.compressImageToUri
import com.example.topics2.ui.viewmodels.MessageViewModel
import com.example.topics2.ui.viewmodels.TopicViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
// TODO: Add to settings where user want app folder

@Composable
fun SelectFileWithPicker(navController: NavController, messageViewModel: MessageViewModel) {
    var fileUri by remember { mutableStateOf<Uri?>(null) }

    if (fileUri == null) {
        FilePicker(onFileSelected = { selectedUri ->
            fileUri = selectedUri
        }, fileTypes = arrayOf("*/*")) // Allow any file type
    }

    val context = LocalContext.current
    LaunchedEffect(fileUri) {
        fileUri?.let { uri ->
            // Set the URI path from the original location to the ViewModel
              messageViewModel.setURI(uri.toString())
            //  messageViewModel.setShowPicker(false)
            fileUri = null // Reset state after selection
            Toast.makeText(context, "File selected: $uri", Toast.LENGTH_SHORT).show()


        }
    }
    BackHandler(enabled = true, onBack = {
         messageViewModel.setShowPicker(false)

        navController.popBackStack()

    })

}

fun copyFileToUserFolder(context: Context, messageViewModel: MessageViewModel) {
    val currentUri = messageViewModel.fileURI.value // Assuming ViewModel exposes URI as LiveData or StateFlow
    Log.d("ZZZ FILE NAME", currentUri)
    if (currentUri.isNullOrBlank()) {
        Toast.makeText(context, "No file selected to import.", Toast.LENGTH_SHORT).show()
        return
    }

    val uri = Uri.parse(currentUri)
    try {
        val appName = context.getString(context.applicationInfo.labelRes) // Gets app label

        val externalDir = File(Environment.getExternalStorageDirectory(), "topics/files")
        if (!externalDir.exists()) externalDir.mkdirs()

        // Extract the file name from the URI
        val fileName = getFileNameFromUri(context, uri)


        // Create the file in the accessible directory
        val destinationFile = File(externalDir, fileName)
        if (destinationFile.exists()) {
            Toast.makeText(context, "File already exists in destination folder.", Toast.LENGTH_SHORT).show()
            return
        }

        // Copy the file to the accessible folder
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(destinationFile).use { outputStream ->
                copyStream(inputStream, outputStream)
            }
        }

        // Update ViewModel URI path to the new location
        messageViewModel.setURI(destinationFile.absolutePath)

        Toast.makeText(context, "File imported successfully!", Toast.LENGTH_SHORT).show()
    } catch (e: IOException) {
        Toast.makeText(context, "Error importing file: ${e.message}", Toast.LENGTH_LONG).show()
    }
}



