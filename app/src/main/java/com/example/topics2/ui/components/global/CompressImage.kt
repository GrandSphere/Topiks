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
    maxWidth: Int = 100,
    maxHeight: Int = 100,
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
        val outputFile = File(compressedUri.path) // Assuming compressedUri is a file path URI
        outputStream = FileOutputStream(outputFile)
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