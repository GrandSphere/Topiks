package com.example.topics.utilities

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import android.webkit.MimeTypeMap
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



fun determineFileType(context: Context, uri: Uri): String {
    // Attempt to get MIME type
    val contentResolver = context.contentResolver
    val mimeType = contentResolver.getType(uri)

    return when {
        mimeType != null -> {
            // Map MIME type to a custom file type string if needed
            when {
                mimeType.startsWith("image/") -> "Image"
                mimeType.startsWith("video/") -> "Video"
                mimeType.startsWith("audio/") -> "Audio"
                mimeType == "application/pdf" -> "PDF"
                mimeType.startsWith("application/") -> "Document"
                else -> "Unknown"
            }
        }
        else -> {
            // Fallback to file extension if MIME type is not available
            val extension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
            when (extension.lowercase()) {
                "jpg", "jpeg", "png", "gif" -> "Image"
                "mp4", "mkv", "avi" -> "Video"
                "mp3", "wav", "aac" -> "Audio"
                "pdf" -> "PDF"
                "doc", "docx", "txt" -> "Document"
                else -> "Unknown"
            }
        }
    }
}
