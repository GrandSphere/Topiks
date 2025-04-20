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

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// FilePicker composable
// Utility function for handling the file picking
// FilePicker Composable that returns the ActivityResultLauncher
@Composable
fun iconFilePicker(
    onFileSelected: (Uri?) -> Unit,
): ActivityResultLauncher<Array<String>> {
    val context = LocalContext.current
    val openFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri: Uri? ->
            uri?.let { selectedUri ->
                // Persist access permission to the URI
                val takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                context.contentResolver.takePersistableUriPermission(selectedUri, takeFlags)
                // Return the selected file URI to the callback
                onFileSelected(selectedUri)
            } ?: run {
                // If no file is selected, return null
                onFileSelected(null)
            }
        }
    )
    // Return the launcher to be used outside
    return openFileLauncher
}

@Composable
fun multipleFilePicker(
    onUserFilesSelected: (List<Uri>?) -> Unit
): ActivityResultLauncher<Array<String>> {
    val context = LocalContext.current
    val openFilesLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments(),
        onResult = { uris: List<Uri>? ->
            if (uris.isNullOrEmpty()) {
                // If no files are selected, return null
                onUserFilesSelected(null)
            } else {
                // Persist access permissions for each URI
                val takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                uris.forEach { uri ->
                    context.contentResolver.takePersistableUriPermission(uri, takeFlags)
                }
                // Return the selected file URIs to the callback
                onUserFilesSelected(uris)
            }
        }
    )
    return openFilesLauncher
}