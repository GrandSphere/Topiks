package com.example.topics.utilities

//import com.example.topics.model.db.AppDatabase
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.topics2.db.AppDatabase
import java.io.File
import java.io.IOException

// Called from topAppBar
// TODO:: REFRESH UI SOMEHOW
@Composable
fun ImportDatabaseWithPicker(onImportComplete: () -> Unit, navController: NavController) {
    // State to hold the selected file URI
    var fileUri by remember { mutableStateOf<Uri?>(null) }

    // Use the FilePicker composable to pick a file
    if (fileUri == null) {
        Log.d("ZZZ Import URI", "EMPTY")
        // TODO I broke this
        //FilePickerIcon(onFileSelected = { selectedUri ->
        //    fileUri = selectedUri },
        //    fileTypes = arrayOf("*/*"),
        //    navController = navController,
        //)
    }
    val context = LocalContext.current
    LaunchedEffect(fileUri) {
        fileUri?.let { uri ->
            importDatabaseFromUri(context, uri)
            fileUri = null // Reset state after import
            onImportComplete()
        }
    }
}

fun importDatabaseFromUri(context: Context, uri: Uri) {
    try {

        // Get the database file from internal storage (the current one)
        val databaseName = "topics_database" // The name of your database file
        val currentDatabaseFile = File(context.getDatabasePath(databaseName).absolutePath)
        Log.d("Import Database from: ", "$uri")

        Log.d("Import Database to: ", "$currentDatabaseFile")

        val dbconn = AppDatabase.getDatabase(context)
        dbconn.close()

        // Open an InputStream for the selected file URI
        val resolver: ContentResolver = context.contentResolver
        resolver.openInputStream(uri)?.use { inputStream ->

            // Open an OutputStream for the current database file to overwrite it
            resolver.openOutputStream(Uri.fromFile(currentDatabaseFile))?.use { outputStream ->

                // Copy the content from the input stream (imported file) to the output stream (current database)
                copyStream(inputStream, outputStream)

                // Show success message
                Toast.makeText(context, "Database imported successfully!", Toast.LENGTH_SHORT).show()
            } ?: run {
                Toast.makeText(context, "Unable to open the current database for import.", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(context, "Unable to open the selected file for import.", Toast.LENGTH_SHORT).show()
        }
    } catch (e: IOException) {
        Toast.makeText(context, "Error importing database: ${e.message}", Toast.LENGTH_LONG).show()
    }
    finally {
        // After export, reopen the database
        AppDatabase.getDatabase(context)
    }
}