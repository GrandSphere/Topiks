package com.GrandSphere.Topiks.utilities

//import com.GrandSphere.Topiks.model.db.AppDatabase
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.GrandSphere.Topiks.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

// Called from topAppBar

suspend fun importDatabaseFromUri(context: Context, uri: Uri) = withContext(Dispatchers.IO) {
    try {
        val databaseName = "topiks_database"
        val currentDatabaseFile = File(context.getDatabasePath(databaseName).absolutePath)
        val walFile = File(context.getDatabasePath("$databaseName-wal").absolutePath)
        val shmFile = File(context.getDatabasePath("$databaseName-shm").absolutePath)

        logFunc(context, "Import Database from: $uri")
        logFunc(context, "Import Database to: $currentDatabaseFile")
        logFunc(context, "Database File: $currentDatabaseFile")
        logFunc(context, "WAL File: $walFile")
        logFunc(context, "SHM File: $shmFile")

        // Close the database connection
        val dbconn = AppDatabase.getDatabase(context)
        dbconn.close()

        logFunc(context, "DELETED DB: ${context.deleteDatabase(databaseName)}")
        AppDatabase.clearInstance()

        // Delete WAL and SHM files
        deleteWalFiles(walFile, shmFile, currentDatabaseFile, context)

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
        logFunc(context, "Error importing database: ${e.message}")
        withContext(Dispatchers.Main) {
            Toast.makeText(context, "Error importing database: ${e.message}", Toast.LENGTH_LONG).show()
        }
    } catch (e: Exception) {
        logFunc(context, "Unexpected error: ${e.message}")
        withContext(Dispatchers.Main) {
            Toast.makeText(context, "Unexpected error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    } finally {
        // Clear and reopen the database to initialize it with the new data
        AppDatabase.clearInstance()
        AppDatabase.getDatabase(context)
    }

}

fun deleteWalFiles(walFile: File, shmFile: File, currentDBFile: File, context: Context) {
    try {
        if (shmFile.exists()) {
            shmFile.delete()
        }
        if (walFile.exists()) {
            walFile.delete()
        }
        if (currentDBFile.exists()) {
            currentDBFile.delete()
        }
    } catch (e: IOException) {
        logFunc(context, "Error deleting WAL/SHM files: ${e.message}")
    }
}
