package com.example.topics2.ui.viewmodels

//import androidx.compose.material.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.topics2.DbTopics
import com.example.topics2.db.dao.TopicDao
import com.example.topics2.db.enitities.TopicTbl
import com.example.topics2.ui.components.addTopic.argbToColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.topics2.model.TopicSearchHandler
import com.example.topics2.model.tblTopicIdName
import com.example.topics2.ui.themes.CustomTertiary

// TODO ADD CATEGORY TO DB WHEN ADDING TOPIC


class TopicViewModel (private val topicDao: TopicDao): ViewModel() {
    val cTopicID: Int = 1
    val defaultColor: Color = Color(0xFFDCD0FF)
//    val defaultColor: Color = CustomTertiary
    var cTopicColor: Color = defaultColor
    var cFontColor: Color = Color.Black

    // Get and refresh the topic list
    private val _topics = MutableStateFlow<List<TopicTbl>>(emptyList())
    val topics: StateFlow<List<TopicTbl>> = _topics
     fun collectTopics() {
        topicDao.getAllTopics().onEach {
            topicList ->_topics.value = topicList
            createTopicsSubset(topicList)
        }.launchIn(viewModelScope)
    }

    private val topicSearchHandler: TopicSearchHandler = TopicSearchHandler(emptyList())
    private val _topicsSubset = MutableStateFlow<List<tblTopicIdName>>(emptyList())
    private val _topicsMap = MutableStateFlow<Map<Int, TopicTbl>>(emptyMap())

    // For search results
    private val _searchResults = MutableLiveData<List<tblTopicIdName>>()
    val searchResults: LiveData<List<tblTopicIdName>> get() = _searchResults

    fun createTopicsSubset(topicList: List<TopicTbl>) {
        _topicsSubset.value = topicList.map { tblTopicIdName(it.id, it.name) }
        // Create a Map for fast lookup by id
        val topicsMap = topicList.associateBy { it.id}
        _topicsMap.value = topicsMap
        topicSearchHandler.updateDataset(_topicsSubset.value)
    }

    // Access the full TopicTbl based on search result id
    fun getTopicObjectById(topicId: Int): TopicTbl? {
        return _topicsMap.value[topicId]
    }

    fun search(query: String, debounceTime: Long = 150L) {
        topicSearchHandler.search(query, debounceTime) { results ->
            _searchResults.postValue(results)
        }
    }

    // This is when adding topic colour
    private val _colour = MutableStateFlow<Color>(defaultColor)
    val colour: StateFlow<Color> = _colour
    fun setColour(newColor: Color) { _colour.value = newColor }

    private val _tempColour = MutableStateFlow<Color>(defaultColor)
    val tempColour: StateFlow<Color> = _tempColour
    fun settempColour(newColor: Color) { _tempColour.value = newColor }

    // File URI for image imports
    private val _fileURI = MutableStateFlow<String>("")
    val fileURI: StateFlow<String> = _fileURI
    fun setFileURI(newURI: String) { _fileURI.value = newURI }

    // Only used in addTopic, to store values before adding to DB
    private val _category = MutableStateFlow<String>("")
    val category: StateFlow<String> = _category
    fun setCategory(newCategory: String) { _category.value = newCategory }

    // Only used in addTopic, to store values when adding Colour
    private val _tempcategory = MutableStateFlow<String>("")
    val tempcategory: StateFlow<String> = _tempcategory
    fun setTempCategory(newCategory: String) {_tempcategory.value = newCategory}

    // Only used in addTopic, to store values when adding Colour
    private val _temptopicname = MutableStateFlow<String>("")
    val temptopicname: StateFlow<String> = _temptopicname
    fun settemptopicname(newCategory: String) {_temptopicname.value = newCategory}

    private val _recentColoursList = MutableStateFlow<List<Color>>(emptyList())
    val recentColoursList: StateFlow<List<Color>> = _recentColoursList

    init {
        collectTopics()
        viewModelScope.launch {
            loadDistinctColours()
        }
    }

// Suspend function to get distinct recent colors
suspend fun loadDistinctColors() {
    val colorList = getColorsFromDistinctARGB()
    _recentColoursList.value = colorList // Update LiveData with the new colors
}

    suspend  fun loadDistinctColours() {
        topicDao.getDistinctColorsOrdered()
            .map { argbList -> argbList.map { argbToColor(it) } } // Convert ARGB to Color
            .collect { colors ->
                _recentColoursList.value = colors
            }
    }
    suspend fun getColorsFromDistinctARGB(): List<Color> {
        return topicDao.getDistinctColorsOrdered()
            .first() // Collect the first emission (assuming you're fetching the data only once)
            .map { argbToColor(it) }
    }

    // Delete Messages for topic
    suspend fun deleteMessagesForTopic(topicId: Int) {
        topicDao.deleteTopicById(topicId)
    }

// Delete a topic by its ID
fun deleteTopic(topicId: Int) {
    viewModelScope.launch (Dispatchers.IO){
        topicDao.deleteTopicById(topicId)
    }
}

fun addTopic(
        topicName: String,
        topicColour: Int,
        topicCategory: Int = 1,
        topicIconPath: String,
        topicPriority: Int
    ) {
        viewModelScope.launch {
            // Create a new TopicTbl object
            val newTopic = TopicTbl(
                name = topicName,
                lastEditTime = System.currentTimeMillis(),
                createTime = System.currentTimeMillis(),
                colour = topicColour,
                categoryId = topicCategory,
                iconPath = topicIconPath,
                priority = topicPriority
            )
            topicDao.insertTopic(newTopic)
        }
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
            return TopicViewModel(myApplication.topicDao) as T
        }
    }
}
}