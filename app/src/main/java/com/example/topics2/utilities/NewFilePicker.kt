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
fun myFilePicker(
    onFileSelected: (Uri?) -> Unit,
    fileTypes: Array<String> = arrayOf("*/*") // Default file types (all files)
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
    onFilesSelected: (List<Uri>?) -> Unit,
    fileTypes: Array<String> = arrayOf("*/*") // Default file types
): ActivityResultLauncher<Array<String>> {
    val context = LocalContext.current
    val openFilesLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments(),
        onResult = { uris: List<Uri>? ->
            if (uris.isNullOrEmpty()) {
                // If no files are selected, return null
                onFilesSelected(null)
            } else {
                // Persist access permissions for each URI
                val takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                uris.forEach { uri ->
                    context.contentResolver.takePersistableUriPermission(uri, takeFlags)
                }
                // Return the selected file URIs to the callback
                onFilesSelected(uris)
            }
        }
    )
    return openFilesLauncher
}

