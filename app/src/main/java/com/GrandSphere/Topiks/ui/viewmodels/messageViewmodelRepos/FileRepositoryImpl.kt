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
import com.GrandSphere.Topiks.db.dao.FilesDao
import com.GrandSphere.Topiks.db.entities.FileInfoWithIcon
import com.GrandSphere.Topiks.db.entities.FileTbl
import com.GrandSphere.Topiks.utilities.copyFileToUserFolder
import com.GrandSphere.Topiks.utilities.determineFileType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FileRepositoryImpl(
    private val filesDao: FilesDao
) : FileRepository {

    private val filePathMap = mutableMapOf<String, Int>()

    override suspend fun addFilesToMessage(
        messageId: Int,
        topicId: Int,
        files: List<Uri>,
        context: Context,
        widthSetting: Int,
        heightSetting: Int
    ) {
        files.forEach { uri ->
            val fileType = determineFileType(context, uri.toString())
            val (normalFilePath, thumbnailFilePath) = copyFileToUserFolder(
                context = context,
                currentUri = uri,
                directoryName = fileType,
                height = heightSetting,
                width = widthSetting
            )
            addFile(
                topicId = topicId,
                messageId = messageId,
                fileType = fileType,
                filePath = normalFilePath,
                description = "",
                iconPath = thumbnailFilePath,
                categoryId = 1,
                createTime = 1111  // TODO
            )
        }
    }

    override suspend fun addFile(
        topicId: Int,
        messageId: Int,
        fileType: String,
        filePath: String,
        description: String,
        iconPath: String,
        categoryId: Int,
        createTime: Long
    ) {
        val file = FileTbl(
            topicId = topicId,
            messageId = messageId,
            fileType = fileType,
            filePath = filePath,
            description = description,
            iconPath = iconPath,
            categoryId = categoryId,
            createTime = createTime
        )
        filesDao.insertFile(file)
        filePathMap[filePath] = file.id
    }

    override suspend fun deleteFiles(fileIds: List<Int>) {
        if (fileIds.isNotEmpty()) {
            filesDao.deleteFilesByIds(fileIds)
        }
    }

    override suspend fun getFilesByMessageId(messageId: Int): List<Uri> {
        val filePaths = filesDao.getFilesByMessageId(messageId)
        filePaths.forEach { filePath ->
            filePathMap[filePath.filePath] = filePath.id
        }
        return filePaths.map { Uri.parse(it.filePath) }
    }

    override fun getFilesByMessageIdFlow(messageId: Int): Flow<List<FileInfoWithIcon>> {
        return filesDao.getFilesByMessageIdFlow(messageId)
            .map { fileList ->
                fileList.map { FileInfoWithIcon(it.filePath, it.iconPath) }
            }
    }

    override fun getFileId(filePath: String): Int? {
        return filePathMap[filePath]
    }
}