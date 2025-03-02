package com.example.topics2.model

import android.content.ContentValues
import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.util.Log
import java.io.File
import java.io.FileOutputStream

class ToPDF {

    fun createPdfInDirectory(
        relativeDirectoryName: String, // e.g. "Exports/test"
        fileName: String,
        contentList: List<String>,
        separator: String? = null // Optional separator between messages
    ) {
        // Base directory is Documents/Topics/files/
        val baseDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        val fullDirectoryPath = File(baseDirectory, "Topics/files/$relativeDirectoryName")
        Log.d("ToPDF", "Full directory path: ${fullDirectoryPath.absolutePath}")

        // Ensure the directory exists, and create it if necessary
        if (!fullDirectoryPath.exists()) {
            if (fullDirectoryPath.mkdirs()) {
                Log.d("ToPDF", "Directory created: ${fullDirectoryPath.absolutePath}")
            } else {
                Log.e("ToPDF", "Failed to create directory: ${fullDirectoryPath.absolutePath}")
                return
            }
        } else {
            Log.d("ToPDF", "Directory already exists: ${fullDirectoryPath.absolutePath}")
        }

        // Create the PDF file in this directory
        val pdfFile = File(fullDirectoryPath, "$fileName.pdf")
        Log.d("ToPDF", "PDF file will be created at: ${pdfFile.absolutePath}")

        try {
            FileOutputStream(pdfFile).use { outputStream ->
                // Create a new PdfDocument and a page with A4 dimensions (595 x 842 points)
                val pdfDocument = PdfDocument()
                val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
                val page = pdfDocument.startPage(pageInfo)
                val canvas = page.canvas

                // Set up a paint object for text drawing
                val paint = Paint().apply { textSize = 12f }
                var yPosition = 25

                for ((index, message) in contentList.withIndex()) {
                    // Split each message by newline characters
                    val lines = message.split("\n")
                    for (line in lines) {
                        canvas.drawText(line, 10f, yPosition.toFloat(), paint)
                        yPosition += (paint.descent() - paint.ascent()).toInt()
                    }

                    // If a separator is provided, draw it; otherwise add extra space (newline equivalent)
                    if (separator != null) {
                        if (separator.isNotEmpty()) {
                            canvas.drawText(separator, 10f, yPosition.toFloat(), paint)
                        }
                        yPosition += (paint.descent() - paint.ascent()).toInt()
                    } else {
                        // Extra spacing if no separator is provided
                        yPosition += 10
                    }
                }

                // Finish the page and write the PDF document to the file
                pdfDocument.finishPage(page)
                pdfDocument.writeTo(outputStream)
                pdfDocument.close()
                Log.d("ToPDF", "PDF file written successfully.")
            }
        } catch (e: Exception) {
            Log.e("ToPDF", "Error writing PDF file: ${e.localizedMessage}", e)
        }
    }
}
