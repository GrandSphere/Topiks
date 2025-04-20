package com.GrandSphere.Topiks.ui.viewmodels.messageViewmodelRepos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.GrandSphere.Topiks.model.Message
import com.GrandSphere.Topiks.model.MessageSearchHandler

class SearchRepositoryImpl : SearchRepository {

    private val _searchResults = MutableLiveData<List<Message>>()
    override val searchResults: LiveData<List<Message>> = _searchResults
    private val messageSearchHandler = MessageSearchHandler(emptyList())

    override fun searchMessages(query: String, debounceTime: Long) {
        messageSearchHandler.search(query, debounceTime) { results ->
            _searchResults.postValue(results)
        }
    }

    override fun clearSearchResults() {
        _searchResults.value = emptyList()
    }

    override fun updateSearchDataset(messages: List<Message>) {
        messageSearchHandler.updateDataset(messages)
    }
}
