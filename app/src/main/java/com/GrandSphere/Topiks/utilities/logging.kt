package com.GrandSphere.Topiks.utilities

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun logFunc(context: Context, message: String) {
    // Define the directory where the log file will be stored
    val externalDir = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
        "topics/files"
    )

    // Create the directory if it doesn't exist
    if (!externalDir.exists()) {
        externalDir.mkdirs()
    }
    // Define the log file
    val logFile = File(externalDir, "app_log.txt")

    // Get the current timestamp
    val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

    // Format the log message with a timestamp
    val logMessage = "$timestamp: $message\n"

    try {
        // Append the log message to the file
        FileOutputStream(logFile, true).use { fos ->
            fos.write(logMessage.toByteArray())
        }
    } catch (e: IOException) {
        e.printStackTrace()
        // Handle the error (e.g., log to Logcat or show a toast)
    }
}