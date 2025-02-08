package com.example.topics2.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.topics2.db.enitities.TopicTbl
import kotlinx.coroutines.flow.Flow


@Dao
interface TopicDao {


 @Insert
 suspend fun insertTopic(topic: TopicTbl)

 @Insert
 suspend fun insert(topic: TopicTbl): Long // used for test data

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

 @Query("SELECT * FROM topic_tbl WHERE categoryId = :topicId")
 fun getTopicById(topicId: Int): TopicTbl

 @Query("SELECT createTime FROM topic_tbl WHERE id = :topicId")
suspend fun getCreatedTimeByID(topicId: Int): Long

 @RawQuery
 fun checkpoint(query: SupportSQLiteQuery): Int

 @Query("SELECT EXISTS(SELECT 1 FROM topic_tbl WHERE name IN (:names) LIMIT 1)")
 suspend fun anyTopicsExist(names: List<String>): Boolean


 @Update
 suspend fun editTopic(updatedTopic: TopicTbl)
}