package com.example.topics.utilities

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

/**
 * Utility function to copy data from InputStream to OutputStream.
 * This function is reusable for various file operations.
 */
@Throws(IOException::class)
fun copyStream(inputStream: InputStream, outputStream: OutputStream) {
    val buffer = ByteArray(1024)
    var length: Int
    while (inputStream.read(buffer).also { length = it } != -1) {
        outputStream.write(buffer, 0, length)
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