package com.GrandSphere.Topiks.utilities

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import java.io.File

private fun guessMimeTypeFromPath(path: String): String? {
    val ext = path.substringAfterLast('.', "").lowercase()
    if (ext.isBlank()) return null
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext)
}

fun filePathToShareUri(context: Context, filePath: String): Uri? {
    val f = File(filePath)
    if (!f.exists()) return null
    return try {
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", f)
    } catch (_: IllegalArgumentException) {
        null
    }
}

/**
 * Builds an outgoing share intent for one or more files.
 *
 * Callers must start it via `Intent.createChooser(...)`.
 */
fun buildShareIntentForFiles(
    context: Context,
    filePaths: List<String>,
    mimeTypeHint: String = "*/*",
): Intent? {
    val uris = filePaths.mapNotNull { filePathToShareUri(context, it) }.distinct()
    if (uris.isEmpty()) return null

    val effectiveType = when {
        mimeTypeHint != "*/*" -> mimeTypeHint
        uris.size == 1 -> (guessMimeTypeFromPath(filePaths.first()) ?: "application/octet-stream")
        else -> "*/*"
    }

    val intent = if (uris.size == 1) {
        Intent(Intent.ACTION_SEND).apply {
            type = effectiveType
            putExtra(Intent.EXTRA_STREAM, uris.first())
        }
    } else {
        Intent(Intent.ACTION_SEND_MULTIPLE).apply {
            type = effectiveType
            putParcelableArrayListExtra(Intent.EXTRA_STREAM, ArrayList(uris))
        }
    }

    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

    // Ensure per-URI grants for receivers that rely on ClipData.
    val resolver = context.contentResolver
    val clip = ClipData.newUri(resolver, "Shared files", uris.first())
    for (i in 1 until uris.size) {
        clip.addItem(ClipData.Item(uris[i]))
    }
    intent.clipData = clip

    return intent
}

fun buildShareIntentForText(text: String): Intent {
    return Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
}

