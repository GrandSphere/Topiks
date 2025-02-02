package com.example.topics2.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
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

     @Query("""
        SELECT m.id, m.content, m.topicId, t.name AS topicName
        FROM message_tbl m
        INNER JOIN topic_tbl t ON m.topicId = t.id
        ORDER BY m.lastEditTime DESC
    """)
    fun getSearchMessages(): Flow<List<MessageSearchContent>>

    @RawQuery
    fun checkpoint(query: SupportSQLiteQuery): Int
}
