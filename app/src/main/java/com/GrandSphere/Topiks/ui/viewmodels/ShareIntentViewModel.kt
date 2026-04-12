/*******************************************************************************
 * Copyright (C) <2025> GrandSphere <GrandSphereStudios@protonmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.GrandSphere.Topiks.ui.viewmodels

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

data class ShareDraft(
    val text: String,
    val uris: List<Uri>,
)

fun parseShareIntent(intent: Intent?): ShareDraft? {
    if (intent == null) return null
    val action = intent.action ?: return null
    if (action != Intent.ACTION_SEND && action != Intent.ACTION_SEND_MULTIPLE) return null

    val text = intent.getStringExtra(Intent.EXTRA_TEXT)?.trim().orEmpty()
    val uriSet = LinkedHashSet<Uri>()

    fun addUri(u: Uri?) {
        if (u != null && u != Uri.EMPTY) uriSet.add(u)
    }

    when (action) {
        Intent.ACTION_SEND -> {
            addUri(getParcelableExtraUriCompat(intent, Intent.EXTRA_STREAM))
            intent.clipData?.let { clip ->
                for (i in 0 until clip.itemCount) {
                    addUri(clip.getItemAt(i).uri)
                }
            }
        }
        Intent.ACTION_SEND_MULTIPLE -> {
            getParcelableArrayListUriCompat(intent, Intent.EXTRA_STREAM)?.forEach { addUri(it) }
            intent.clipData?.let { clip ->
                for (i in 0 until clip.itemCount) {
                    addUri(clip.getItemAt(i).uri)
                }
            }
        }
    }

    if (text.isEmpty() && uriSet.isEmpty()) return null
    return ShareDraft(text = text, uris = uriSet.toList())
}

@Suppress("DEPRECATION")
private fun getParcelableExtraUriCompat(intent: Intent, name: String): Uri? {
    return if (Build.VERSION.SDK_INT >= 33) {
        intent.getParcelableExtra(name, Uri::class.java)
    } else {
        intent.getParcelableExtra(name)
    }
}

@Suppress("DEPRECATION")
private fun getParcelableArrayListUriCompat(intent: Intent, name: String): ArrayList<Uri>? {
    return if (Build.VERSION.SDK_INT >= 33) {
        intent.getParcelableArrayListExtra(name, Uri::class.java)
    } else {
        intent.getParcelableArrayListExtra(name)
    }
}

class ShareIntentViewModel : ViewModel() {

    private var initialIntentHandled: Boolean = false

    private val _pendingDraft = MutableStateFlow<ShareDraft?>(null)
    val pendingDraft: StateFlow<ShareDraft?> = _pendingDraft.asStateFlow()

    private val _shareSessionId = MutableStateFlow(0)
    val shareSessionId: StateFlow<Int> = _shareSessionId.asStateFlow()

    private val _navigateToTopicList = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val navigateToTopicList: SharedFlow<Unit> = _navigateToTopicList.asSharedFlow()

    fun acceptIntent(context: Context, intent: Intent, fromNewIntent: Boolean) {
        if (!fromNewIntent && initialIntentHandled) return

        val draft = parseShareIntent(intent)
        if (!fromNewIntent) {
            initialIntentHandled = true
        }
        if (draft == null) return

        tryGrantUriPermissions(context, draft.uris, intent)
        _pendingDraft.value = draft
        _shareSessionId.value = _shareSessionId.value + 1
        if (fromNewIntent) {
            _navigateToTopicList.tryEmit(Unit)
        }
    }

    private fun tryGrantUriPermissions(context: Context, uris: List<Uri>, intent: Intent) {
        val flags = intent.flags and (Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        if (flags == 0) return
        if ((intent.flags and Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION) == 0) return
        for (u in uris) {
            if (u.scheme != "content") continue
            try {
                context.contentResolver.takePersistableUriPermission(u, flags)
            } catch (_: SecurityException) {
            }
        }
    }

    fun consumePendingDraft(): ShareDraft? {
        val d = _pendingDraft.value
        _pendingDraft.value = null
        return d
    }
}
