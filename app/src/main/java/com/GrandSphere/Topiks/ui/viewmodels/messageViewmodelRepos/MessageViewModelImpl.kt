package com.GrandSphere.Topiks.ui.viewmodels.messageViewmodelRepos

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ImportExport
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.GrandSphere.Topiks.DbTopics
import com.GrandSphere.Topiks.db.enitities.MessageTbl
import com.GrandSphere.Topiks.db.entities.FileInfoWithIcon
import com.GrandSphere.Topiks.model.Message
import com.GrandSphere.Topiks.model.MessageSearchContent
import com.GrandSphere.Topiks.model.dataClasses.CustomIcon
import com.GrandSphere.Topiks.model.dataClasses.MessageUiModel
import com.GrandSphere.Topiks.ui.components.addTopic.chooseColorBasedOnLuminance
import com.GrandSphere.Topiks.ui.viewmodels.MenuItem
import com.GrandSphere.Topiks.ui.viewmodels.MessageViewModelContract
import com.GrandSphere.Topiks.ui.viewmodels.TopBarViewModel
import com.GrandSphere.Topiks.utilities.determineFileType
import com.GrandSphere.Topiks.utilities.helper.highlightSearchText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class MessageViewModelImpl(
    private val messageRepository: MessageRepository,
    private val fileRepository: FileRepository,
    private val searchRepository: SearchRepository,
    private val exportRepository: ExportRepository
) : ViewModel(), MessageViewModelContract {

    // UI State
    override val toastMessage = MutableStateFlow<String?>(null)
    override val toFocusTextbox = MutableStateFlow(false)
    override val toUnFocusTextbox = MutableStateFlow(false)
    override val bEditMode = MutableStateFlow(false)
    override var tempMessageId: Int = -1
    override val topicColor = MutableStateFlow(Color.Cyan)
    override val topicFontColor = MutableStateFlow(Color.Cyan)
    override val messages = MutableStateFlow<List<MessageUiModel>>(emptyList())
    private val _messageIndexMap = mutableStateOf<Map<Int, Int>>(emptyMap())
    override val showDeleteDialog = MutableStateFlow(false)
    override val isSearchActive = MutableStateFlow(false)
    override val requestSearchFocus = MutableStateFlow(false)
    override val currentSearchNav = MutableStateFlow(0)
    override val searchedMessageIndex = MutableStateFlow(-3)
    override val isDeleteEnabled = MutableStateFlow(false)
    override val selectedMessageIds = MutableStateFlow<Set<Int>>(emptySet())
    override val searchQuery = MutableStateFlow("")
    override val inputText = MutableStateFlow("")
    override val selectedFiles = MutableStateFlow<List<Uri>>(emptyList())
    override val selectedFilesBeforeEdit = MutableStateFlow<List<Uri>>(emptyList())
    override val searchResults: LiveData<List<Message>> = searchRepository.searchResults
    override val multipleMessageSelected = MutableStateFlow(false)
    private val _messagesContentById = MutableStateFlow<Map<Int, String>>(emptyMap())
    override val topicId = MutableStateFlow(0)
    override val searchMessages = MutableStateFlow<List<MessageSearchContent>>(emptyList())

    // UI Actions
    override fun setToFocusTextbox(newValue: Boolean) {
        toFocusTextbox.value = newValue
    }

    override fun setToUnFocusTextbox(newValue: Boolean) {
        toUnFocusTextbox.value = newValue
    }

    override fun setEditMode(newValue: Boolean) {
        bEditMode.value = newValue
    }

    override fun setTempMessageID(newValue: Int) {
        tempMessageId = newValue
    }

    override fun getTempMessageID(): Int {
        return tempMessageId
    }

    override fun setTopicColor(topicColor: Color) {
        this.topicColor.value = topicColor
    }

    override fun setTopicFontColor() {
        topicFontColor.value = chooseColorBasedOnLuminance(topicColor.value)
    }

    override fun setShowDeleteDialog(show: Boolean) {
        showDeleteDialog.value = show
    }

    override fun clearSearchFocusFocus() {
        requestSearchFocus.value = false
    }

    override fun resetCurrentSearchNav() {
        currentSearchNav.value = 0
    }

    override fun resetSearchedMessageIndex() {
        searchedMessageIndex.value = -1
    }

    override fun toggleDeleteEnabled() {
        isDeleteEnabled.value = !isDeleteEnabled.value
    }

    override fun setMultipleMessageSelected(newState: Boolean) {
        multipleMessageSelected.value = newState
    }

    override fun setTopicId(newValue: Int) {
        topicId.value = newValue
    }

    override fun updateInputText(newText: String) {
        inputText.value = newText
    }

    override fun addSelectedFiles(uris: List<Uri>?) {
        selectedFiles.value = (selectedFiles.value + (uris ?: emptyList())).distinct()
    }

    override fun removeSelectedFile(index: Int) {
        selectedFiles.value = selectedFiles.value.toMutableList().apply { removeAt(index) }
    }

    override fun initializeInputBar() {
        inputText.value = ""
        selectedFiles.value = emptyList()
        selectedFilesBeforeEdit.value = emptyList()
        bEditMode.value = false
        tempMessageId = -1
        toFocusTextbox.value = true
    }

    override fun sendMessage(
        topicId: Int,
        context: Context,
        widthSetting: Int,
        heightSetting: Int
    ) {
        viewModelScope.launch {
            val content = inputText.value.trim()
            val files = selectedFiles.value
            if (content.isEmpty() && files.isEmpty()) {
                toastMessage.value = "Message or file required"
                return@launch
            }

            if (bEditMode.value && tempMessageId > -1) {
                editMessageWithFiles(
                    messageId = tempMessageId,
                    topicId = topicId,
                    content = content,
                    context = context,
                    widthSetting = widthSetting,
                    heightSetting = heightSetting
                )
            } else {
                addMessageWithFiles(
                    topicId = topicId,
                    content = content,
                    context = context,
                    widthSetting = widthSetting,
                    heightSetting = heightSetting
                )
            }

            resetInputBar()
        }
    }

    override fun clearInputBar() {
        inputText.value = ""
        selectedFiles.value = emptyList()
        selectedFilesBeforeEdit.value = emptyList()
        bEditMode.value = false
        tempMessageId = -1
    }

    override fun updateSearchQuery(query: String) {
        searchQuery.value = query
        if (query.isNotEmpty()) {
            searchRepository.searchMessages(query)
        } else {
            searchRepository.clearSearchResults()
        }
    }

    override fun toggleSearch() {
        isSearchActive.value = !isSearchActive.value
        if (isSearchActive.value) {
            requestSearchFocus.value = true
            searchedMessageIndex.value = -1
        } else {
            requestSearchFocus.value = false
            searchQuery.value = ""
            searchRepository.clearSearchResults()
        }
    }

    override fun toggleMessageSelection(messageId: Int) {
        val currentSet = selectedMessageIds.value
        selectedMessageIds.value = if (messageId in currentSet) {
            currentSet - messageId
        } else {
            currentSet + messageId
        }
        Log.d("MessageViewModel", "Toggled message $messageId, Selected IDs: ${selectedMessageIds.value}")
    }

    override fun toggleSelectAllMessages() {
        if (selectedMessageIds.value.size < messages.value.size) {
            selectedMessageIds.value = _messageIndexMap.value.keys
        } else {
            selectedMessageIds.value = emptySet()
        }
        Log.d("MessageViewModel", "Select All toggled, Selected IDs: ${selectedMessageIds.value}")
    }

    override fun navigateNextSearchResult() {
        val results = searchResults.value ?: emptyList()
        if (results.isEmpty()) {
            resetCurrentSearchNav()
            resetSearchedMessageIndex()
            return
        }
        val currentCount = currentSearchNav.value
        if (currentCount < results.size) {
            currentSearchNav.value = currentCount + 1
        } else {
            currentSearchNav.value = 1
        }
        updateCurrentSearchMessage()
    }

    override fun navigatePreviousSearchResult() {
        val results = searchResults.value ?: emptyList()
        if (results.isEmpty()) {
            resetCurrentSearchNav()
            resetSearchedMessageIndex()
            return
        }
        val currentCount = currentSearchNav.value
        if (currentCount > 1) {
            currentSearchNav.value = currentCount - 1
        } else {
            currentSearchNav.value = results.size
        }
        updateCurrentSearchMessage()
    }

    override fun confirmDeleteSelectedMessages() {
        viewModelScope.launch {
            messageRepository.deleteMultipleMessages(selectedMessageIds.value)
            selectedMessageIds.value = emptySet()
            multipleMessageSelected.value = false
            showDeleteDialog.value = false
            Log.d("MessageViewModel", "Deleted messages, Selected IDs cleared")
        }
    }

    override fun cancelDeleteDialog() {
        showDeleteDialog.value = false
    }

    override fun initialize(topicId: Int, topicColor: Color, messageId: Int, context: Context) {
        setTopicId(topicId)
        setTopicColor(topicColor)
        setTopicFontColor()
        collectMessages(topicId, context)
    }

    override fun updateTopBar(topBarViewModel: TopBarViewModel) {
        val searchIcon = CustomIcon(
            icon = Icons.Default.Search,
            onClick = { toggleSearch() },
            contentDescription = "Search"
        )

        if (multipleMessageSelected.value) {
            topBarViewModel.setCustomIcons(
                listOf(
                    CustomIcon(
                        icon = Icons.Default.SelectAll,
                        onClick = { toggleSelectAllMessages() },
                        contentDescription = "Select All"
                    ),
                    CustomIcon(
                        icon = Icons.Default.ImportExport,
                        onClick = { viewModelScope.launch { exportRepository.exportMessagesToPDF(selectedMessageIds.value) } },
                        contentDescription = "Export Selected Messages"
                    ),
                    CustomIcon(
                        icon = Icons.Default.Delete,
                        onClick = {
                            Log.d("MessageViewModel", "Delete clicked, Selected IDs: ${selectedMessageIds.value}")
                            showDeleteDialog.value = true
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
                MenuItem("Select Messages") { setMultipleMessageSelected(!multipleMessageSelected.value) },
                MenuItem(if (isDeleteEnabled.value) "Disable Delete" else "Enable Delete") { toggleDeleteEnabled() },
                MenuItem("Back") { /* Handled in Composable due to navController */ }
            )
        )
    }

    override fun collectMessages(topicId: Int, context: Context) {
        messageRepository.getMessagesForTopic(topicId)
            .distinctUntilChanged()
            .onEach { messageList ->
                messages.value = messageList.mapIndexed { index, msg ->
                    val files = fileRepository.getFilesByMessageIdFlow(msg.id).map { fileList ->
                        fileList.map { FileInfoWithIcon(it.filePath, it.iconPath) }
                    }.first()
                    val pictures = files.filter { determineFileType(context, it.filePath) == "Image" }
                    val attachments = files.filter { determineFileType(context, it.filePath) != "Image" }.map { it.filePath }
                    MessageUiModel(
                        id = msg.id,
                        messageContent = msg.content,
                        timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(msg.createTime),
                        pictures = pictures,
                        attachments = attachments,
                        hasPictures = pictures.isNotEmpty(),
                        hasAttachments = attachments.isNotEmpty(),
                        isSelected = msg.id in selectedMessageIds.value,
                        onDelete = { viewModelScope.launch { messageRepository.deleteMessage(msg.id) } },
                        onSelect = {
                            setMultipleMessageSelected(true)
                            toggleMessageSelection(msg.id)
                        },
                        onView = {
                            tempMessageId = msg.id
                        },
                        onEdit = { viewModelScope.launch { enterEditMode(msg.id) } },
                        onExport = { viewModelScope.launch { exportRepository.exportMessagesToPDF(setOf(msg.id)) } }
                    )
                }
                _messagesContentById.value = messageList.associateBy({ it.id }, { it.content })
                _messageIndexMap.value = messageList
                    .mapIndexed { index, message -> message.id to index }
                    .toMap()
                createMessageSubset(messageList)
                if (searchResults.value?.isNotEmpty() == true) {
                    searchRepository.searchMessages(searchQuery.value)
                }
            }.launchIn(viewModelScope)
    }

    override fun resetState() {
        toFocusTextbox.value = false
        toUnFocusTextbox.value = false
        bEditMode.value = false
        tempMessageId = -1
        topicColor.value = Color.Cyan
        topicFontColor.value = Color.Cyan
        messages.value = emptyList()
        _messageIndexMap.value = emptyMap()
        searchRepository.clearSearchResults()
        multipleMessageSelected.value = false
        toastMessage.value = null
        showDeleteDialog.value = false
        isSearchActive.value = false
        requestSearchFocus.value = false
        searchQuery.value = ""
        currentSearchNav.value = 0
        searchedMessageIndex.value = -1
        isDeleteEnabled.value = false
        selectedMessageIds.value = emptySet()
        inputText.value = ""
        selectedFiles.value = emptyList()
        selectedFilesBeforeEdit.value = emptyList()
    }

    override fun collectSearchMessages() {
        messageRepository.getSearchMessages()
            .distinctUntilChanged()
            .onEach { messageList ->
                searchMessages.value = messageList
            }.launchIn(viewModelScope)
    }

    override fun clearToast() {
        toastMessage.value = null
    }

    override fun updateToast(toastMessage: String) {
        this.toastMessage.value = toastMessage
    }

    override suspend fun enterEditMode(messageId: Int) {
        tempMessageId = messageId
        bEditMode.value = true
        inputText.value = _messagesContentById.value[messageId] ?: ""
        selectedFiles.value = fileRepository.getFilesByMessageId(messageId)
        selectedFilesBeforeEdit.value = selectedFiles.value
    }

    override fun createContentToDisplay(messageContent: String, id: Int): AnnotatedString {
        val messageIndex = getMessageIndexFromID(id)
        return if (isSearchActive.value && messageIndex == searchedMessageIndex.value) {
            highlightSearchText(
                messageContent = messageContent,
                searchQuery = searchQuery.value,
                topicColor = topicColor.value,
                topicFontColor = topicFontColor.value
            )
        } else {
            AnnotatedString(messageContent)
        }
    }

    private suspend fun addMessageWithFiles(
        topicId: Int,
        content: String,
        context: Context,
        widthSetting: Int,
        heightSetting: Int
    ) {
        val messageId = messageRepository.addMessage(
            topicId = topicId,
            content = content,
            priority = 0,
            type = 0,
            categoryID = 1
        ).toInt()
        addFilesToMessage(messageId, topicId, selectedFiles.value, context, widthSetting, heightSetting)
    }

    override suspend fun editMessageOnly(
        messageId: Int,
        topicId: Int,
        content: String,
        priority: Int,
        categoryId: Int,
        type: Int,
        messageTimestamp: Long
    ) {
        messageRepository.editMessage(
            messageId = messageId,
            topicId = topicId,
            content = content,
            priority = 0,
            categoryId = 1,
            type = 1,
            messageTimestamp = messageTimestamp
        )
    }

    private suspend fun editMessageWithFiles(
        messageId: Int,
        topicId: Int,
        content: String,
        context: Context,
        widthSetting: Int,
        heightSetting: Int
    ) {
        messageRepository.editMessage(
            messageId = messageId,
            topicId = topicId,
            content = content,
            priority = 0,
            categoryId = 1,
            type = 1,
            messageTimestamp = System.currentTimeMillis()
        )
        val (deletedFiles, addedFiles) = compareFileLists(selectedFilesBeforeEdit.value, selectedFiles.value)
        if (deletedFiles.isNotEmpty()) {
            val fileIdsToDelete = deletedFiles.mapNotNull { uri ->
                uri.path?.let { fileRepository.getFileId(it) }
            }
            fileRepository.deleteFiles(fileIdsToDelete)
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
        fileRepository.addFilesToMessage(
            messageId = messageId,
            topicId = topicId,
            files = files,
            context = context,
            widthSetting = widthSetting,
            heightSetting = heightSetting
        )
    }

    private fun compareFileLists(before: List<Uri>?, after: List<Uri>?): Pair<List<Uri>, List<Uri>> {
        val beforeList = before ?: emptyList()
        val afterList = after ?: emptyList()
        val deleted = beforeList - afterList.toSet()
        val added = afterList - beforeList.toSet()
        return deleted to added
    }

    private fun updateCurrentSearchMessage() {
        val results = searchResults.value ?: emptyList()
        val count = currentSearchNav.value
        if (count in 1..results.size) {
            val messageId = results[count - 1].id
            val messageIndex = getMessageIndexFromID(messageId)
            searchedMessageIndex.value = if (messageIndex >= 0) messageIndex else -1
        } else {
            resetSearchedMessageIndex()
            resetCurrentSearchNav()
        }
    }

    override fun getMessageIndexFromID(messageId: Int): Int {
        return _messageIndexMap.value[messageId] ?: -1
    }

    private fun createMessageSubset(messageList: List<MessageTbl>) {
        val messages = messageList.map { Message(it.id, it.content) }
        _messagesContentById.value = messageList.associateBy({ it.id }, { it.content })
        searchRepository.updateSearchDataset(messages)
    }

    private fun resetInputBar() {
        inputText.value = ""
        selectedFiles.value = emptyList()
        selectedFilesBeforeEdit.value = emptyList()
        bEditMode.value = false
        tempMessageId = -1
    }

    override fun getMessageContentById(messageId: Int): String {
        return _messagesContentById.value[messageId] ?: ""
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                val myApplication = application as DbTopics
                return MessageViewModelImpl(
                    messageRepository = MessageRepositoryImpl(myApplication.messageDao, myApplication.topicDao),
                    fileRepository = FileRepositoryImpl(myApplication.filesDao),
                    searchRepository = SearchRepositoryImpl(),
                    exportRepository = ExportRepositoryImpl()
                ) as T
            }
        }
    }
}