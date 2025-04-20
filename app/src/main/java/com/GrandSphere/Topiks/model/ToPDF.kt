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

package com.GrandSphere.Topiks.model

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileOutputStream


class ToPDF {
    companion object {
        // Define margin constants
        const val LEFT_MARGIN = 40f
        const val RIGHT_MARGIN = 170f
        const val TOP_MARGIN = 25f
    }

    fun createPdfInDirectory(
        relativeDirectoryName: String,
        fileName: String,
        contentList: List<String>,
        separator: String? = null
    ): Boolean {
        val baseDirectory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        val fullDirectoryPath = File(baseDirectory, "topiks/files/$relativeDirectoryName")

        if (!fullDirectoryPath.exists() && !fullDirectoryPath.mkdirs()) {
            Log.e("ToPDF", "Failed to create directory: ${fullDirectoryPath.absolutePath}")
            return false
        }

        val pdfFile = File(fullDirectoryPath, "$fileName.pdf")

        return try {
            FileOutputStream(pdfFile).use { outputStream ->
                val pdfDocument = PdfDocument()
                val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
                val paint = Paint().apply { textSize = 12f }
                val pageWidth = pageInfo.pageWidth - LEFT_MARGIN - RIGHT_MARGIN
                var yPosition = TOP_MARGIN
                var page: PdfDocument.Page? = null
                var canvas: Canvas?

                Log.d("ToPDF", "Starting to write content to PDF.")

                for (message in contentList) {
                    val lines = wrapText(message, paint, pageWidth)
                    for (line in lines) {
                        if (yPosition + (paint.descent() - paint.ascent()) > pageInfo.pageHeight - TOP_MARGIN) {
                            // If yPosition exceeds the page height, start a new page
                            page?.let {
                                pdfDocument.finishPage(it)
                            }
                            page = pdfDocument.startPage(pageInfo)
                            canvas = page?.canvas
                            yPosition = TOP_MARGIN
                        } else {
                            canvas = page?.canvas
                        }
                        canvas?.drawText(line, LEFT_MARGIN, yPosition, paint)
                        yPosition += (paint.descent() - paint.ascent())
                    }

                    if (!separator.isNullOrEmpty()) {
                        if (yPosition + (paint.descent() - paint.ascent()) > pageInfo.pageHeight - TOP_MARGIN) {
                            // If yPosition exceeds the page height, start a new page
                            page?.let {
                                pdfDocument.finishPage(it)
                            }
                            page = pdfDocument.startPage(pageInfo)
                            canvas = page?.canvas
                            yPosition = TOP_MARGIN
                        } else {
                            canvas = page?.canvas
                        }
                        canvas?.drawText(separator, LEFT_MARGIN, yPosition, paint)
                        yPosition += (paint.descent() - paint.ascent())
                    }
                }

                page?.let {
                    pdfDocument.finishPage(it)
                }
                pdfDocument.writeTo(outputStream)
                pdfDocument.close()

                Log.d("ToPDF", "PDF file written successfully.")
                return (pdfFile.exists() && pdfFile.length() > 0)
            }
        } catch (e: Exception) {
            Log.e("ToPDF", "Error writing PDF file: ${e.localizedMessage}", e)
            return false
        }
    }

    private fun wrapText(text: String, paint: Paint, maxWidth: Float): List<String> {
        val wrappedLines = mutableListOf<String>()
        val words = text.split(" ")
        val currentLine = StringBuilder()

        for (word in words) {
            val testLine = currentLine.toString() + if (currentLine.isNotEmpty()) " $word" else word
            val width = paint.measureText(testLine)

            if (width <= maxWidth) {
                currentLine.append(if (currentLine.isNotEmpty()) " " else "").append(word)
            } else {
                wrappedLines.add(currentLine.toString())
                currentLine.clear()
                currentLine.append(word)
            }
        }

        if (currentLine.isNotEmpty()) {
            wrappedLines.add(currentLine.toString())
        }

        return wrappedLines
    }
}