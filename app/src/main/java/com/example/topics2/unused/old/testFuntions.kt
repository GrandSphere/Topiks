package com.example.topics2.unused.old

import android.net.Uri
import java.net.URLDecoder

fun getFileNameFromString(uriString: String): String {
    // Decode the URI (handles percent-encoding like %3A -> :)
    val decodedUri = URLDecoder.decode(uriString, "UTF-8")

    // Create a Uri object from the decoded URI string
    val uri = Uri.parse(decodedUri)

    // Get the last path segment which is the file name
    return uri.lastPathSegment ?: ""
}