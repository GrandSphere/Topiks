package com.example.topics2.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.topics2.db.entities.FileTbl
import kotlinx.coroutines.flow.Flow

@Dao
interface FilesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFile(file: FileTbl): Long

    @Update
    suspend fun updateFile(file: FileTbl)

    @Delete
    suspend fun deleteFile(file: FileTbl)

    @Query("SELECT * FROM file_tbl")
    fun getAllFiles(): Flow<List<FileTbl>>

    @Query("SELECT * FROM file_tbl WHERE id = :fileId")
    fun getFileById(fileId: Int): Flow<FileTbl>

    @Query("SELECT * FROM file_tbl WHERE topicId = :topicId")
    fun getFilesByTopicId(topicId: Int): Flow<List<FileTbl>>

    @Query("SELECT * FROM file_tbl WHERE messageId = :messageId")
    fun getFilesByMessageId(messageId: Int): Flow<List<FileTbl>>

    @Query("SELECT * FROM file_tbl WHERE categoryId = :categoryId")
    fun getFilesByCategoryId(categoryId: Int): Flow<List<FileTbl>>
}
