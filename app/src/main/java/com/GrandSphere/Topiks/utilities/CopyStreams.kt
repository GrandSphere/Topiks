package com.GrandSphere.Topiks.utilities

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import android.webkit.MimeTypeMap
import android.webkit.MimeTypeMap.getFileExtensionFromUrl
import androidx.core.net.toUri
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.URLConnection

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

fun getFileNameFromUri(context: Context, uri: Uri): String {
    var fileName = "default_image.png"
    val resolver: ContentResolver = context.contentResolver

    // Check if URI has a valid scheme
    if (uri.scheme == null || uri.scheme == "file" || uri.toString().startsWith("/")) {
        // Extract filename from file path or file URI
        val path = if (uri.scheme == "file") uri.path else uri.toString()
        path?.let {
            val file = File(it)
            if (file.exists()) {
                fileName = file.name
            } else {
                Log.w("QQRREE", "File does not exist: $it")
                // Fallback to last path segment
                fileName = it.substringAfterLast("/").takeIf { name -> name.isNotEmpty() } ?: fileName
            }
        } ?: Log.w("QQRREE", "Path is null for URI: $uri")
        return fileName.trim()
    }

    // Handle content URIs
    try {
        val cursor = resolver.query(uri, null, null, null, null)
        if (cursor == null) {
            return fileName
        }

        cursor.use {
            if (!it.moveToFirst()) {
                return fileName
            }

            // Log available columns for debugging
            val columns = it.columnNames.joinToString(", ")
            Log.d("QQRREE", "Available columns: $columns")

            try {
                val columnIndex = it.getColumnIndexOrThrow("_display_name")
                fileName = it.getString(columnIndex) ?: fileName
                Log.d("QQRREE", "Found display name: $fileName")
            } catch (e: IllegalArgumentException) {
                Log.e("QQRREE", "Column _display_name not found: ${e.message}")

                // Fallback: Try other possible column names
                val possibleColumns = listOf(
                    OpenableColumns.DISPLAY_NAME,
                    "title",
                    "name"
                )

                for (column in possibleColumns) {
                    val index = it.getColumnIndex(column)
                    if (index != -1) {
                        fileName = it.getString(index) ?: fileName
                        Log.d("QQRREE", "Used fallback column $column: $fileName")
                        break
                    }
                }
            }
        }
    } catch (e: Exception) {
        Log.e("QQRREE", "Error querying URI: $uri", e)
        // Fallback to path segment
        uri.path?.let { path ->
            val lastSegment = path.substringAfterLast("/")
            if (lastSegment.isNotEmpty()) {
                fileName = lastSegment
                Log.d("QQRREE", "Used path fallback: $fileName")
            }
        }
    }

    // Additional check for empty or invalid filename
    if (fileName.isEmpty() || fileName == "default_image.png") {
        Log.w("QQRREE", "Filename not resolved properly, using default: $fileName")
    } else {
        Log.d("QQRREE", "Final filename: $fileName")
    }

    return fileName.trim()
}

//fun determineFileType(context: Context, uri: Uri): String {
//    // Attempt to get file extension first
//    val extension = MimeTypeMap.getFileExtensionFromUrl(uri.toString()) ?: ""
//
//    return when (extension.lowercase()) {
//        "jpg", "jpeg", "png", "gif", "heic" -> "Image"
//        "mp4", "mkv", "avi" -> "Video"
//        "mp3", "wav", "aac" -> "Audio"
//        "pdf" -> "PDF"
//        "doc", "docx", "txt" -> "Document"
//        else -> {
//            // Fallback to MIME type if the extension is unknown
//            val mimeType = context.contentResolver.getType(uri)
//            when {
//                mimeType == null -> "Unknown"
//                mimeType.startsWith("image/") -> "Image"
//                mimeType.startsWith("video/") -> "Video"
//                mimeType.startsWith("audio/") -> "Audio"
//                mimeType == "application/pdf" -> "PDF"
//                mimeType.startsWith("application/") -> "Document"
//                else -> "Unknown"
//            }
//        }
//    }
//}



/**
 * Determines the file type based on the file extension or MIME type from a Uri or file path.
 *
 * This function extracts the file extension from a file path or Uri (e.g., /path/to/file.jpg or
 * content://media/123) using MimeTypeMapSingleton, or queries the MIME type via ContentResolver for
 * content:// URIs. It maps the result to a predefined file type category (e.g., Image, Video, Audio,
 * PDF, Document, Unknown). Requires a Context for content:// URI resolution.
 *
 * @param context The application Context for querying ContentResolver.
 * @param filePathOrUri The file path or Uri as a String (e.g., /path/to/file.jpg or content://media/123).
 * @return The file type as a String (Image, Video, Audio, PDF, Document, or Unknown).
 */
fun determineFileType(context: Context, filePathOrUri: String): String {
    // Log input for debugging
    Log.d("FileTypeUtils", "Input: $filePathOrUri")

    // Convert to Uri, adding file:// for raw paths
    val uri = try {
        if (filePathOrUri.startsWith("/")) {
            Uri.parse("file://$filePathOrUri")
        } else {
            Uri.parse(filePathOrUri)
        }
    } catch (e: Exception) {
        Log.e("FileTypeUtils", "Invalid input: $filePathOrUri, Error: ${e.message}")
        return "Unknown"
    }

    Log.d("FileTypeUtils", "Parsed Uri: $uri, Scheme: ${uri.scheme}, Path: ${uri.path}")

    // Handle content:// URIs with ContentResolver
    if (uri.scheme == "content") {
        val mimeType = try {
            context.contentResolver.getType(uri)
        } catch (e: Exception) {
            Log.e("FileTypeUtils", "Failed to get MIME type for $uri: ${e.message}")
            null
        }
        Log.d("FileTypeUtils", "MIME type: $mimeType")
        return when {
            mimeType == null -> "Unknown"
            mimeType.startsWith("image/") -> "Image"
            mimeType.startsWith("video/") -> "Video"
            mimeType.startsWith("audio/") -> "Audio"
            mimeType == "application/pdf" -> "PDF"
            mimeType.startsWith("application/") || mimeType.startsWith("text/") -> "Document"
            else -> "Unknown"
        }
    }

    // Try extracting extension using MimeTypeMapSingleton for file:// or raw paths
    val encodedUriString = uri.toString().replace(" ", "%20")
    val extensionFromMime = MimeTypeMap.getFileExtensionFromUrl(encodedUriString)?.lowercase()
    Log.d("FileTypeUtils", "Extension from MimeTypeMap: $extensionFromMime")

    // Fallback to manual parsing
    val extension = extensionFromMime ?: run {
        val path = uri.path?.takeIf { it.isNotEmpty() } ?: run {
            Log.d("FileTypeUtils", "Path is null or empty")
            return "Unknown"
        }
        Log.d("FileTypeUtils", "Manual parsing path: $path")
        val ext = path.substringAfterLast(".", "").lowercase().trim()
        if (ext.isEmpty() || ext.length > 5) {
            Log.d("FileTypeUtils", "Invalid extension from path: $ext")
            return "Unknown"
        }
        ext
    }

    Log.d("FileTypeUtils", "Final extension: $extension")

    // Map extension to file type
    return when (extension) {
        "jpg", "jpeg", "png", "gif", "heic" -> "Image"
        "mp4", "mkv", "avi" -> "Video"
        "mp3", "wav", "aac" -> "Audio"
        "pdf" -> "PDF"
        "doc", "docx", "txt" -> "Document"
        else -> {
            Log.d("FileTypeUtils", "Unknown extension: $extension")
            "Unknown"
        }
    }
}
