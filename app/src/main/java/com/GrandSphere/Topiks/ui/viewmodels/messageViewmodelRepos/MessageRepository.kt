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

package com.GrandSphere.Topiks.ui.viewmodels.messageViewmodelRepos

import com.GrandSphere.Topiks.db.enitities.MessageTbl
import com.GrandSphere.Topiks.model.MessageSearchContent
import kotlinx.coroutines.flow.Flow

/**
 * Handles database stuff for messages, like adding, editing, deleting, and fetching them.
 */
interface MessageRepository {
    /** Add a new message to the database */
    suspend fun addMessage(
        topicId: Int,
        content: String,
        priority: Int,
        type: Int,
        categoryID: Int
    ): Long

    /** Edit an existing message */
    suspend fun editMessage(
        messageId: Int,
        topicId: Int,
        content: String,
        priority: Int,
        categoryId: Int,
        type: Int,
        messageTimestamp: Long
    )

    /** Delete a single message by ID */
    suspend fun deleteMessage(messageId: Int)

    /** Delete multiple messages by their IDs */
    suspend fun deleteMultipleMessages(messageIds: Set<Int>)

    /** Get all messages for a specific topic */
    fun getMessagesForTopic(topicId: Int): Flow<List<MessageTbl>>

    /** Get messages for search purposes */
    fun getSearchMessages(): Flow<List<MessageSearchContent>>
}