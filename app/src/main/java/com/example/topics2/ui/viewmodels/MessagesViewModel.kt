package com.example.topics2.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.topics2.DbTopics
import com.example.topics2.db.dao.MessageDao
import com.example.topics2.db.enitities.MessageTbl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class MessageViewModel (private val messageDao: MessageDao): ViewModel() {

    // Retrieve messages
    private val _messages = MutableStateFlow<List<MessageTbl>>(emptyList())
    val messages: StateFlow<List<MessageTbl>> = _messages
    fun fetchMessages(topicId: Int?) { viewModelScope.launch { _messages.value = messageDao.getMessagesForTopic(topicId) } }


    // Delete Message
    suspend fun deleteMessagesForTopic(topicId: Int) {
        messageDao.deleteMessagesForTopic(topicId)
        fetchMessages(topicId)
    }

    // Add Message
    suspend fun addMessage(topicId: Int?, content: String, priority: Int) {
        val newMessage = createMessage(topicId, content, priority)
        messageDao.insertMessage(newMessage) // Insert the message into the database
        fetchMessages(topicId)
    }

    // New Message
    private fun createMessage(topicId: Int?, content: String, priority: Int): MessageTbl {
        return MessageTbl(
            topicId = topicId,
            messageContent = content,
            messageTimestamp = System.currentTimeMillis(),
            messagePriority = priority
        )
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
                return MessageViewModel(myApplication.messageDao) as T
            }
        }
    }


// fun insertTestMessages() {

//        // Inserting 20 test messages with different content
//        val testMessages = (1..20).map { index ->
//            MessageTbl(
//                topicId = 2,  // Example Topic ID (you can change this)
//                messageContent = "Otherr chat #$index",
//                messageTimestamp = System.currentTimeMillis() + index * 1000L,  // Add a small delay to each message timestamp
//                messagePriority = (index % 3) + 1  // Random priority (1, 2, or 3)
//            )
//        }
//
//        // Launching a coroutine to insert data into the database on a background thread
//        viewModelScope.launch {
//            testMessages.forEach { message ->
//                messageDao.insertMessage(message)
//            }
//            //loadMessagesByTopicId(1)  // Reload the messages to update the UI (if needed)
//        }
//    }





}


