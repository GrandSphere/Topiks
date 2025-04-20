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

import android.content.Context
import android.net.Uri
import com.GrandSphere.Topiks.db.entities.FileInfoWithIcon
import kotlinx.coroutines.flow.Flow

/**
 * Manages files for messages, like adding, deleting, and getting file info.
 */
interface FileRepository {
    /** Add files to a message */
    suspend fun addFilesToMessage(
        messageId: Int,
        topicId: Int,
        files: List<Uri>,
        context: Context,
        widthSetting: Int,
        heightSetting: Int
    )

    /** Add a single file to the database */
    suspend fun addFile(
        topicId: Int,
        messageId: Int,
        fileType: String,
        filePath: String,
        description: String,
        iconPath: String,
        categoryId: Int,
        createTime: Long
    )

    /** Delete files by their IDs */
    suspend fun deleteFiles(fileIds: List<Int>)

    /** Get files associated with a message */
    suspend fun getFilesByMessageId(messageId: Int): List<Uri>

    /** Get a flow of files for a message (for real-time updates) */
    fun getFilesByMessageIdFlow(messageId: Int): Flow<List<FileInfoWithIcon>>

    /** Get the ID of a file by its path */
    fun getFileId(filePath: String): Int?
}