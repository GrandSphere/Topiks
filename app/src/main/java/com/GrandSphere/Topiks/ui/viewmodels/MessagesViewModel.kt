/*
package com.GrandSphere.Topiks.ui.viewmodels

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
import com.GrandSphere.Topiks.utilities.copyFileToUserFolder
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

    // Toast messages/add
    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage

    // Focus control for textbox
    private val _toFocusTextbox = MutableStateFlow(false)
    val toFocusTextbox: StateFlow<Boolean> = _toFocusTextbox
    fun setToFocusTextbox(newValue: Boolean) { _toFocusTextbox.value = newValue }

    private val _toUnFocusTextbox = MutableStateFlow(false)
    val toUnFocusTextbox: StateFlow<Boolean> = _toUnFocusTextbox
    fun setToUnFocusTextbox(newValue: Boolean) { _toUnFocusTextbox.value = newValue }

    // Edit mode
    private val _bEditMode = MutableStateFlow(false)
    val bEditMode: StateFlow<Boolean> = _bEditMode
    fun setEditMode(newValue: Boolean) { _bEditMode.value = newValue }

    // Temporary message ID
    private val _tempMessageId = MutableStateFlow(-1)
    val tempMessageId: StateFlow<Int> = _tempMessageId
    fun setTempMessageId(newValue: Int) { _tempMessageId.value = newValue }

    // Topic colors
    private val _topicColor = MutableStateFlow(Color.Cyan)
    val topicColor: StateFlow<Color> = _topicColor
    fun setTopicColor(topicColor: Color) { _topicColor.value = topicColor }

    private val _topicFontColor = MutableStateFlow(Color.Cyan)
    val topicFontColor: StateFlow<Color> = _topicFontColor
    fun setTopicFontColor() {
        _topicFontColor.value = chooseColorBasedOnLuminance(topicColor.value)
        Log.d("QQWWEE", "CHANGED NOW: ${_topicFontColor.value}")
    }

    // Messages
    private val _messages = MutableStateFlow<List<MessageUiModel>>(emptyList())
    val messages: StateFlow<List<MessageUiModel>> = _messages
    private val _messageIndexMap = mutableStateOf<Map<Int, Int>>(emptyMap())
    val messagesMap: MutableState<Map<Int, Int>> = _messageIndexMap

    // Delete dialog
    private val _showDeleteDialog = MutableStateFlow(false)
    val showDeleteDialog: StateFlow<Boolean> = _showDeleteDialog
    fun setShowDeleteDialog(show: Boolean) { _showDeleteDialog.value = show }

    // Search states
    private val _isSearchActive = MutableStateFlow(false)
    val isSearchActive: StateFlow<Boolean> = _isSearchActive
    private val _requestSearchFocus = MutableStateFlow(false)
    val requestSearchFocus: StateFlow<Boolean> = _requestSearchFocus
    fun clearSearchFocusRequest() { _requestSearchFocus.value = false }

    private val _currentSearchNav = MutableStateFlow(0)
    val currentSearchNav: StateFlow<Int> = _currentSearchNav
    fun resetCurrentSearchNav(){_currentSearchNav.value = 0}

    private val _searchedMessageIndex = MutableStateFlow(-1)
    val searchedMessageIndex: StateFlow<Int> = _searchedMessageIndex
    fun resetSearchedMessageIndex(){_searchedMessageIndex.value = -1}

    // Delete enabled
    private val _isDeleteEnabled = MutableStateFlow(false)
    val isDeleteEnabled: StateFlow<Boolean> = _isDeleteEnabled
    fun toggleDeleteEnabled() { _isDeleteEnabled.value = !_isDeleteEnabled.value }

    // Selected messages
    private val _selectedMessageIds = MutableStateFlow<Set<Int>>(emptySet())
    val selectedMessageIds: StateFlow<Set<Int>> = _selectedMessageIds

    // Search query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    // Input bar states
    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText

    private val _selectedFiles = MutableStateFlow<List<Uri>>(emptyList())
    val selectedFiles: StateFlow<List<Uri>> = _selectedFiles

    private val _selectedFilesBeforeEdit = MutableStateFlow<List<Uri>>(emptyList())
    val selectedFilesBeforeEdit: StateFlow<List<Uri>> = _selectedFilesBeforeEdit

    // Search results
    private val _searchResults = MutableLiveData<List<Message>>()
    val searchResults: LiveData<List<Message>> get() = _searchResults

    private val messageSearchHandler = MessageSearchHandler(emptyList())
    private val _messageSubset = MutableStateFlow<List<Message>>(emptyList())
    private val _messageMap = MutableStateFlow<Map<Int, Message>>(emptyMap())

    private val _multipleMessageSelected = MutableStateFlow(false)
    val multipleMessageSelected: StateFlow<Boolean> get() = _multipleMessageSelected
    fun setMultipleMessageSelected(newState: Boolean) {
        _multipleMessageSelected.value = newState
    }

    private val _messagesContentById = MutableStateFlow<Map<Int, String>>(emptyMap())
    fun getMessageContentById(messageId: Int): String? {
        return _messagesContentById.value[messageId]
    }

    private val _topicID = MutableStateFlow(0)
    val topicId: StateFlow<Int> = _topicID
    fun setTopicId(newValue: Int) { _topicID.value = newValue }

    private val filePathMap = mutableMapOf<String, Int>()

    private val _searchMessages = MutableStateFlow<List<MessageSearchContent>>(emptyList())
    val searchMessages: StateFlow<List<MessageSearchContent>> = _searchMessages

    // Update input text
    fun updateInputText(newText: String) {
        _inputText.value = newText
    }

    // Add files to selected files
    fun addSelectedFiles(uris: List<Uri>?) {
        _selectedFiles.value = (_selectedFiles.value + (uris ?: emptyList())).distinct()
    }

    // Remove a file at index
    fun removeSelectedFile(index: Int) {
        _selectedFiles.value = _selectedFiles.value.toMutableList().apply { removeAt(index) }
    }

    // Initialize input bar state
    fun initializeInputBar() {
        _inputText.value = ""
        _selectedFiles.value = emptyList()
        _selectedFilesBeforeEdit.value = emptyList()
        _bEditMode.value = false
        _tempMessageId.value = -1
        _toFocusTextbox.value = true
    }

    // Enter edit mode for a message
    suspend fun enterEditMode(messageId: Int) {
        _tempMessageId.value = messageId
        _bEditMode.value = true
        _inputText.value = getMessageContentById(messageId) ?: ""
        _selectedFiles.value = getFilesByMessageId(messageId)
        _selectedFilesBeforeEdit.value = _selectedFiles.value
    }

    // Handle send button click (add or edit message)
    fun sendMessage(
        topicId: Int,
        context: Context,
        widthSetting: Int = 500,
        heightSetting: Int = 500
    ) {
        viewModelScope.launch {
            val content = _inputText.value.trim()
            val files = _selectedFiles.value
            if (content.isEmpty() && files.isEmpty()) {
                _toastMessage.value = "Message or file required"
                return@launch
            }

            if (_bEditMode.value && _tempMessageId.value > -1) {
                // Edit existing message
                editMessageWithFiles(
                    messageId = _tempMessageId.value,
                    topicId = topicId,
                    content = content,
                    priority = 0,
                    categoryId = 1,
                    type = 1,
                    context = context,
                    widthSetting = widthSetting,
                    heightSetting = heightSetting
                )
            } else {
                // Add new message
                addMessageWithFiles(
                    topicId = topicId,
                    content = content,
                    priority = 0,
                    type = 0,
                    categoryId = 1,
                    context = context,
                    widthSetting = widthSetting,
                    heightSetting = heightSetting
                )
            }

            // Reset state
            resetInputBar()
        }
    }

    // Clear input bar (long press on send button)
    fun clearInputBar() {
        _inputText.value = ""
        _selectedFiles.value = emptyList()
        _selectedFilesBeforeEdit.value = emptyList()
        _bEditMode.value = false
        _tempMessageId.value = -1
    }

    private suspend fun addMessageWithFiles(
        topicId: Int,
        content: String,
        priority: Int,
        type: Int,
        categoryId: Int,
        context: Context,
        widthSetting: Int,
        heightSetting: Int
    ) {
        val messageId = addMessage(topicId, content, priority, type, categoryId).toInt()
        addFilesToMessage(messageId, topicId, _selectedFiles.value, context, widthSetting, heightSetting)
    }



    private suspend fun editMessageWithFiles(
        messageId: Int,
        topicId: Int,
        content: String,
        priority: Int,
        categoryId: Int,
        type: Int,
        context: Context,
        widthSetting: Int,
        heightSetting: Int
    ) {
        editMessage(messageId, topicId, content, priority, categoryId, type)
        val (deletedFiles, addedFiles) = compareFileLists(_selectedFilesBeforeEdit.value, _selectedFiles.value)
        if (deletedFiles.isNotEmpty()) {
            val fileIdsToDelete = deletedFiles.mapNotNull { uri ->
                val filePath = uri.path
                filePath?.let { getIdForFilePath(it) }
            }
            deleteFiles(fileIdsToDelete)
        }
        if (addedFiles.isNotEmpty()) {
            addFilesToMessage(messageId, topicId, addedFiles, context, widthSetting, heightSetting)
        }
    }

    private suspend fun addFilesToMessage(
        messageId: Int,
        topicId: Int,
        files: List<Uri>,
        context: Context,
        widthSetting: Int,
        heightSetting: Int
    ) {
        files.forEach { uri ->
            val fileType = determineFileType(context, uri.toString())
            val (normalFilePath, thumbnailFilePath) = copyFileToUserFolder(
                context = context,
                currentUri = uri,
                directoryName = fileType,
                height = heightSetting,
                width = widthSetting
            )
            addFile(
                topicId = topicId,
                messageId = messageId,
                fileType = fileType,
                filePath = normalFilePath,
                description = "",
                iconPath = thumbnailFilePath,
                categoryId = 1
            )
        }
    }

    private fun compareFileLists(before: List<Uri>?, after: List<Uri>?): Pair<List<Uri>, List<Uri>> {
        val beforeList = before ?: emptyList()
        val afterList = after ?: emptyList()
        val deleted = beforeList - afterList.toSet()
        val added = afterList - beforeList.toSet()
        return deleted to added
    }

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
            _searchedMessageIndex.value = -1
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
            resetCurrentSearchNav()
            resetSearchedMessageIndex()
            return
        }
        val currentCount = _currentSearchNav.value
        if (currentCount < results.size) {
            _currentSearchNav.value = currentCount + 1
        } else {
            _currentSearchNav.value = 1
        }
        updateCurrentSearchMessage()
    }

    fun navigatePreviousSearchResult() {
        val results = _searchResults.value ?: emptyList()
        if (results.isEmpty()) {
            resetCurrentSearchNav()
            resetSearchedMessageIndex()
            return
        }
        val currentCount = _currentSearchNav.value
        if (currentCount > 1) {
            _currentSearchNav.value = currentCount - 1
        } else {
            _currentSearchNav.value = results.size
        }
        updateCurrentSearchMessage()
    }

    private fun updateCurrentSearchMessage() {
        val results = _searchResults.value ?: emptyList()
        val count = _currentSearchNav.value
        if (count in 1..results.size) {
            val messageId = results[count - 1].id
            val messageIndex = getMessageIndexFromID(messageId)
            _searchedMessageIndex.value = if (messageIndex >= 0) messageIndex else -1
        } else {
            resetSearchedMessageIndex()
            resetCurrentSearchNav()
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
                    Log.d("QQWWEE search active", "${_isSearchActive.value}")
                    Log.d("QQWWEE current index", "${_searchedMessageIndex.value}")
                    Log.d("QQWWEE nomral index", "${index}")
                    MessageUiModel(
                        id = msg.id,
                        messageContent = msg.content,
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
                            viewModelScope.launch {
                                enterEditMode(msg.id)
                            }
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

    fun createContentToDisplay(messageContent: String, id: Int): AnnotatedString {
        val messageIndex = getMessageIndexFromID(id)

        val annotatedContent = if (_isSearchActive.value && messageIndex == _searchedMessageIndex.value) {
            highlightSearchText(
                messageContent = messageContent,
                searchQuery = _searchQuery.value,
                topicColor = _topicColor.value,
                topicFontColor = _topicFontColor.value
            )
        } else {
            AnnotatedString(messageContent)
        }
        return annotatedContent
    }
    fun resetState() {
        _toFocusTextbox.value = false
        _toUnFocusTextbox.value = false
        _bEditMode.value = false
        _tempMessageId.value = -1
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
        _currentSearchNav.value = 0
        _searchedMessageIndex.value = -1
        _isDeleteEnabled.value = false
        _selectedMessageIds.value = emptySet()
        _inputText.value = ""
        _selectedFiles.value = emptyList()
        _selectedFilesBeforeEdit.value = emptyList()
    }

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

    fun clearSearchResult() {
        _searchResults.value = emptyList()
    }

    fun collectSearchMessages() {
        messageDao.getSearchMessages()
            .distinctUntilChanged()
            .onEach { messageList ->
                _searchMessages.value = messageList
            }.launchIn(viewModelScope)
    }

    suspend fun deleteMessage(messageId: Int) {
        messageDao.deleteMessagesWithID(messageId)
    }

    suspend fun deleteMultipleMessages(messageIds: Set<Int>) {
        messageDao.deleteMessagesWithID(messageIds)
        Log.d("MessageViewModel", "Deleted message IDs: $messageIds")
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

    private fun resetInputBar() {
        _inputText.value = ""
        _selectedFiles.value = emptyList()
        _selectedFilesBeforeEdit.value = emptyList()
        _bEditMode.value = false
        _tempMessageId.value = -1
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

*/