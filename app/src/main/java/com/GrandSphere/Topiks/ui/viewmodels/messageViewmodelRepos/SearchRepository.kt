package com.GrandSphere.Topiks.ui.viewmodels.messageViewmodelRepos

import androidx.lifecycle.LiveData
import com.GrandSphere.Topiks.model.Message

/**
 * Handles searching messages based on a query.
 */
interface SearchRepository {
    /** Search results for the current query */
    val searchResults: LiveData<List<Message>>

    /** Search messages with a query */
    fun searchMessages(query: String, debounceTime: Long = 150L)

    /** Clear all search results */
    fun clearSearchResults()

    /** Update the dataset used for searching */
    fun updateSearchDataset(messages: List<Message>)
}