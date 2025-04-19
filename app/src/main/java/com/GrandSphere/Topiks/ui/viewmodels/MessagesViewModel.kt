package com.GrandSphere.Topiks.ui.viewmodels

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ImportExport
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.GrandSphere.Topiks.DbTopics
import com.GrandSphere.Topiks.db.dao.FilesDao
import com.GrandSphere.Topiks.db.dao.MessageDao
import com.GrandSphere.Topiks.db.dao.TopicDao
import com.GrandSphere.Topiks.db.enitities.MessageTbl
import com.GrandSphere.Topiks.db.entities.FileInfoWithIcon
import com.GrandSphere.Topiks.db.entities.FileTbl
import com.GrandSphere.Topiks.model.Message
import com.GrandSphere.Topiks.model.MessageSearchContent
import com.GrandSphere.Topiks.model.MessageSearchHandler
import com.GrandSphere.Topiks.model.ToPDF
import com.GrandSphere.Topiks.model.dataClasses.CustomIcon
import com.GrandSphere.Topiks.model.dataClasses.MessageUiModel
import com.GrandSphere.Topiks.ui.components.addTopic.chooseColorBasedOnLuminance
import com.GrandSphere.Topiks.utilities.determineFileType
import com.GrandSphere.Topiks.utilities.helper.highlightSearchText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class MessageViewModel(
    private val messageDao: MessageDao,
    private val topicDao: TopicDao,
    private val filesDao: FilesDao
) : ViewModel() {

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage

    private val _ToFocusTextbox = MutableStateFlow<Boolean>(false)
    val ToFocusTextbox: StateFlow<Boolean> = _ToFocusTextbox
    fun setToFocusTextbox(newValue: Boolean) { _ToFocusTextbox.value = newValue }

    private val _ToUnFocusTextbox = MutableStateFlow<Boolean>(false)
    val ToUnFocusTextbox: StateFlow<Boolean> = _ToUnFocusTextbox
    fun setToUnFocusTextbox(newValue: Boolean) { _ToUnFocusTextbox.value = newValue }

    private val _bEditMode = MutableStateFlow<Boolean>(false)
    val bEditMode: StateFlow<Boolean> = _bEditMode
    fun setEditMode(newValue: Boolean) { _bEditMode.value = newValue }

    private val _tempMessageId = MutableStateFlow<Int>(0)
    val tempMessageId: StateFlow<Int> = _tempMessageId
    fun setTempMessageId(newValue: Int) { _tempMessageId.value = newValue }

    private val _topicColor = MutableStateFlow<Color>(Color.Cyan)
    val topicColor: StateFlow<Color> = _topicColor
    fun setTopicColor(topicColor: Color) { _topicColor.value = topicColor }

    private val _topicFontColor = MutableStateFlow<Color>(Color.Cyan)
    val topicFontColor: StateFlow<Color> = _topicFontColor
    fun setTopicFontColor() {
        _topicFontColor.value = chooseColorBasedOnLuminance(topicColor.value)
        Log.d("QQWWEE", "CHANGED NOW: ${_topicFontColor.value}")
    }

    private val _messages = MutableStateFlow<List<MessageUiModel>>(emptyList())
    val messages: StateFlow<List<MessageUiModel>> = _messages
    private val _messageIndexMap = mutableStateOf<Map<Int, Int>>(emptyMap())
    val messagesMap: MutableState<Map<Int, Int>> = _messageIndexMap

    private val _showDeleteDialog = MutableStateFlow<Boolean>(false)
    val showDeleteDialog: StateFlow<Boolean> = _showDeleteDialog
    fun setShowDeleteDialog(show: Boolean) { _showDeleteDialog.value = show }

    private val _isSearchActive = MutableStateFlow<Boolean>(false)
    val isSearchActive: StateFlow<Boolean> = _isSearchActive
    private val _requestSearchFocus = MutableStateFlow<Boolean>(false)
    val requestSearchFocus: StateFlow<Boolean> = _requestSearchFocus
    fun clearSearchFocusRequest() { _requestSearchFocus.value = false }

    private val _searchResultCount = MutableStateFlow<Int>(0)
    val searchResultCount: StateFlow<Int> = _searchResultCount

    private val _currentSearchMessageIndex = MutableStateFlow<Int>(-1)
    val currentSearchMessageIndex: StateFlow<Int> = _currentSearchMessageIndex

    private val _isDeleteEnabled = MutableStateFlow<Boolean>(false)
    val isDeleteEnabled: StateFlow<Boolean> = _isDeleteEnabled
    fun toggleDeleteEnabled() { _isDeleteEnabled.value = !_isDeleteEnabled.value }

    private val _selectedMessageIds = MutableStateFlow<Set<Int>>(emptySet())
    val selectedMessageIds: StateFlow<Set<Int>> = _selectedMessageIds

    private val _searchQuery = MutableStateFlow<String>("")
    val searchQuery: StateFlow<String> = _searchQuery

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        if (query.isNotEmpty()) {
            messageSearch(query)
        } else {
            clearSearchResult()
        }
    }

    fun toggleSearch() {
        _isSearchActive.value = !_isSearchActive.value
        if (_isSearchActive.value) {
            _requestSearchFocus.value = true
            _currentSearchMessageIndex.value = -1
        } else {
            _requestSearchFocus.value = false
            _searchQuery.value = ""
            clearSearchResult()
        }
    }

    fun toggleMessageSelection(messageId: Int) {
        val currentSet = _selectedMessageIds.value
        _selectedMessageIds.value = if (messageId in currentSet) {
            currentSet - messageId
        } else {
            currentSet + messageId
        }
        Log.d("MessageViewModel", "Toggled message $messageId, Selected IDs: ${_selectedMessageIds.value}")
    }

    fun toggleSelectAllMessages() {
        if (_selectedMessageIds.value.size < _messages.value.size) {
            _selectedMessageIds.value = _messageIndexMap.value.keys
        } else {
            _selectedMessageIds.value = emptySet()
        }
        Log.d("MessageViewModel", "Select All toggled, Selected IDs: ${_selectedMessageIds.value}")
    }

    fun navigateNextSearchResult() {
        val results = _searchResults.value ?: emptyList()
        if (results.isEmpty()) {
            _searchResultCount.value = 0
            _currentSearchMessageIndex.value = -1
            return
        }
        val currentCount = _searchResultCount.value
        if (currentCount < results.size) {
            _searchResultCount.value = currentCount + 1
        } else {
            _searchResultCount.value = 1
        }
        updateCurrentSearchMessage()
    }

    fun navigatePreviousSearchResult() {
        val results = _searchResults.value ?: emptyList()
        if (results.isEmpty()) {
            _searchResultCount.value = 0
            _currentSearchMessageIndex.value = -1
            return
        }
        val currentCount = _searchResultCount.value
        if (currentCount > 1) {
            _searchResultCount.value = currentCount - 1
        } else {
            _searchResultCount.value = results.size
        }
        updateCurrentSearchMessage()
    }

    private fun updateCurrentSearchMessage() {
        val results = _searchResults.value ?: emptyList()
        val count = _searchResultCount.value
        if (count in 1..results.size) {
            val messageId = results[count - 1].id
            val messageIndex = getMessageIndexFromID(messageId)
            _currentSearchMessageIndex.value = if (messageIndex >= 0) messageIndex else -1
        } else {
            _currentSearchMessageIndex.value = -1
            _searchResultCount.value = 0
        }
    }

    fun confirmDeleteSelectedMessages() {
        viewModelScope.launch {
            deleteMultipleMessages(_selectedMessageIds.value)
            _selectedMessageIds.value = emptySet()
            _multipleMessageSelected.value = false
            _showDeleteDialog.value = false
            Log.d("MessageViewModel", "Deleted messages, Selected IDs cleared")
        }
    }

    fun cancelDeleteDialog() {
        _showDeleteDialog.value = false
    }

    fun initialize(topicId: Int, topicColor: Color, messageId: Int, context: Context) {
        setTopicId(topicId)
        setTopicColor(topicColor)
        setTopicFontColor()
        collectMessages(topicId, context)
    }

    fun updateTopBar(topBarViewModel: TopBarViewModel) {
        val searchIcon = CustomIcon(
            icon = Icons.Default.Search,
            onClick = { toggleSearch() },
            contentDescription = "Search"
        )

        if (_multipleMessageSelected.value) {
            topBarViewModel.setCustomIcons(
                listOf(
                    CustomIcon(
                        icon = Icons.Default.SelectAll,
                        onClick = { toggleSelectAllMessages() },
                        contentDescription = "Select All"
                    ),
                    CustomIcon(
                        icon = Icons.Default.ImportExport,
                        onClick = { viewModelScope.launch { exportMessagesToPDF(_selectedMessageIds.value) } },
                        contentDescription = "Export Selected Messages"
                    ),
                    CustomIcon(
                        icon = Icons.Default.Delete,
                        onClick = {
                            Log.d("MessageViewModel", "Delete clicked, Selected IDs: ${_selectedMessageIds.value}")
                            _showDeleteDialog.value = true
                        },
                        contentDescription = "Delete Selected Messages"

                    )
                )
            )
        } else {
            topBarViewModel.setCustomIcons(listOf(searchIcon))
        }

        topBarViewModel.setMenuItems(
            listOf(
                MenuItem("Search") { toggleSearch() },
                MenuItem("Select Messages") { setMultipleMessageSelected(!_multipleMessageSelected.value) },
                MenuItem(if (_isDeleteEnabled.value) "Disable Delete" else "Enable Delete") { toggleDeleteEnabled() },
                MenuItem("Back") { /* Handled in Composable due to navController */ }
            )
        )
    }

    fun collectMessages(topicId: Int, context: Context) {
        messageDao.getMessagesForTopic(topicId)
            .distinctUntilChanged()
            .onEach { messageList ->
                _messages.value = messageList.mapIndexed { index, msg ->
                    val files = filesDao.getFilesByMessageIdFlow(msg.id).map { fileList ->
                        fileList.map { FileInfoWithIcon(it.filePath, it.iconPath) }
                    }.first()
                    val pictures = files.filter { determineFileType(context, it.filePath) == "Image" }
                    val attachments = files.filter { determineFileType(context, it.filePath) != "Image" }.map { it.filePath }
                    MessageUiModel(
                        id = msg.id,
                        annotatedContent = if (_isSearchActive.value && index == _currentSearchMessageIndex.value) {
                            highlightSearchText(
                                messageContent = msg.content,
                                searchQuery = _searchQuery.value,
                                topicColor = _topicColor.value,
                                topicFontColor = _topicFontColor.value
                            )
                        } else {
                            AnnotatedString(msg.content)
                        },
                        timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(msg.createTime),
                        pictures = pictures,
                        attachments = attachments,
                        hasPictures = pictures.isNotEmpty(),
                        hasAttachments = attachments.isNotEmpty(),
                        isSelected = msg.id in _selectedMessageIds.value,
                        onDelete = { viewModelScope.launch { deleteMessage(msg.id) } },
                        onSelect = {
                            setMultipleMessageSelected(true)
                            toggleMessageSelection(msg.id)
                        },
                        onView = {
                            setTempMessageId(msg.id)
                        },
                        onEdit = {
                            setTempMessageId(msg.id)
                            setEditMode(true)
                        },
                        onExport = { viewModelScope.launch { exportMessagesToPDF(setOf(msg.id)) } }
                    )
                }
                _messagesContentById.value = messageList.associateBy({ it.id }, { it.content })
                _messageIndexMap.value = messageList
                    .mapIndexed { index, message -> message.id to index }
                    .toMap()
                createMessageSubset(messageList)
                if (_searchResults.value?.isNotEmpty() == true) {
                    messageSearch(_searchQuery.value)
                }
            }.launchIn(viewModelScope)
    }

    fun resetState() {
        _ToFocusTextbox.value = false
        _ToUnFocusTextbox.value = false
        _bEditMode.value = false
        _tempMessageId.value = 0
        _topicColor.value = Color.Cyan
        _topicFontColor.value = Color.Cyan
        _messages.value = emptyList()
        _messageIndexMap.value = emptyMap()
        _searchResults.value = emptyList()
        _messageSubset.value = emptyList()
        _messageMap.value = emptyMap()
        _messagesContentById.value = emptyMap()
        _topicID.value = 0
        filePathMap.clear()
        _searchMessages.value = emptyList()
        _multipleMessageSelected.value = false
        _toastMessage.value = null
        _showDeleteDialog.value = false
        _isSearchActive.value = false
        _requestSearchFocus.value = false
        _searchQuery.value = ""
        _searchResultCount.value = 0
        _currentSearchMessageIndex.value = -1
        _isDeleteEnabled.value = false
        _selectedMessageIds.value = emptySet()
    }

    private val _searchResults = MutableLiveData<List<Message>>()
    val searchResults: LiveData<List<Message>> get() = _searchResults

    private val messageSearchHandler: MessageSearchHandler = MessageSearchHandler(emptyList())
    private val _messageSubset = MutableStateFlow<List<Message>>(emptyList())
    private val _messageMap = MutableStateFlow<Map<Int, Message>>(emptyMap())

    fun createMessageSubset(messageList: List<MessageTbl>) {
        _messageSubset.value = messageList.map { Message(it.id, it.content) }
        val messageMap = _messageSubset.value.associateBy { it.id }
        _messageMap.value = messageMap
        messageSearchHandler.updateDataset(_messageSubset.value)
    }

    fun messageSearch(query: String, debounceTime: Long = 150L) {
        messageSearchHandler.search(query, debounceTime) { results ->
            _searchResults.postValue(results)
        }
    }

    fun getMessageIndexFromID(messageId: Int): Int {
        return _messageIndexMap.value[messageId] ?: -1
    }

    private val _multipleMessageSelected = MutableStateFlow<Boolean>(false)
    val multipleMessageSelected: StateFlow<Boolean> get() = _multipleMessageSelected
    fun setMultipleMessageSelected(newState: Boolean) {
        _multipleMessageSelected.value = newState
    }

    private val _messagesContentById = MutableStateFlow<Map<Int, String>>(emptyMap())
    fun getMessageContentById(messageId: Int): String? {
        return _messagesContentById.value[messageId]
    }

    fun clearSearchResult() {
        _searchResults.value = emptyList()
    }

    private val _topicID = MutableStateFlow<Int>(0)
    val topicId: StateFlow<Int> = _topicID
    fun setTopicId(newValue: Int) { _topicID.value = newValue }

    private val filePathMap = mutableMapOf<String, Int>()

    suspend fun deleteMessage(messageId: Int) {
        messageDao.deleteMessagesWithID(messageId)
    }

    suspend fun deleteMultipleMessages(messageIds: Set<Int>) {
        messageDao.deleteMessagesWithID(messageIds)
        Log.d("MessageViewModel", "Deleted message IDs: $messageIds")
    }

    private val _searchMessages = MutableStateFlow<List<MessageSearchContent>>(emptyList())
    val searchMessages: StateFlow<List<MessageSearchContent>> = _searchMessages

    fun collectSearchMessages() {
        messageDao.getSearchMessages()
            .distinctUntilChanged()
            .onEach { messageList ->
                _searchMessages.value = messageList
            }.launchIn(viewModelScope)
    }

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
            lastEditTime = timestamp,
            categoryId = categoryID
        )
        topicDao.updateLastModifiedTopic(topicId, timestamp)
        return messageDao.insertMessage(newMessage)
    }

    suspend fun getFilesByMessageId(messageId: Int): List<Uri> {
        val filePaths = filesDao.getFilesByMessageId(messageId)
        filePaths.forEach { filePath ->
            filePathMap[filePath.filePath] = filePath.id
        }
        return filePaths.map { Uri.parse(it.filePath) }
    }

    fun getIdForFilePath(filePath: String): Int? {
        return filePathMap[filePath]
    }

    suspend fun deleteFiles(fileIds: List<Int>) {
        if (fileIds.isNotEmpty()) {
            filesDao.deleteFilesByIds(fileIds)
        }
    }

    fun getFilesByMessageIdFlow(messageId: Int): Flow<List<FileInfoWithIcon>> {
        return filesDao.getFilesByMessageIdFlow(messageId)
            .map { fileList ->
                fileList.map { FileInfoWithIcon(it.filePath, it.iconPath) }
            }
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    fun updateToast(toastMessage: String) {
        _toastMessage.value = toastMessage
    }

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
        filesDao.insertFile(
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

    suspend fun editMessage(
        messageId: Int,
        topicId: Int,
        content: String,
        priority: Int,
        categoryId: Int,
        type: Int,
        messageTimestamp: Long = System.currentTimeMillis()
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

    fun exportMessagesToPDF(messageIDs: Set<Int>) {
        Log.d("QQWWEE THIS IS THE MESSAGE TO EXPORT", "$messageIDs")
        val toPdf = ToPDF()
        val contentById = _messagesContentById.value
        val contentList = messageIDs.mapNotNull { id -> contentById[id] }
        val fileName: String = if (messageIDs.size > 1) topicId.value.toString() + "_multiple" else
            topicId.value.toString() + "_" + messageIDs.first().toString()
        Log.d("ViewModel", "Exporting ${contentList.size} messages to PDF using map lookup for IDs: $messageIDs")
        if (contentList.isEmpty()) {
            Log.w("ViewModel", "No messages found for the provided IDs: $messageIDs")
            updateToast("No Messages found to export")
            return
        }
        val success: Boolean = toPdf.createPdfInDirectory(
            relativeDirectoryName = "Exports",
            fileName = fileName,
            contentList = contentList
        )
        updateToast(if (success) "Export successful" else "Export failed")
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                val myApplication = application as DbTopics
                return MessageViewModel(myApplication.messageDao, myApplication.topicDao, myApplication.filesDao) as T
            }
        }
    }
}