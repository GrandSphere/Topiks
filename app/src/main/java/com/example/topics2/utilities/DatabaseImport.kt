package com.example.topics.utilities

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
import com.example.topics2.db.AppDatabase

//import com.example.topics.model.db.AppDatabase
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


@Composable
fun ImportDatabaseWithPicker(onImportComplete: () -> Unit) {
    // State to hold the selected file URI
    var fileUri by remember { mutableStateOf<Uri?>(null) }

    // Use the FilePicker composable to pick a file
    FilePicker(onFileSelected = { selectedUri ->
        fileUri = selectedUri
    })
    val context = LocalContext.current
    // Perform the import operation outside the composable body
    LaunchedEffect(fileUri) {
        fileUri?.let { uri ->
            // Ensure you call the importDatabaseFromUri function when the URI is selected
            importDatabaseFromUri(context, uri)
            // Notify that import is complete
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
                copy(inputStream, outputStream)

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

/**
 * Helper function to copy data from InputStream to OutputStream.
 */
private fun copy(inputStream: InputStream, outputStream: OutputStream) {
    val buffer = ByteArray(1024)
    var length: Int
    while (inputStream.read(buffer).also { length = it } != -1) {
        outputStream.write(buffer, 0, length)
    }
}
