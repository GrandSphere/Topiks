package com.example.topics2.ui.viewmodels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.topics2.DbTopics
import com.example.topics2.db.dao.FilesDao
import com.example.topics2.db.dao.MessageDao
import com.example.topics2.db.dao.TopicDao
import com.example.topics2.db.enitities.MessageTbl
import com.example.topics2.db.entities.FileTbl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

// TODO Fix category add when adding message
// TODO Fix created time when  editing when adding message
class MessageViewModel (
    private val messageDao: MessageDao,
    private val topicDao: TopicDao,
    private val filesDao: FilesDao
): ViewModel() {

    // Function to update focus state
    private val _ToFocusTextbox = MutableStateFlow<Boolean>(false)
    val ToFocusTextbox: StateFlow<Boolean> = _ToFocusTextbox
    fun setToFocusTextbox(newValue: Boolean) { _ToFocusTextbox.value = newValue }

    // File Source URI for file imports
    private val _fileURI = MutableStateFlow<String>("")
    val fileURI: StateFlow<String> = _fileURI
    fun setFileURI(newURI: String) { _fileURI.value = newURI }

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

    // ImagePaths, used for messageBubble and showmore
    private val _imagePaths = MutableStateFlow<List<String>>(listOf(""))
    val imagePaths: StateFlow<List<String>> = _imagePaths
    fun setImagePaths(imagePaths: List<String>) {_imagePaths.value = imagePaths}

    // Topic Color
    private val _topicColor = MutableStateFlow<Color>(Color.Cyan)
    val topicColor: StateFlow<Color> = _topicColor
    fun setTopicColor(topicColor: Color) {_topicColor.value = topicColor}

    // Topic Font Color
    private val _topicFontColor = MutableStateFlow<Color>(Color.Cyan)
    val topicFontColor: StateFlow<Color> = _topicColor
    fun setTopicFontColor(topicColor: Color) {_topicFontColor.value = topicColor}



    // Retrieve messages
    private val _messages = MutableStateFlow<List<MessageTbl>>(emptyList())
    val messages: StateFlow<List<MessageTbl>> = _messages
    fun collectMessages(topicId: Int) {
        messageDao.getMessagesForTopic(topicId).onEach { messageList ->_messages.value = messageList
        }.launchIn(viewModelScope)
    }

    // Delete Message
    suspend fun deleteMessage(messageId: Int, topicId: Int?) {
        messageDao.deleteMessagesWithID(messageId)

    }

    // Add Message
    suspend fun addMessage(
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
            lastEditTime =  timestamp,
            categoryId = categoryID
            )
        topicDao.updateLastModifiedTopic(topicId, timestamp)
        return messageDao.insertMessage(newMessage) // Insert the message into the database
    }

    fun getFilesByMessageId(messageId: Int): Flow<List<String>> {
        return filesDao.getFilesByMessageId(messageId)
            .map { fileList -> fileList.map { it.filePath } }
    }

    // Add File to File_tbl
    suspend fun addFile(
        topicId: Int,
        messageId: Int,
        fileType: String,
        filePath: String,
        description: String,
        iconPath: String = "",
        categoryId: Int,
        createTime: Long = System.currentTimeMillis()

    ) {
        val value = filesDao.insertFile(
            FileTbl(
                topicId = topicId,
                messageId = messageId,
                fileType = fileType,
                filePath = filePath,
                description = description,
                iconPath = iconPath,
                categoryId = categoryId,
                createTime = createTime
        )

        )
    }

    //Edit Message
    suspend fun editMessage(
        messageId: Int,
        topicId: Int,
        content: String,
        priority: Int,
        messageTimestamp: Long = System.currentTimeMillis()
    )
    {
        val editedMessage = MessageTbl(
            id = messageId,
            topicId = topicId,
            content = content,
            createTime = messageTimestamp,
            priority = priority,
            lastEditTime =  messageTimestamp,
            categoryId = 1,
            type =  1
        )
        messageDao.updateMessage(editedMessage)
      //  fetchMessages(topicId)
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
                return MessageViewModel(myApplication.messageDao, myApplication.topicDao, myApplication.filesDao) as T
            }
        }
    }
}