package com.example.topics2.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.topics2.db.enitities.TopicTbl


@Dao
interface TopicDao {

    @Insert
    suspend fun insertTopic(topic: TopicTbl)

    @Query("SELECT * FROM topics ORDER BY topicLastEdit DESC")
    suspend fun getAllTopics(): List<TopicTbl>

    @Query("DELETE FROM topics WHERE topicID = :topicId")
    suspend fun deleteTopicById(topicId: Int)

    // Update the last modified date for a specific topic by topicId
    @Query("UPDATE topics SET topicLastEdit = :lastEdit WHERE topicId = :topicId")
    suspend fun updateLastModified(topicId: Int, lastEdit: Long)


    @Query("DELETE FROM messages WHERE topicId = :topicId")
    suspend fun deleteMessagesForTopic(topicId: Int)
}
