import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.activity.result.ActivityResultCallback
import androidx.compose.runtime.LaunchedEffect

@Composable
fun DirectoryPicker(onDirectorySelected: (Uri?) -> Unit) {
    val context = LocalContext.current

    // Register the launcher for opening document tree (directory picker)
    val openDirectoryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree(),
        onResult = { uri: Uri? ->
            uri?.let { documentUri ->
                // Persist access permission to the URI
                val takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                context.contentResolver.takePersistableUriPermission(documentUri, takeFlags)

                // Return the selected directory URI to the caller
                onDirectorySelected(documentUri)
            }
        }
    )

    // Open the directory picker when the composable is first displayed or triggered
    LaunchedEffect(Unit) {
        openDirectoryLauncher.launch(null)
    }
}
