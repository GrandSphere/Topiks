/*******************************************************************************
 * Copyright (C) <2025> GrandSphere <GrandSphereStudios@protonmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.GrandSphere.Topiks.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.GrandSphere.Topiks.db.enitities.TopicTbl
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


 @Query("SELECT iconPath FROM topic_tbl WHERE id = :topicId")
 suspend fun getIconPathById(topicId: Int): String

 @RawQuery
 fun checkpoint(query: SupportSQLiteQuery): Int

 @Query("SELECT EXISTS(SELECT 1 FROM topic_tbl WHERE name IN (:names) LIMIT 1)")
 suspend fun anyTopicsExist(names: List<String>): Boolean


 @Update
 suspend fun editTopic(updatedTopic: TopicTbl)
}