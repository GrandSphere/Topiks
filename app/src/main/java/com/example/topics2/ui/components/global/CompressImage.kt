package com.example.topics2.ui.components.global

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun compressImageToUri(
    context: Context,
    originalUri: Uri,
    compressedUri: Uri,
    width: Int = 100,   // Default width value
    height: Int = 100   // Default height value
): Boolean {
    // Decode the original image from URI
    val originalBitmap = BitmapFactory.decodeStream(context.contentResolver.openInputStream(originalUri))

    // Resize the bitmap to the desired dimensions
    val resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, false)

    var outputStream: FileOutputStream? = null
    try {
        // Open a FileOutputStream to the destination compressed URI
        val outputFile = File(compressedUri.path) // Assuming compressedUri is a file path URI
        outputStream = FileOutputStream(outputFile)

        // Compress the resized image and write it to the output stream (JPEG format, 90% quality)
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)

        // Flush and close the stream
        outputStream.flush()

        // Successful compression and save
        return true
    } catch (e: IOException) {
        e.printStackTrace()
        return false // In case of an error
    } finally {
        outputStream?.close()
        resizedBitmap.recycle() // Recycle the bitmap to free up memory
    }
}
