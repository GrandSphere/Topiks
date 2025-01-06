package com.example.topics.utilities

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.topics2.ui.components.global.compressImageToUri
import com.example.topics2.ui.viewmodels.MessageViewModel
import com.example.topics2.ui.viewmodels.TopicViewModel
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
// TODO: Add to settings where user want app folder

@Composable
fun SelectFileWithPicker(navController: NavController, messageViewModel: MessageViewModel) {

    var fileUri by remember { mutableStateOf<Uri?>(null) }
    if (fileUri == null) { FilePickerMessage(
        onFileSelected = {  selectedUri -> fileUri = selectedUri },
        navController = navController, fileTypes = arrayOf("*/*"),
        messageViewModel = messageViewModel
    ) }

    val context = LocalContext.current
    // Without the LaunchEffect things will go bad. Do not remove, filepicker will re-open
    LaunchedEffect(fileUri) {
        messageViewModel.setfilePicked(false)
        fileUri?.let { uri ->
            Log.d("AABBCCSelectFileWithPicker2", "Selected URI: $uri")
            messageViewModel.setURI(uri.toString())
            messageViewModel.setfilePicked(true)
            messageViewModel.setShowPicker(false)
            fileUri = null
            Toast.makeText(context, "File selected: $uri", Toast.LENGTH_SHORT).show()
        }

    }
}

fun copyFileToUserFolder(context: Context, messageViewModel: MessageViewModel) {
    val currentUri = messageViewModel.fileURI.value // Assuming ViewModel exposes URI as LiveData or StateFlow
    Log.d("AABBCCDD", "THIS IS THE URI BEFORE COPY: $currentUri")
    if (currentUri.isNullOrBlank()) {
        Toast.makeText(context, "No file selected to import.", Toast.LENGTH_SHORT).show()
        return
    }

    val uri = Uri.parse(currentUri)
    try {
        // Use a public directory like Downloads
        val externalDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "topics/files")
        Log.d("AABBCCDD", "THIS IS DESTINATION FILE: $externalDir")
        if (!externalDir.exists() && !externalDir.mkdirs()) {
            throw IOException("Failed to create directory: $externalDir")
        }

        // Extract the file name from the URI
        val fileName = getFileNameFromUri(context, uri)
        Log.d("AABBCCD", "Filename: $fileName")
        // Create the file in the accessible directory
        val destinationFile = File(externalDir, fileName)
        Log.d("AABBCCD", "destinationFile: $destinationFile")
        if (destinationFile.exists()) {
            Toast.makeText(context, "File already exists in destination folder.", Toast.LENGTH_SHORT).show()
            return
        }

        // Copy the file to the accessible folder
        val inputStream = context.contentResolver.openInputStream(uri) ?: throw IOException("Unable to open input stream for URI: $uri")
        val outputStream = destinationFile.outputStream()
        copyStream(inputStream, outputStream) // Use existing function to copy file contents
        inputStream.close()
        outputStream.close()

        // Update ViewModel URI path to the new location
        messageViewModel.setURI(destinationFile.absolutePath)

        Toast.makeText(context, "File imported successfully! You can find it in Documents/topics/files.", Toast.LENGTH_SHORT).show()
        messageViewModel.setfilePicked(false)
    } catch (e: IOException) {
        Log.e("AABBCCDD", "Error copying file: ${e.message}")
        messageViewModel.setfilePicked(false)
        Toast.makeText(context, "Error importing file: ${e.message}", Toast.LENGTH_LONG).show()
    }
}


