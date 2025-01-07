import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color

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
fun MyScreen() {
    // Holds the URI of the selected file
    val selectedFileUri: MutableState<Uri?> = remember { mutableStateOf(null) }
    val openFileLauncher = myFilePicker(onFileSelected = {uri->selectedFileUri.value=uri})

    // Get the file picker launcher from the FilePicker composable
    // Update the selected URI when a file is chosen

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.Red)
    ) {
        // IconButton to trigger the file picker
        IconButton(onClick = {
            // Trigger the file picker using the returned launcher
            // TODO THIS IS USED
            openFileLauncher.launch(arrayOf("*/*")) // Launch the file picker
        }) {
            Icon(Icons.Default.Add, contentDescription = "Pick a File")
        }
        // Display the selected file URI or a message if none is selected
        Text(
            text = "Selected file: ${selectedFileUri.value?.path ?: "None"}",
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}