package com.example.topics2.ui.viewmodels

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

    // Function to update focus state
    private val _ToFocusTextbox = MutableStateFlow<Boolean>(false)
    val ToFocusTextbox: StateFlow<Boolean> = _ToFocusTextbox
    fun setToFocusTextbox(newValue: Boolean) { _ToFocusTextbox.value = newValue }

    // States whether file picker is done
    private val _showPicker = MutableStateFlow<Boolean>(false)
    val showPicker: StateFlow<Boolean> = _showPicker
    fun setShowPicker(newValue: Boolean) { _showPicker.value = newValue }

    // States whether reached the end of the SelectFileWithPicker function, meaning validURI set
    private val _filePicked = MutableStateFlow<Boolean>(false)
    val filePicked: StateFlow<Boolean> = _filePicked
    fun setfilePicked(newValue: Boolean) { _filePicked.value = newValue }

    // File Source URI for file imports
    private val _fileURI = MutableStateFlow<String>("")
    val fileURI: StateFlow<String> = _fileURI
    fun setURI(newURI: String) { _fileURI.value = newURI }

    // File Name for file imports
    private val _fileName = MutableStateFlow<String>("")
    val fileName: StateFlow<String> = _fileName
    fun setfileName(newFileName: String) { _fileName.value = newFileName }

    // File destination URI for file imports
    private val _destURI = MutableStateFlow<String>("")
    val destURI: StateFlow<String> = _destURI
    fun setdestURI(newURI: String) { _destURI.value = newURI }

    private val _ToUnFocusTextbox = MutableStateFlow<Boolean>(false)
    val ToUnFocusTextbox: StateFlow<Boolean> = _ToUnFocusTextbox
    fun setToUnFocusTextbox(newValue: Boolean) { _ToUnFocusTextbox.value = newValue }

    // States whether you are editing or sending
    private val _amEditing = MutableStateFlow<Boolean>(false)
    val amEditing: StateFlow<Boolean> = _amEditing
    fun setAmEditing(newValue: Boolean) { _amEditing.value = newValue }

    // TempID, used only for editing a message
    private val _tempMessageId = MutableStateFlow<Int>(0)
    val tempMessageId: StateFlow<Int> = _tempMessageId
    fun setTempMessageId(newValue: Int) { _tempMessageId.value = newValue }

    //Temp message, used only for editing a message
    private val _tempMessage = MutableStateFlow<String>("")
    val tempMessage: StateFlow<String> = _tempMessage
    fun setTempMessage(newCategory: String) {_tempMessage.value = newCategory}

    // Retrieve messages
    private val _messages = MutableStateFlow<List<MessageTbl>>(emptyList())
    val messages: StateFlow<List<MessageTbl>> = _messages
    fun fetchMessages(topicId: Int?) { viewModelScope.launch { _messages.value = messageDao.getMessagesForTopic(topicId) } }



    // Delete Message
    suspend fun deleteMessage(messageId: Int, topicId: Int?) {
        messageDao.deleteMessage(messageId)
        fetchMessages(topicId)
    }

    // Add Message
    suspend fun addMessage(
        topicId: Int?,
        content: String,
        priority: Int,
        filePath: String = "",
        fileType: Int = 0
    ) {
        val newMessage = MessageTbl(
            topicId = topicId,
            messageContent = content,
            messagePriority = priority,
            filePath = filePath,
            fileType = fileType,
            messageTimestamp = System.currentTimeMillis()
            )
        messageDao.insertMessage(newMessage) // Insert the message into the database
        fetchMessages(topicId)
    }

    //Edit Message
    suspend fun editMessage(
        messageId: Int,
        topicId: Int?, content: String, priority: Int,
        messageTimestamp: Long = System.currentTimeMillis() )
    {
        val editedMessage = MessageTbl(
            id = messageId,
            topicId = topicId,
            messageContent = content,
            messageTimestamp = messageTimestamp,
            messagePriority = priority,
        )
        messageDao.updateMessage(editedMessage)
        fetchMessages(topicId)
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
}