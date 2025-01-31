package com.example.topics.utilities

//import com.example.topics.model.db.AppDatabase
import FilePicker
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
import com.example.topics2.activities.MainActivity
import com.example.topics2.db.AppDatabase
import com.example.topics2.ui.viewmodels.TopicViewModel
import iconFilePicker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

// Called from topAppBar

suspend fun importDatabaseFromUri(context: Context, uri: Uri) = withContext(Dispatchers.IO) {
    try {
        val databaseName = "topics_database"
        val currentDatabaseFile = File(context.getDatabasePath(databaseName).absolutePath)
        val walFile = File(context.getDatabasePath("$databaseName-wal").absolutePath)
        val shmFile = File(context.getDatabasePath("$databaseName-shm").absolutePath)

        Log.d("Import Database from: ", "$uri")
        Log.d("Import Database to: ", "$currentDatabaseFile")
        Log.d("Database File: ", "$currentDatabaseFile")
        Log.d("WAL File: ", "$walFile")
        Log.d("SHM File: ", "$shmFile")

        // Clear the existing database instance
        AppDatabase.clearInstance()

        // Close the database connection
        val dbconn = AppDatabase.getDatabase(context)
        dbconn.close()

        deleteWalFiles(walFile, shmFile)

        val resolver: ContentResolver = context.contentResolver
        resolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(currentDatabaseFile).use { outputStream ->
                copyStream(inputStream, outputStream)
                outputStream.flush()
                outputStream.fd.sync()
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "Database imported successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                    // Restart the activity
                    (context as? MainActivity)?.restartActivity()
                }
            }
        } ?: run {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    "Unable to open the selected file for import.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    } catch (e: IOException) {
        withContext(Dispatchers.Main) {
            Toast.makeText(context, "Error importing database: ${e.message}", Toast.LENGTH_LONG)
                .show()
        }
    } finally {
        // Reopen the database
        AppDatabase.getDatabase(context)
        Log.d("DB Import", "Database reopened.")
    }
}

fun deleteWalFiles(walFile: File, shmFile: File) {
    if (shmFile.exists()) {
        shmFile.delete()
    }
    if (walFile.exists()) {
        walFile.delete()
    }
}
