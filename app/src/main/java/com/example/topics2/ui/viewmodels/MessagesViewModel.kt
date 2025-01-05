package com.example.topics2.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.topics2.DbTopics
import com.example.topics2.model.dao.MessageDao
import com.example.topics2.model.enitities.MessageTbl


class MessageViewModel (private val messageDoa: MessageDao): ViewModel() {

    private fun createMessage(topicId: Int, content: String, priority: Int): MessageTbl {
        return MessageTbl(
            topicId = topicId,
            messageContent = content,
            messageTimestamp = System.currentTimeMillis(),
            messagePriority = priority
        )
    }

    // Function to add a new message for a specific topic
    suspend fun addMessage(topicId: Int, content: String, priority: Int) {
        val newMessage = createMessage(topicId, content, priority)
        messageDoa.insertMessage(newMessage) // Insert the message into the database
    }

    // Function to retrieve messages for a specific topic
    suspend fun getMessagesForTopic(topicId: Int): List<MessageTbl> {
        //Log.d("MessageController", "Fetching messages for topic ID: $topicId")
        return messageDoa.getMessagesForTopic(topicId)
    }

    suspend fun deleteMessagesForTopic(topicId: Int) {
        messageDoa.deleteMessagesForTopic(topicId)
    }

    companion object {
        // Factory to create the ViewModel
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                // Get the Application object from extras
                val application = checkNotNull(extras[APPLICATION_KEY])

                // Get the TopicDao from the Application class
                val myApplication = application as DbTopics
                return TopicViewModel(myApplication.topicDao) as T
            }
        }
    }
}
