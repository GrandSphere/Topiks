package com.example.topics.utilities

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import com.example.topics2.ui.viewmodels.MessageViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// TODO: Add to settings where user want app folder

//RETURN NORMAL PATH FIRST, THEN THUMBNAIL PATH
fun copyFileToUserFolder(
    context: Context,
    currentUri: Uri,
    directoryName: String,
    compressionPercentage: Int? = null,
    thumbnailOnly: Boolean = false
): Pair<String, String> {

    if (currentUri.path.isNullOrBlank()) {
        Toast.makeText(context, "No file selected to import.", Toast.LENGTH_SHORT).show()
        return Pair("E", "")
    }

    try {
        val externalDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "topics/files/$directoryName")
        if (!externalDir.exists() && !externalDir.mkdirs()) {
            throw IOException("Failed to create directory: $externalDir")
        }

        var fileName = getFileNameFromUri(context, currentUri)
        var destinationFile = File(externalDir, fileName)

        // If the file already exists, add timestamp to avoid overwrite
        if (destinationFile.exists()) {
            val fileExtension = destinationFile.extension
            val baseName = destinationFile.nameWithoutExtension

            // Get the current date and time
            val now = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
            val timestamp = now.format(formatter)

            fileName = "$baseName-$timestamp.$fileExtension"
            destinationFile = File(externalDir, fileName)
        }

        // Prepare the variables for the file paths
        var thumbnailPath = ""

        // Check if we need to compress the image and handle thumbnail-only logic
        val fileType = determineFileType(context, currentUri)

        if (compressionPercentage != null && fileType == "Image") {
            val folderName = if (thumbnailOnly) "icons" else "thumbnails"
            val folderDir = File(externalDir, folderName)

            if (!folderDir.exists() && !folderDir.mkdirs()) {
                throw IOException("Failed to create $folderName directory: $folderDir")
            }

            val compressedImageFile = File(folderDir, fileName)

            compressImage(currentUri, context, compressedImageFile, compressionPercentage)

            thumbnailPath = compressedImageFile.toString()
            if (thumbnailOnly) {
                return Pair(thumbnailPath, "") // Only return the thumbnail file path
            }
        }

        // If not in "thumbnailOnly" mode, copy the original file as well
        val inputStream = context.contentResolver.openInputStream(currentUri)
            ?: throw IOException("Unable to open input stream for URI: $currentUri")
        val outputStream = destinationFile.outputStream()
        copyStream(inputStream, outputStream) // Use existing function to copy file contents
        inputStream.close()
        outputStream.close()

        Toast.makeText(context, "File imported successfully! You can find it in Documents/topics/files.", Toast.LENGTH_SHORT).show()
        return Pair(destinationFile.toString(), thumbnailPath)

    } catch (e: IOException) {
        Toast.makeText(context, "Error importing file: ${e.message}", Toast.LENGTH_LONG).show()
        e.printStackTrace()
    } catch (e: Exception) {
        Toast.makeText(context, "Unexpected error: ${e.message}", Toast.LENGTH_LONG).show()
        e.printStackTrace()
    }
    return Pair("E", "")
}

// Compress the image and save it to the thumbnails folder
fun compressImage(
    uri: Uri,
    context: Context,
    compressedFile: File,
    compressionPercentage: Int
) {
    // Ensure that the compression percentage is within the valid range (0-100)
    val validCompressionPercentage = compressionPercentage.coerceIn(0, 100)
    val inputStream = context.contentResolver.openInputStream(uri)
        ?: throw IOException("Unable to open input stream for URI: $uri")

    val originalBitmap = BitmapFactory.decodeStream(inputStream)
    inputStream.close()

    val outputStream = FileOutputStream(compressedFile)
    originalBitmap.compress(Bitmap.CompressFormat.JPEG, validCompressionPercentage, outputStream)
    outputStream.close()
}