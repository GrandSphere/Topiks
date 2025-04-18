package com.GrandSphere.Topiks.ui.viewmodels

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
import com.GrandSphere.Topiks.ui.components.addTopic.chooseColorBasedOnLuminance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

// TODO Fix category add when adding message
// TODO Fix created time when  editing when adding message
class MessageViewModel (
    private val messageDao: MessageDao,
    private val topicDao: TopicDao,
    private val filesDao: FilesDao
): ViewModel() {

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage

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
    private val _topicFontColor = MutableStateFlow<Color>(Color.Black)
    val topicFontColor: StateFlow<Color> = _topicFontColor
    fun setTopicFontColor() {_topicFontColor.value = chooseColorBasedOnLuminance(topicColor.value)
        Log.d("QQWWEE: " , "CHANGED NOW: ${_topicFontColor.value}")
    }


    // Retrieve messages
    private val _messages = MutableStateFlow<List<MessageTbl>>(emptyList())
    val _messageIndexMap = mutableStateOf<Map<Int, Int>>(emptyMap())
    val messages: StateFlow<List<MessageTbl>> = _messages
    val messagesMap: MutableState<Map<Int,Int>> = _messageIndexMap

    // New state properties
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
        Log.d("QQEE This is the selected messages", _selectedMessageIds.value.toString())
    }
    fun toggleSelectAllMessages() {
        if (_selectedMessageIds.value.size < _messages.value.size) {
            _selectedMessageIds.value = _messageIndexMap.value.keys
        } else {
            _selectedMessageIds.value = emptySet()
        }
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
    /**
     * Navigates to the previous search result and updates the current search index.
     *
     * If at the start of the search results, wraps around to the last result.
     */
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
    /**
     * Updates the current search message index based on the search result count.
     *
     * Sets [currentSearchMessageIndex] to the index of the message corresponding to the current
     * search result, or -1 if no valid result is found.
     */
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
    /**
 * Confirms deletion of selected messages.
 *
     * Deletes the messages in [selectedMessageIds], clears the selection, and hides the delete dialog.
     */
    fun confirmDeleteSelectedMessages() {
        viewModelScope.launch {
            deleteMultipleMessages(_selectedMessageIds.value)
            _selectedMessageIds.value = emptySet()
            _multipleMessageSelected.value = false
            _showDeleteDialog.value = false
        }
    }
    fun cancelDeleteDialog() {
    _showDeleteDialog.value = false
}


    /**
     * Initializes the ViewModel for a specific topic and message.
     *
     * Collects messages for the given [topicId], sets the [topicColor], and scrolls to the specified
     * [messageId] if provided. If no [messageId] is provided, scrolls to the last message.
     *
     * @param topicId The ID of the topic to load messages for.
     * @param topicColor The color associated with the topic.
     * @param messageId The ID of the message to scroll to, or -1 to scroll to the last message.
     * @param topicFontColor The font color for the topic, calculated based on luminance.
     */
    fun initialize(topicId: Int, topicColor: Color, messageId: Int ) {
        setTopicId(topicId)
        setTopicColor(topicColor)
        setTopicFontColor()
        collectMessages(topicId)
    }


    /**
     * Updates the top bar icons and menu items based on the current state.
     *
     * Configures the top bar with appropriate icons and menu items depending on whether
     * multiple messages are being selected ([multipleMessageSelected]).
     *
     * @param topBarViewModel The ViewModel for managing the top bar.
     */
    fun updateTopBar(topBarViewModel: TopBarViewModel) {
        val searchIcon = CustomIcon(
            icon = Icons.Default.Search,
            onClick = {
                toggleSearch()
            },
            contentDescription = "Search"
        )

        if (_multipleMessageSelected.value) {
            Log.d("QQWWEERR: ", "Changed now")
            topBarViewModel.setCustomIcons(
                listOf(
                    CustomIcon(
                        icon = Icons.Default.SelectAll,
                        onClick =  { toggleSelectAllMessages() },
                        contentDescription = "Select All"
                    ),
                    CustomIcon(
                        icon = Icons.Default.ImportExport,
                        onClick = {
                            viewModelScope.launch { exportMessagesToPDF(_selectedMessageIds.value) }
                        },
                        contentDescription = "Export Selected Messages"
                    ),
                    CustomIcon(
                        icon = Icons.Default.Delete,
                        onClick = {
                            if (_selectedMessageIds.value.size > 0) {
                                _showDeleteDialog.value = true
                            }
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
                MenuItem(if (_isDeleteEnabled.value) "Disable Delete" else "Enable Delete") {
                    toggleDeleteEnabled()
                },
                MenuItem("Back") { /* Handled in Composable due to navController */ }
            )
        )
    }
    fun collectMessages(topicId: Int) {
        messageDao.getMessagesForTopic(topicId)
            .distinctUntilChanged()
            .onEach { messageList ->
                _messages.value = messageList
                _messagesContentById.value = messageList.associateBy({ it.id }, { it.content })
                // Generate HashMap
                _messageIndexMap.value = messageList
                    .mapIndexed { index, message -> message.id to index }
                    .toMap()
                createMessageSubset(messageList)
                // Reset search when messages change
                if (_searchResults.value?.isNotEmpty() == true) {
                    messageSearch(_searchQuery.value)
                }
            }.launchIn(viewModelScope)
    }
    // Function to reset the entire state of the ViewModel
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

    // For search results
    private val _searchResults = MutableLiveData<List<Message>>()
    val searchResults: LiveData<List<Message>> get() = _searchResults

    private val messageSearchHandler: MessageSearchHandler= MessageSearchHandler(emptyList())
    // Create subset for search
    private val _messageSubset = MutableStateFlow<List<Message>>(emptyList())
    private val _messageMap = MutableStateFlow<Map<Int, Message>>(emptyMap())
    fun createMessageSubset(messageList: List<MessageTbl>) {
        _messageSubset.value = messageList.map { Message(it.id, it.content) }
        // Create a Map for fast lookup by id
        val messageMap = _messageSubset.value.associateBy { it.id }
        _messageMap.value = messageMap

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



    private val _multipleMessageSelected = MutableStateFlow<Boolean>(false)
    val multipleMessageSelected: StateFlow<Boolean> get() = _multipleMessageSelected
    fun setMultipleMessageSelected(newState: Boolean) {
        _multipleMessageSelected.value =  newState
    }

    private val _messagesContentById = MutableStateFlow<Map<Int, String>>(emptyMap())
    fun getMessageContentById(messageId: Int): String? {
        return _messagesContentById.value[messageId]
    }

    fun clearSearchResult(){
        _searchResults.value = emptyList()
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
    // Delete Message
    suspend fun deleteMultipleMessages(messageIds: Set<Int>) {
        messageDao.deleteMessagesWithID(messageIds)
    }

    // Retrieve messages from all Topics, to search through
    private val _searchMessages = MutableStateFlow<List<MessageSearchContent>>(emptyList())
    val searchMessages: StateFlow<List<MessageSearchContent>> = _searchMessages
    fun collectSearchMessages() {
        messageDao.getSearchMessages()
            .distinctUntilChanged()
            .onEach { messageList ->
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
        filePaths .forEach { filePath ->
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

    fun clearToast() {
        _toastMessage.value = null
    }
    fun updateToast(toastMessage: String) {
        _toastMessage.value = toastMessage
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

    fun exportMessagesToPDF(messageIDs: Set<Int>) {
        Log.d("QQWWEE THIS IS THE MESSAGE TO EXPORT", "${messageIDs}")
        val toPdf = ToPDF()
        val contentById = _messagesContentById.value

        // Use the map to build a list of message contents based on the provided IDs
        val contentList = messageIDs.mapNotNull { id ->
            contentById[id]
        }
        val fileName:String = if(messageIDs.size > 1) topicId.value.toString() + "_multiple" else
            topicId.value.toString() + "_" + messageIDs.first().toString()
        Log.d("ViewModel", "Exporting ${contentList.size} messages to PDF using map lookup for IDs: $messageIDs")

        if (contentList.isEmpty()) {
            Log.w("ViewModel", "No messages found for the provided IDs: $messageIDs")
            updateToast("No Messages found to export")
            return
        }

        // Create the PDF using the ToPDF function
        val success: Boolean = toPdf.createPdfInDirectory(
            relativeDirectoryName = "Exports",
            fileName = fileName,
            contentList = contentList
        )
        updateToast(if (success) "Export successful" else "Export failed")
    }

    companion object {
        // Factory to ggycreate the ViewModel
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