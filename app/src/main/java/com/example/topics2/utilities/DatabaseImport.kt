package com.example.topics.utilities

//import com.example.topics.model.db.AppDatabase
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.StartOffsetType.Companion.Delay
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.topics2.DbTopics
import com.example.topics2.activities.MainActivity
import com.example.topics2.db.AppDatabase
import com.example.topics2.ui.viewmodels.TopicViewModel
import iconFilePicker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
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

        // Close the database connection
        val dbconn = AppDatabase.getDatabase(context)
        dbconn.close()

        Log.d("DELETED DB: " ,"${context.deleteDatabase(databaseName)}")
        AppDatabase.clearInstance()
        // Delete WAL and SHM files
        deleteWalFiles(walFile, shmFile, currentDatabaseFile)

        val resolver: ContentResolver = context.contentResolver
        resolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(currentDatabaseFile).use { outputStream ->
                copyStream(inputStream, outputStream)
                outputStream.flush()
                outputStream.fd.sync()

                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Database imported successfully!", Toast.LENGTH_SHORT).show()
                }
            }
        } ?: run {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Unable to open the selected file for import.", Toast.LENGTH_SHORT).show()
            }
        }
    } catch (e: IOException) {
        withContext(Dispatchers.Main) {
            Toast.makeText(context, "Error importing database: ${e.message}", Toast.LENGTH_LONG).show()
        }
    } finally {
        // Clear and reopen the database to initialize it with the new data
        AppDatabase.clearInstance()
        AppDatabase.getDatabase(context)
    }
}


fun deleteWalFiles(walFile: File, shmFile: File, currentDBFile: File) {
    if (shmFile.exists()) {
        shmFile.delete()
    }
    if (walFile.exists()) {
        walFile.delete()
    }
    if (currentDBFile.exists()) {
        currentDBFile.delete()
    }
}
fun isFileAccessible(file: File): Boolean {
    return try {
        // Try opening the file for reading and writing (non-blocking)
        val fileInputStream = FileInputStream(file)
        fileInputStream.close()  // Close immediately after checking
        true  // File is accessible
    } catch (e: IOException) {
        // File is either locked or not accessible
        Log.e("FileAccess", "Error accessing file: ${e.message}")
        false
    }
}
