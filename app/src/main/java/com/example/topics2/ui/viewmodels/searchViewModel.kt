package com.example.topics2.ui.viewmodels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.topics2.model.MessageSearchContent
import com.example.topics2.model.allSearchHandler

class searchViewModel : ViewModel() {


    private val allSearchHandler: allSearchHandler = allSearchHandler(emptyList())
    private val _searchResults = MutableLiveData<List<MessageSearchContent>>()
    val searchResults: LiveData<List<MessageSearchContent>> get() = _searchResults

    fun search(query: String, debounceTime: Long = 150L) {
        allSearchHandler.search(query, debounceTime) { results ->
            _searchResults.postValue(results)
        }
    }
    fun updateDataset(newDataset: List<MessageSearchContent>)
    {
       allSearchHandler.updateDataset(newDataset)
    }

}
