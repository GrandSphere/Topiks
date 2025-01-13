package com.example.topics.utilities

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import com.example.topics2.ui.viewmodels.MessageViewModel
import java.io.File
import java.io.IOException

// TODO: Add to settings where user want app folder


fun copyFileToUserFolder(context: Context, messageViewModel: MessageViewModel, currentUri: Uri){
  //  val currentUri = messageViewModel.fileURI.value

    if (currentUri == null || currentUri.path.isNullOrBlank()) {
        Toast.makeText(context, "No file selected to import.", Toast.LENGTH_SHORT).show()
        return
    }

    try {
        // Use a public directory like Downloads
        val externalDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "topics/files")

        if (!externalDir.exists() && !externalDir.mkdirs()) {
            throw IOException("Failed to create directory: $externalDir")
        }
        // Extract the file name from the URI
        val fileName = getFileNameFromUri(context, currentUri)
        messageViewModel.setfileName(currentUri.toString())

        // Create the file in the accessible directory
        val destinationFile = File(externalDir, fileName)
        messageViewModel.setdestURI(destinationFile.toString())
        if (destinationFile.exists()) {
            Toast.makeText(context, "File already exists in destination folder.", Toast.LENGTH_SHORT).show()
            return
        }

        // Copy the file to the accessible folder
        val inputStream = context.contentResolver.openInputStream(currentUri) ?: throw IOException("Unable to open input stream for URI: $currentUri")
        val outputStream = destinationFile.outputStream()
        copyStream(inputStream, outputStream) // Use existing function to copy file contents
        inputStream.close()
        outputStream.close()

        // Update ViewModel URI path to the new location
        messageViewModel.setFileURI(destinationFile.absolutePath)

        Toast.makeText(context, "File imported successfully! You can find it in Documents/topics/files.", Toast.LENGTH_SHORT).show()

    } catch (e: IOException) {

        Toast.makeText(context, "Error importing file: ${e.message}", Toast.LENGTH_LONG).show()
    }
}


