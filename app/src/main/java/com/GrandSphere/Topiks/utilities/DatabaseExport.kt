//import com.GrandSphere.Topiks.utilities.DirectoryPicker
//import com.GrandSphere.Topiks.model.db.AppDatabase
import android.content.Context
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.sqlite.db.SimpleSQLiteQuery
import com.GrandSphere.Topiks.db.AppDatabase
import com.GrandSphere.Topiks.utilities.copyStream
import com.GrandSphere.Topiks.utilities.logFunc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


suspend fun ExportDatabaseWithPicker(context: Context) {
    withContext(Dispatchers.IO) {
        val externalDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
            "topiks/files"
        )
        if (!externalDir.exists() && !externalDir.mkdirs()) {
            logFunc(context, "Failed to create directory: $externalDir")
            throw IOException("Failed to create directory: $externalDir")
        }
        // Directly create a file path using externalDir
        val databaseName = "topiks_database"

        // Get the current date and time
        val now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
        val timestamp = now.format(formatter)
        val fileName = "$databaseName-$timestamp"
        val destinationFile = File(externalDir, fileName)

        exportDatabaseToUri(context, destinationFile)
    }
}

suspend fun exportDatabaseToUri(context: Context, destinationFile: File) {
    try {
        val databaseName = "topiks_database"
        // Get the database file from internal storage
        val databaseFile = File(context.getDatabasePath(databaseName).absolutePath)
        Log.d("Export Database from: ", "$databaseFile")

        // Log the database file path
        logFunc(context, "Export Database from: $databaseFile")

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
            logFunc(context, "Database exported successfully to $destinationFile")
        } else {
            // Show error message if the file doesn't exist (on the main thread)
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Database file not found!", Toast.LENGTH_SHORT).show()
            }
            logFunc(context, "Database file not found!")
        }
    } catch (e: IOException) {
        // Handle any I/O exceptions (on the main thread)
        withContext(Dispatchers.Main) {
            Toast.makeText(context, "Error exporting database: ${e.message}", Toast.LENGTH_LONG).show()
            Log.d("FILEACCESS", "${e.message}")
        }
        // Log the exception
        logFunc(context, "Error exporting database: ${e.message}")
    }
}

