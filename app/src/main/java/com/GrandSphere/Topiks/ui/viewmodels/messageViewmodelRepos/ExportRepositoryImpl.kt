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

package com.GrandSphere.Topiks.ui.viewmodels.messageViewmodelRepos

import android.util.Log
import com.GrandSphere.Topiks.model.ToPDF

class ExportRepositoryImpl : ExportRepository {

    private val _messagesContentById = mutableMapOf<Int, String>()
    private var topicId: Int = 0
    private var onToastUpdate: (String) -> Unit = {}

    fun setMessagesContentById(messagesContent: Map<Int, String>) {
        _messagesContentById.clear()
        _messagesContentById.putAll(messagesContent)
    }

    fun setTopicId(id: Int) {
        topicId = id
    }

    fun setOnToastUpdate(callback: (String) -> Unit) {
        onToastUpdate = callback
    }

    override suspend fun exportMessagesToPDF(messageIDs: Set<Int>) {
        val toPdf = ToPDF()
        val contentList = messageIDs.mapNotNull { id -> _messagesContentById[id] }
        val fileName: String = if (messageIDs.size > 1) "${topicId}_multiple" else "${topicId}_${messageIDs.first()}"
        Log.d("ExportRepository", "Exporting ${contentList.size} messages to PDF for IDs: $messageIDs")
        if (contentList.isEmpty()) {
            Log.w("ExportRepository", "No messages found for IDs: $messageIDs")
            onToastUpdate("No Messages found to export")
            return
        }
        val success = toPdf.createPdfInDirectory(
            relativeDirectoryName = "Exports",
            fileName = fileName,
            contentList = contentList
        )
        onToastUpdate(if (success) "Export successful" else "Export failed")
    }
}
