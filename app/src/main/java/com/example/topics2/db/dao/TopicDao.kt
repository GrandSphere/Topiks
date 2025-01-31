package com.example.topics2.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.topics2.db.enitities.TopicTbl
import kotlinx.coroutines.flow.Flow


@Dao
interface TopicDao {


 @Insert
suspend  fun insertTopic(topic: TopicTbl)

 @Query("SELECT * FROM topic_tbl ORDER BY LastEditTime DESC")
 fun getAllTopics(): Flow<List<TopicTbl>>

 @Query("DELETE FROM topic_tbl WHERE id = :topicId")
 suspend fun deleteTopicById(topicId: Int)

 // Update the last modified date for a specific topic by topicId
 @Query("UPDATE topic_tbl SET LastEditTime = :lastEdit WHERE id = :topicId")
 suspend fun updateLastModifiedTopic(topicId: Int, lastEdit: Long)

 // selects the last 50 unique colours from data base to populate recent colours
 @Query("SELECT DISTINCT Colour FROM topic_tbl ORDER BY CreateTime DESC LIMIT 50")
 fun getDistinctColorsOrdered(): Flow<List<Int>>

 @Query("SELECT * FROM topic_tbl WHERE categoryId = :categoryId")
 fun getTopicsByCategoryId(categoryId: Int): Flow<List<TopicTbl>>

}

