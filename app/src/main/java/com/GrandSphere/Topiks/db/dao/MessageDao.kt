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
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.GrandSphere.Topiks.db.enitities.MessageTbl
import com.GrandSphere.Topiks.model.MessageSearchContent
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

    @Query("DELETE FROM message_tbl WHERE id IN (:messageIds)")
    suspend fun deleteMessagesWithID(messageIds: Set<Int>)

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

      @Insert
    suspend fun insertAll(messages: List<MessageTbl>) // used for test data

}
