package com.example.topics2.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.topics2.db.enitities.MessageTbl
import com.example.topics2.model.MessageSearchContent
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageTbl): Long

    @Query("SELECT * FROM message_tbl")
    fun getAllMessages(): Flow<List<MessageTbl>>

    @Query("SELECT * FROM message_tbl WHERE topicId = :topicId")
    fun getMessagesForTopic(topicId: Int?): Flow<List<MessageTbl>>

    @Query("SELECT content FROM message_tbl WHERE id = :messageID")
    suspend fun getMessageWithID(messageID: Int): String

    @Query("DELETE FROM message_tbl WHERE topicId = :topicId")
    suspend fun deleteMessagesForTopic(topicId: Int)

    @Query("DELETE FROM message_tbl WHERE id = :messageID")
    suspend fun deleteMessagesWithID(messageID: Int)

    @Update
    suspend fun updateMessage(message: MessageTbl)

    // GET MESSAGE ID, CONTENT AND TOPIC ID FOR SEARCH
    @Query("SELECT id, content, topicId FROM message_tbl ORDER BY lastEditTime DESC")
    fun getSearchMessages(): List<MessageSearchContent>




}
