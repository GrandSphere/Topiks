package com.GrandSphere.Topiks.ui.viewmodels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.GrandSphere.Topiks.model.MessageSearchContent
import com.GrandSphere.Topiks.model.allSearchHandler

class searchViewModel : ViewModel() {

    private val allSearchHandler: allSearchHandler = allSearchHandler(emptyList())
    private val _searchResults = MutableLiveData<List<MessageSearchContent>>()
    val searchResults: LiveData<List<MessageSearchContent>> get() = _searchResults

    fun setSearchResultsEmpty()
    {
        _searchResults.value = emptyList()
    }

    fun allSearch(query: String, debounceTime: Long = 150L) {
        allSearchHandler.search(query, debounceTime) { results ->
            _searchResults.postValue(results)
        }
    }
    fun updateAllSearchDataset(newDataset: List<MessageSearchContent>)
    {
       allSearchHandler.updateDataset(newDataset)
    }
}