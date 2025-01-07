package com.example.topics2.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.topics2.db.enitities.MessageTbl
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageTbl): Long

    @Query("SELECT * FROM message_tbl")
    fun getAllMessages(): Flow<List<MessageTbl>>

    @Query("SELECT * FROM message_tbl WHERE topicId = :topicId")
    suspend fun getMessagesForTopic(topicId: Int?): Flow<List<MessageTbl>>

    @Delete
    suspend fun deleteMessage(message: MessageTbl)

    @Update
    suspend fun updateMessage(message: MessageTbl)

}
