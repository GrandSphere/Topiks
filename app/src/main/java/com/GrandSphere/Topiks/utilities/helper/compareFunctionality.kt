/*******************************************************************************
 * Copyright (C) <2025> GrandSphere <GrandSphereStudios@protonmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
