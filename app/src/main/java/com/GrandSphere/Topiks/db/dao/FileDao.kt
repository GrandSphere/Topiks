package com.GrandSphere.Topiks.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.GrandSphere.Topiks.db.entities.FileInfo
import com.GrandSphere.Topiks.db.entities.FilePath
import com.GrandSphere.Topiks.db.entities.FileTbl
import kotlinx.coroutines.flow.Flow

@Dao
interface FilesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFile(file: FileTbl): Long

    @Update
    suspend fun updateFile(file: FileTbl)

    @Query("DELETE FROM file_tbl WHERE id IN (:fileIds)")
    suspend fun deleteFilesByIds(fileIds: List<Int>)

    @Query("SELECT * FROM file_tbl")
    fun getAllFiles(): Flow<List<FileTbl>>

    @Query("SELECT * FROM file_tbl WHERE id = :fileId")
    fun getFileById(fileId: Int): Flow<FileTbl>

    @Query("SELECT * FROM file_tbl WHERE topicId = :topicId")
    fun getFilesByTopicId(topicId: Int): Flow<List<FileTbl>>

    @Query("SELECT id, filePath FROM file_tbl WHERE messageId = :messageId")
    suspend fun getFilesByMessageId(messageId: Int): List<FilePath>

    @Query("SELECT filePath, fileType, iconPath FROM file_tbl WHERE messageId = :messageId")
    fun getFilesByMessageIdFlow(messageId: Int): Flow<List<FileInfo>>

    @Query("SELECT * FROM file_tbl WHERE categoryId = :categoryId")
    fun getFilesByCategoryId(categoryId: Int): Flow<List<FileTbl>>

    @RawQuery
    fun checkpoint(query: SupportSQLiteQuery): Int

}
