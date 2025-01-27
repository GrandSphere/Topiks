package com.example.topics.utilities

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import com.example.topics2.ui.viewmodels.MessageViewModel
import java.io.File
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// TODO: Add to settings where user want app folder


fun copyFileToUserFolder(context: Context, messageViewModel: MessageViewModel, currentUri: Uri, directoryName: String):String{

    if (currentUri == null || currentUri.path.isNullOrBlank()) {
        Toast.makeText(context, "No file selected to import.", Toast.LENGTH_SHORT).show()
        return "E"
    }

    try {
        val externalDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "topics/files/${directoryName}")
          //val externalDir = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "topics/files")

        //val externalDir = File(context.getExternalFilesDir(null)?.parentFile, "com.yourpackage.name/Topics")

        if (!externalDir.exists() && !externalDir.mkdirs()) {
            throw IOException("Failed to create directory: $externalDir")
        }
        var fileName = getFileNameFromUri(context, currentUri)
        var destinationFile = File(externalDir, fileName)

        if (destinationFile.exists()) {
            val fileExtension =  destinationFile.extension
            val baseName = destinationFile.nameWithoutExtension

            // Get the current date and time
            val now = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
            val timestamp = now.format(formatter)

            fileName = "$baseName-$timestamp.$fileExtension"
            destinationFile = File(externalDir, fileName)
        }
        // copy the file
        val inputStream = context.contentResolver.openInputStream(currentUri)
            ?: throw IOException("Unable to open input stream for URI: $currentUri")
        val outputStream = destinationFile.outputStream()
        copyStream(inputStream, outputStream) // Use existing function to copy file contents
        inputStream.close()
        outputStream.close()
        Toast.makeText(context, "File imported successfully! You can find it in Documents/topics/files.", Toast.LENGTH_SHORT).show()
        return destinationFile.toString()

    } catch (e: IOException) {
        Toast.makeText(context, "Error importing file: ${e.message}", Toast.LENGTH_LONG).show()
        e.printStackTrace()
    }
    catch (e: Exception) {
        Toast.makeText(context, "Unexpected error: ${e.message}", Toast.LENGTH_LONG).show()
        e.printStackTrace()
    }
    return "E"
}


