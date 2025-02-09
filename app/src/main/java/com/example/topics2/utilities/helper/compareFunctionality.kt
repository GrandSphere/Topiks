package com.example.topics2.utilities.helper

import android.net.Uri

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