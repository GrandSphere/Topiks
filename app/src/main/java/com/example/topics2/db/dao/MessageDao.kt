package com.example.topics2.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.topics2.db.enitities.MessageTbl

@Dao
interface MessageDao {
    @Insert
    suspend fun insertMessage(message: MessageTbl)

    @Query("SELECT * FROM messages WHERE topicId = :topicId")
    suspend fun getMessagesForTopic(topicId: Int?): List<MessageTbl>

    @Query("DELETE FROM messages WHERE id = :messageId")
    suspend fun deleteMessage(messageId: Int)

    @Update
    suspend fun updateMessage(message: MessageTbl)

    @Query("DELETE FROM messages WHERE topicId = :topicId")
    suspend fun deleteMessagesForTopic(topicId: Int)
}
