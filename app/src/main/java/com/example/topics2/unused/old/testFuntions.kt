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

//val uriString = "content://com.android.externalstorage.documents/document/primary%3ADocuments%2FtopicsContent%2F20.png"
//val fileName = getFileNameFromUri(uriString)
//println(fileName) // This will print "20.png"