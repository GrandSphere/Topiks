package com.example.topics.utilities

import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

/**
 * Utility function to copy data from InputStream to OutputStream.
 * This function is reusable for various file operations.
 */
@Throws(IOException::class)
fun copyStream(inputStream: InputStream, outputStream: OutputStream) {
    val buffer = ByteArray(1024)
    var length: Int
    while (inputStream.read(buffer).also { length = it } != -1) {
        outputStream.write(buffer, 0, length)
    }
}
