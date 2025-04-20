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

import com.GrandSphere.Topiks.db.dao.MessageDao
import com.GrandSphere.Topiks.db.dao.TopicDao
import com.GrandSphere.Topiks.db.enitities.MessageTbl
import com.GrandSphere.Topiks.model.MessageSearchContent
import kotlinx.coroutines.flow.Flow

class MessageRepositoryImpl(
    private val messageDao: MessageDao,
    private val topicDao: TopicDao
) : MessageRepository {

    override suspend fun addMessage(
        topicId: Int,
        content: String,
        priority: Int,
        type: Int,
        categoryID: Int
    ): Long {
        val timestamp = System.currentTimeMillis()
        val newMessage = MessageTbl(
            topicId = topicId,
            content = content,
            priority = priority,
            type = type,
            createTime = timestamp,
            lastEditTime = timestamp,
            categoryId = categoryID
        )
        topicDao.updateLastModifiedTopic(topicId, timestamp)
        return messageDao.insertMessage(newMessage)
    }

    override suspend fun editMessage(
        messageId: Int,
        topicId: Int,
        content: String,
        priority: Int,
        categoryId: Int,
        type: Int,
        messageTimestamp: Long
    ) {
        val editedMessage = MessageTbl(
            id = messageId,
            topicId = topicId,
            content = content,
            createTime = messageTimestamp,
            priority = priority,
            lastEditTime = messageTimestamp,
            categoryId = categoryId,
            type = type
        )
        messageDao.updateMessage(editedMessage)
    }

    override suspend fun deleteMessage(messageId: Int) {
        messageDao.deleteMessagesWithID(messageId)
    }

    override suspend fun deleteMultipleMessages(messageIds: Set<Int>) {
        messageDao.deleteMessagesWithID(messageIds)
    }

    override fun getMessagesForTopic(topicId: Int): Flow<List<MessageTbl>> {
        return messageDao.getMessagesForTopic(topicId)
    }

    override fun getSearchMessages(): Flow<List<MessageSearchContent>> {
        return messageDao.getSearchMessages()
    }
}
