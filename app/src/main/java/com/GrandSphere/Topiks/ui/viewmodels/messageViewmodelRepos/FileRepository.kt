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