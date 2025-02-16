package com.example.topics.utilities

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// TODO: Add to settings where user want app folder

//RETURN NORMAL PATH FIRST, THEN THUMBNAIL PATH
fun copyFileToUserFolder(
    context: Context,
    currentUri: Uri,
    directoryName: String,
    width: Int = 100,
    height: Int = 100,
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
            val now = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
            val timestamp = now.format(formatter)

            fileName = "$baseName-$timestamp.$fileExtension"
            destinationFile = File(externalDir, fileName)
        }

        var thumbnailPath = ""
        val fileType = determineFileType(context, currentUri)

        if (fileType == "Image") {
            val folderName = if (thumbnailOnly) "Icons" else "Thumbnails"
            val folderDir = File(externalDir, folderName)

            if (!folderDir.exists() && !folderDir.mkdirs()) {
                throw IOException("Failed to create $folderName directory: $folderDir")
            }

            var compressedImageFile = File(folderDir, fileName)

            // Ensure unique naming for the thumbnail/icon file as well
            if (compressedImageFile.exists()) {
                val fileExtension = compressedImageFile.extension
                val baseName = compressedImageFile.nameWithoutExtension
                val now = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
                val timestamp = now.format(formatter)

                fileName = "$baseName-$timestamp.$fileExtension"
                compressedImageFile = File(folderDir, fileName)
            }

            compressImage(context, currentUri, compressedImageFile, width, height)
            thumbnailPath = compressedImageFile.toString()

            if (thumbnailOnly) {
                return Pair(thumbnailPath, "") // Only return the thumbnail file path
            }
        }

        // Copy the original file only if not in "thumbnailOnly" mode
        context.contentResolver.openInputStream(currentUri)?.use { inputStream ->
            destinationFile.outputStream().use { outputStream ->
                copyStream(inputStream, outputStream)
            }
        } ?: throw IOException("Unable to open input stream for URI: $currentUri")

        Toast.makeText(context, "File imported successfully! You can find it in ${destinationFile.parent}.", Toast.LENGTH_SHORT).show()
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
fun compressImage(
    context: Context,
    originalUri: Uri,
    compressedFile: File,
    maxWidth: Int = 100,
    maxHeight: Int = 100
): Boolean {
    val originalBitmap = BitmapFactory.decodeStream(
        context.contentResolver.openInputStream(originalUri)
    )

    if (originalBitmap == null) return false

    // Calculate aspect ratio
    val aspectRatio = originalBitmap.width.toFloat() / originalBitmap.height
    val newWidth: Int
    val newHeight: Int

    if (originalBitmap.width > originalBitmap.height) {
        newWidth = maxWidth
        newHeight = (maxWidth / aspectRatio).toInt()
    } else {
        newHeight = maxHeight
        newWidth = (maxHeight * aspectRatio).toInt()
    }

    // Resize the bitmap while keeping the aspect ratio
    val resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, false)
    var outputStream: FileOutputStream? = null
    try {
        outputStream = FileOutputStream(compressedFile)
        // Compress the resized image
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
        outputStream.flush()

        return true // Successful compression and save
    } catch (e: IOException) {
        e.printStackTrace()
        return false // In case of an error
    } finally {
        outputStream?.close()
        resizedBitmap.recycle() // Recycle the bitmap to free up memory
    }
}