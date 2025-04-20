/*******************************************************************************
 * Copyright (C) <2025> GrandSphere <GrandSphereStudios@protonmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
        "topiks/files"
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