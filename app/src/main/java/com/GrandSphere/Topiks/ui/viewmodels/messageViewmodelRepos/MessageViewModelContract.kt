
package com.GrandSphere.Topiks.ui.viewmodels

import android.content.Context
import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.lifecycle.LiveData
import com.GrandSphere.Topiks.model.Message
import com.GrandSphere.Topiks.model.MessageSearchContent
import com.GrandSphere.Topiks.model.dataClasses.MessageUiModel
import kotlinx.coroutines.flow.StateFlow

/**
 * Defines the UI-related stuff for the MessageViewModel, like state (e.g., messages, search query)
 * and actions (e.g., sending a message, toggling search).
 */
interface MessageViewModelContract {
    // UI State
    /** List of messages to show in the UI */
    val messages: StateFlow<List<MessageUiModel>>
    /** Toast messages to display errors or success */
    val toastMessage: StateFlow<String?>
    /** Whether to focus the textbox */
    val toFocusTextbox: StateFlow<Boolean>
    /** Whether to unfocus the textbox */
    val toUnFocusTextbox: StateFlow<Boolean>
    /** Whether we're in edit mode for a message */
    val bEditMode: StateFlow<Boolean>
    /** Temporary message ID for editing */
    var tempMessageId: Int
    /** Background color of the topic */
    val topicColor: StateFlow<Color>
    /** Font color for the topic (based on luminance) */
    val topicFontColor: StateFlow<Color>
    /** Whether to show the delete confirmation dialog */
    val showDeleteDialog: StateFlow<Boolean>
    /** Whether search is active */
    val isSearchActive: StateFlow<Boolean>
    /** Whether to request focus for the search bar */
    val requestSearchFocus: StateFlow<Boolean>
    /** Current search result index for navigation */
    val currentSearchNav: StateFlow<Int>
    /** Index of the message highlighted in search */
    val searchedMessageIndex: StateFlow<Int>
    /** Whether delete is enabled */
    val isDeleteEnabled: StateFlow<Boolean>
    /** Set of selected message IDs */
    val selectedMessageIds: StateFlow<Set<Int>>
    /** Current search query */
    val searchQuery: StateFlow<String>
    /** Text in the input bar */
    val inputText: StateFlow<String>
    /** List of selected files for the message */
    val selectedFiles: StateFlow<List<Uri>>
    /** Files selected before editing (for comparison) */
    val selectedFilesBeforeEdit: StateFlow<List<Uri>>
    /** Search results for messages */
    val searchResults: LiveData<List<Message>>
    /** Whether multiple messages are selected */
    val multipleMessageSelected: StateFlow<Boolean>
    /** Current topic ID */
    val topicId: StateFlow<Int>
    /** Messages available for search */
    val searchMessages: StateFlow<List<MessageSearchContent>>

    // UI Actions
    /** Set whether to focus the textbox */
    fun setToFocusTextbox(newValue: Boolean)
    /** Set whether to unfocus the textbox */
    fun setToUnFocusTextbox(newValue: Boolean)
    /** Turn edit mode on or off */
    fun setEditMode(newValue: Boolean)
    /** Set the topic background color */
    fun setTopicColor(topicColor: Color)
    /** Set the topic font color based on luminance */
    fun setTopicFontColor()

    fun getTempMessageID(): Int
    fun setTempMessageID(newValue: Int)
    /** Show or hide the delete dialog */
    fun setShowDeleteDialog(show: Boolean)
    /** Clear the search focus request */
    fun clearSearchFocusFocus()
    /** Reset the search navigation index */
    fun resetCurrentSearchNav()
    /** Reset the highlighted search message index */
    fun resetSearchedMessageIndex()
    /** Toggle whether delete is enabled */
    fun toggleDeleteEnabled()
    /** Set whether multiple messages are selected */
    fun setMultipleMessageSelected(newState: Boolean)
    /** Set the current topic ID */
    fun setTopicId(newValue: Int)
    /** Update the text in the input bar */
    fun updateInputText(newText: String)
    /** Add files to the selected files list */
    fun addSelectedFiles(uris: List<Uri>?)
    /** Remove a file from the selected files list */
    fun removeSelectedFile(index: Int)
    /** Reset the input bar to its initial state */
    fun initializeInputBar()
    /** Send or edit a message with files */
    fun sendMessage(topicId: Int, context: Context, widthSetting: Int = 500, heightSetting: Int = 500)
    /** Clear the input bar (long press on send) */
    fun clearInputBar()
    /** Update the search query and trigger search */
    fun updateSearchQuery(query: String)
    /** Toggle search mode on or off */
    fun toggleSearch()
    /** Select or deselect a message */
    fun toggleMessageSelection(messageId: Int)
    /** Select or deselect all messages */
    fun toggleSelectAllMessages()
    /** Go to the next search result */
    fun navigateNextSearchResult()
    /** Go to the previous search result */
    fun navigatePreviousSearchResult()
    /** Confirm deletion of selected messages */
    fun confirmDeleteSelectedMessages()
    /** Cancel the delete dialog */
    fun cancelDeleteDialog()
    /** Initialize the ViewModel with topic details */
    fun initialize(topicId: Int, topicColor: Color, messageId: Int, context: Context)
    /** Update the top bar with icons and menu items */
    fun updateTopBar(topBarViewModel: TopBarViewModel)
    /** Start collecting messages for the topic */
    fun collectMessages(topicId: Int, context: Context)
    /** Reset all ViewModel state */
    fun resetState()
    /** Start collecting messages for search */
    fun collectSearchMessages()
    /** Clear the toast message */
    fun clearToast()
    /** Show a toast message */
    fun updateToast(toastMessage: String)
    /** Enter edit mode for a message */
    suspend fun enterEditMode(messageId: Int)
    /** Format message content for display (e.g., highlight search text) */
    fun createContentToDisplay(messageContent: String, id: Int): AnnotatedString
    /**
     * Gets the index of a message in the messages list by its ID.
     * Returns -1 if the message is not found.
     */
    fun getMessageIndexFromID(messageId: Int): Int
    /**
     * Gets the content of a message by its ID.
     * Returns an empty string if the message is not found.
     */
    fun getMessageContentById(messageId: Int): String
    /**
     * Edits an existing message with new content and optional files.
     */
    suspend fun editMessageOnly( messageId: Int, topicId: Int, content: String, priority: Int,
            categoryId: Int, type: Int, messageTimestamp: Long = System.currentTimeMillis())


}