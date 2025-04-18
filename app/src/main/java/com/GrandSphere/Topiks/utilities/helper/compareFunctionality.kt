package com.GrandSphere.Topiks.utilities.helper

import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract

fun compareFileLists(
    originalList: List<Uri>?,
    editedList: List<Uri>?
    ): Pair<List<Uri>, List<Uri>> {
        val original = originalList ?: emptyList()
        val edited = editedList ?: emptyList()

        val deletedFiles = original.filterNot { edited.contains(it) }
        val addedFiles = edited.filterNot { original.contains(it) }

        return Pair(deletedFiles, addedFiles)
    }


fun isSameFile(context: Context, uri1: Uri, uri2: Uri): Boolean {
    if (uri1 == uri2) return true // Direct comparison

    if (uri1.scheme == "file" && uri2.scheme == "file") {
        return uri1.path == uri2.path
    }

    if (uri1.authority == "com.android.externalstorage.documents" &&
        uri2.authority == "com.android.externalstorage.documents"
    ) {
        return DocumentsContract.getDocumentId(uri1) == DocumentsContract.getDocumentId(uri2)
    }

    return false
}
