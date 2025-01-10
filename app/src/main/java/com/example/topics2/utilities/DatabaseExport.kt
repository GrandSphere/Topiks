//import com.example.topics.utilities.DirectoryPicker
//import com.example.topics.model.db.AppDatabase
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.documentfile.provider.DocumentFile
import com.example.topics2.db.AppDatabase
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


@Composable
fun ExportDatabaseWithPicker(onExportComplete: () -> Unit) {
        val context = LocalContext.current
        var selectedDirectoryUri by remember { mutableStateOf<Uri?>(null) }

        // Show the directory picker when the function is called
    // TODO I broke this
 //       DirectoryPicker(onDirectorySelected = { uri ->
 //           selectedDirectoryUri = uri
 //           // Once a directory is selected, trigger the export
 //           if (uri != null) {
 //               Log.d("Export Database to: ","$uri")
 //               exportDatabaseToUri(context, uri)
 //               onExportComplete()
 //           }
 //       })
    }

fun exportDatabaseToUri(context: Context, uri: Uri) {
    try {
        val databaseName = "topics_database" // The name of your database file
        // Get the database file from internal storage
        val databaseFile = File(context.getDatabasePath(databaseName).absolutePath)
        Log.d("Export Database from: ", "$databaseFile")

        val dbconn = AppDatabase.getDatabase(context)
        dbconn.close()

        if (databaseFile.exists()) {
            val resolver: ContentResolver = context.contentResolver

            // Create a DocumentFile object for the selected directory
            val documentDirectory = DocumentFile.fromTreeUri(context, uri)

            if (documentDirectory != null && documentDirectory.canWrite()) {
                // Create a new file in the selected directory
                val newFile = documentDirectory.createFile("application/octet-stream", databaseName)

                // Safely access the URI of the new file
                newFile?.uri?.let { newUri ->
                    resolver.openOutputStream(newUri)?.use { outputStream ->
                        // Open the input stream to the database file
                        FileInputStream(databaseFile).use { inputStream ->
                            copy(inputStream, outputStream)
                        }

                        // Show success message
                        Toast.makeText(context, "Database exported successfully!", Toast.LENGTH_SHORT).show()
                    } ?: run {
                        Toast.makeText(context, "Unable to open output stream for the selected location.", Toast.LENGTH_SHORT).show()
                    }
                } ?: run {
                    Toast.makeText(context, "Unable to create the new file.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Unable to access or write to the selected directory.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Database file not found!", Toast.LENGTH_SHORT).show()
        }
    } catch (e: IOException) {
        Toast.makeText(context, "Error exporting database: ${e.message}", Toast.LENGTH_LONG).show()
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