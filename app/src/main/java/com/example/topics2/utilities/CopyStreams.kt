package com.example.topics.utilities

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
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
    return fileName.trim()
}

fun determineFileType(context: Context, uri: Uri): String {
    // Attempt to get file extension first
    val extension = MimeTypeMap.getFileExtensionFromUrl(uri.toString()) ?: ""

    return when (extension.lowercase()) {
        "jpg", "jpeg", "png", "gif", "heic" -> "Image"
        "mp4", "mkv", "avi" -> "Video"
        "mp3", "wav", "aac" -> "Audio"
        "pdf" -> "PDF"
        "doc", "docx", "txt" -> "Document"
        else -> {
            // Fallback to MIME type if the extension is unknown
            val mimeType = context.contentResolver.getType(uri)
            when {
                mimeType == null -> "Unknown"
                mimeType.startsWith("image/") -> "Image"
                mimeType.startsWith("video/") -> "Video"
                mimeType.startsWith("audio/") -> "Audio"
                mimeType == "application/pdf" -> "PDF"
                mimeType.startsWith("application/") -> "Document"
                else -> "Unknown"
            }
        }
    }
}