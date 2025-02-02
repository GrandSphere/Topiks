//import com.example.topics.utilities.DirectoryPicker
//import com.example.topics.model.db.AppDatabase
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.topics.utilities.copyStream
import com.example.topics2.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


suspend fun ExportDatabaseWithPicker(context: Context) {
    withContext(Dispatchers.IO) {
        val externalDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
            "topics/files"
        )
        if (!externalDir.exists() && !externalDir.mkdirs()) {
            throw IOException("Failed to create directory: $externalDir")
        }
        // Directly create a file path using externalDir
        val databaseName = "topics_database"
        val destinationFile = File(externalDir, databaseName)

        exportDatabaseToUri(context, destinationFile)
    }
}

suspend fun exportDatabaseToUri(context: Context, destinationFile: File) {
    try {
        val databaseName = "topics_database"
        // Get the database file from internal storage
        val databaseFile = File(context.getDatabasePath(databaseName).absolutePath)
        Log.d("Export Database from: ", "$databaseFile")

        // To ensure all wal writes get commit before copying the database
        val dbconn = AppDatabase.getDatabase(context)
        val checkpointQuery = SimpleSQLiteQuery("PRAGMA wal_checkpoint(FULL);")
        dbconn.topicDao().checkpoint(checkpointQuery)
        dbconn.messageDao().checkpoint(checkpointQuery)
        dbconn.fileDao().checkpoint(checkpointQuery)
        dbconn.categoryDao().checkpoint(checkpointQuery)

        if (databaseFile.exists()) {
            val outputStream = FileOutputStream(destinationFile)
            val inputStream = FileInputStream(databaseFile)
            copyStream(inputStream, outputStream)
            inputStream.close()
            outputStream.close()
            // Show success message on the main thread
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Database exported successfully!", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Show error message if the file doesn't exist (on the main thread)
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Database file not found!", Toast.LENGTH_SHORT).show()
            }
        }
    } catch (e: IOException) {
        // Handle any I/O exceptions (on the main thread)
        withContext(Dispatchers.Main) {
            Toast.makeText(context, "Error exporting database: ${e.message}", Toast.LENGTH_LONG).show()
            Log.d("FILEACCESS", "${e.message}")
        }
    }
}
