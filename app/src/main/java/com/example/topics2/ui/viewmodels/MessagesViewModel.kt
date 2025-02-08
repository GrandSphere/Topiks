package com.example.topics2.ui.viewmodels

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import com.example.topics2.db.entities.FileInfo
import com.example.topics2.db.entities.FileInfoWithIcon
import com.example.topics2.db.entities.FilePath
import com.example.topics2.db.entities.FileTbl
import com.example.topics2.model.Message
import com.example.topics2.model.MessageSearchContent
import com.example.topics2.model.MessageSearchHandler
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

    private var _tempID: Int= -1
    fun settempID(message: Int) {
        _tempID = message
    }
    fun gettempID(): Int{
        return _tempID.also{_tempID=-1}
    }

    // Function to update focus state
    private val _ToFocusTextbox = MutableStateFlow<Boolean>(false)
    val ToFocusTextbox: StateFlow<Boolean> = _ToFocusTextbox
    fun setToFocusTextbox(newValue: Boolean) { _ToFocusTextbox.value = newValue }

    private val _ToUnFocusTextbox = MutableStateFlow<Boolean>(false)
    val ToUnFocusTextbox: StateFlow<Boolean> = _ToUnFocusTextbox
    fun setToUnFocusTextbox(newValue: Boolean) { _ToUnFocusTextbox.value = newValue }

    // States whether you are editing or sending
    private val _bEditMode = MutableStateFlow<Boolean>(false)
    val bEditMode: StateFlow<Boolean> = _bEditMode
    fun setEditMode(newValue: Boolean) { _bEditMode.value = newValue }

    // TempID, used only for editing a message
    private val _tempMessageId = MutableStateFlow<Int>(0)
    val tempMessageId: StateFlow<Int> = _tempMessageId
    fun setTempMessageId(newValue: Int) { _tempMessageId.value = newValue }

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
    private val _messageIndexMap = mutableStateOf<Map<Int, Int>>(emptyMap())
    val messages: StateFlow<List<MessageTbl>> = _messages
    fun collectMessages(topicId: Int) {
        messageDao.getMessagesForTopic(topicId).onEach { messageList ->
            _messages.value = messageList
            _messagesContentById.value = messageList.associateBy({ it.id }, { it.content })
           // Generate HashMap
           _messageIndexMap.value = messageList
            .mapIndexed { index, message -> message.id to index }
            .toMap()
            createMessageSubset(messageList)
        }.launchIn(viewModelScope)


    }


    // For search results
    private val _searchResults = MutableLiveData<List<Message>>()
    val searchResults: LiveData<List<Message>> get() = _searchResults

    private val messageSearchHandler: MessageSearchHandler= MessageSearchHandler(emptyList())
    // Create subset for search
    private val _messageSubset = MutableStateFlow<List<Message>>(emptyList())
    fun createMessageSubset(topicList: List<MessageTbl>) {
        _messageSubset.value = topicList.map { Message(it.id, it.content) }
        messageSearchHandler.updateDataset(_messageSubset.value)
    }

    fun messageSearch(query: String, debounceTime: Long = 150L) {
        messageSearchHandler.search(query, debounceTime) { results ->
            _searchResults.postValue(results)
        }
    }

    fun getMessageIndexFromID(messageID: Int):Int {
        return _messageIndexMap.value[messageID] ?: -1
    }

    private val _messagesContentById = MutableStateFlow<Map<Int, String>>(emptyMap())
    fun getMessageContentById(messageId: Int): String? {
        return _messagesContentById.value[messageId]
    }

    // TempID, used only for editing a message
    private val _topicID = MutableStateFlow<Int>(0)
    val topicId: StateFlow<Int> = _topicID
    fun setTopicId(newValue: Int) { _topicID.value = newValue }

    // HashMap to store filePath and corresponding id
    private val filePathMap = mutableMapOf<String, Int>()

    // Delete Message
    suspend fun deleteMessage(messageId: Int) {
        messageDao.deleteMessagesWithID(messageId)
    }

    // Retrieve messages from all Topics, to search through
    private val _searchMessages = MutableStateFlow<List<MessageSearchContent>>(emptyList())
    val searchMessages: StateFlow<List<MessageSearchContent>> = _searchMessages
    fun collectSearchMessages() {
        messageDao.getSearchMessages().onEach { messageList ->
            _searchMessages.value = messageList
        }.launchIn(viewModelScope)
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

    suspend fun getFilesByMessageId(messageId: Int): List<Uri> {
        val filePaths = filesDao.getFilesByMessageId(messageId)
        // Populate the HashMap and list of file paths
        filePaths.forEach { filePath ->
            filePathMap[filePath.filePath] = filePath.id
        }
        return filePaths.map { Uri.parse(it.filePath)}
    }
    // Function to get the ID of a filePath from the HashMap
    fun getIdForFilePath(filePath: String): Int? {
        return filePathMap[filePath]
    }

    suspend fun deleteFiles(fileIds: List<Int>) {
        if (fileIds.isNotEmpty()) {
            // Call DAO to delete the files by their IDs
            filesDao.deleteFilesByIds(fileIds)
        }
    }

    fun getFilesByMessageIdFlow(messageId: Int): Flow<List<FileInfoWithIcon>> {
        return filesDao.getFilesByMessageIdFlow(messageId)
            .map { fileList ->
                fileList.map { FileInfoWithIcon(it.filePath, it.iconPath) }
            }
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
        categoryId: Int,
        type: Int,
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
            categoryId = categoryId,
            type =  type
        )
        messageDao.updateMessage(editedMessage)
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