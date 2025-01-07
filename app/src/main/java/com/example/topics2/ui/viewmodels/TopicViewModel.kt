package com.example.topics2.ui.viewmodels

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.topics2.DbTopics
import com.example.topics2.db.dao.TopicDao
import com.example.topics2.db.enitities.TopicTbl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class TopicViewModel (private val topicDao: TopicDao): ViewModel() {
    val cTopicID: Int = 1
    var cTopicColor: Color = Color.Cyan
    var cFontColor: Color = Color.Red

    private val _tempColour = MutableStateFlow<Color>(Color.Cyan)  // Default color as Gray
    val tempColour: StateFlow<Color> = _tempColour  // Expose as immutable StateFlow
    fun settempColour(newColor: Color) { _tempColour.value = newColor }

    // States whether file picker is done
    private val _showPicker = MutableStateFlow<Boolean>(false)
    val showPicker: StateFlow<Boolean> = _showPicker
    fun setShowPicker(newValue: Boolean) { _showPicker.value = newValue }

    // File URI for image imports
    private val _fileURI = MutableStateFlow<String>("")
    val fileURI: StateFlow<String> = _fileURI
    fun setURI(newURI: String) { _fileURI.value = newURI }

    private val _topics = MutableStateFlow<List<TopicTbl>>(emptyList())
    val topics: StateFlow<List<TopicTbl>> = _topics
    private fun fetchTopics() { viewModelScope.launch { _topics.value = topicDao.getAllTopics() } }

    init { fetchTopics() }

    private val _category = MutableStateFlow<String>("")
    val category: StateFlow<String> = _category
    fun setCategory(newCategory: String) { _category.value = newCategory }

    // This is when adding topic colour
    private val _colour = MutableStateFlow<Color>(Color.Cyan)  // Default color as Gray
    val colour: StateFlow<Color> = _colour  // Expose as immutable StateFlow
    fun setColour(newColor: Color) { _colour.value = newColor }


    // Only used in addTopic, to store values when adding Colour
    private val _tempcategory = MutableStateFlow<String>("")
    val tempcategory: StateFlow<String> = _tempcategory
    fun setTempCategory(newCategory: String) {_tempcategory.value = newCategory}


    // Only used in addTopic, to store values when adding Colour
    private val _temptopicname = MutableStateFlow<String>("")
    val temptopicname: StateFlow<String> = _temptopicname
    fun settemptopicname(newCategory: String) {_temptopicname.value = newCategory}

    //private val _topicname = MutableStateFlow<String>("")
    //val topicname: StateFlow<String> = _topicname
    //fun settopicname(newCategory: String) {_topicname.value = newCategory}

    // Delete Messages for topic
    suspend fun deleteMessagesForTopic(topicId: Int) {
        topicDao.deleteMessagesForTopic(topicId)
    }

//suspend fun loadDistinctColors() : List<Color> {
//       // Launch a coroutine in the ViewModel scope
//    var colorList : List<Color> =emptyList()
//       viewModelScope.launch {
//           // Get the colors using the function defined earlier
//           colorList = getColorsFromDistinctARGB()
//
//           Log.d("zzz colorlist",colorList.size.toString())
//           // Update the LiveData with the result
//           //_colors.value =
//       }
//    return colorList
//   }
//
//private val _colors = MutableLiveData<List<Color>>()
//val colors: LiveData<List<Color>> get() = _colors

// Suspend function to get the colors
//suspend fun loadDistinctColors() {
//    val colorList = getColorsFromDistinctARGB()
//    _colors.postValue(colorList) // Update LiveData with the new colors
//}



   suspend fun getColorsFromDistinctARGB(): List<Color> {
       // Retrieve the distinct ordered colors (ARGB as Int)
       val distinctColors = topicDao.getDistinctColorsOrdered()
       // Convert each ARGB value to a Color and return the list
       return distinctColors.map { argbToColor( it) }
   }

        private val _colors = MutableStateFlow<List<Color>>(emptyList())
        val recentColorsList: StateFlow<List<Color>> = _colors
        init { viewModelScope.launch { _colors.value = getColorsFromDistinctARGB() } }


    fun setcolors(){
         viewModelScope.launch { _colors.value = getColorsFromDistinctARGB() }
    }
// Set a new category

// Add a new topic
fun addTopic(
    topicName: String,
    topicColour: Int,
    topicCategory: String,
    topicIcon: String,
    topicPriority: Int
) {
    viewModelScope.launch {
        // Create a new TopicTbl object
        val newTopic = TopicTbl(
            topicName = topicName,
            topicLastEdit = System.currentTimeMillis(),
            topicCreated = System.currentTimeMillis(),
            topicColour = topicColour,
            topicCategory = topicCategory,
            topicIcon = topicIcon,
            topicPriority = topicPriority
        )

        // Insert the new topic into the database
        topicDao.insertTopic(newTopic)

        // Refresh the list of topics
        fetchTopics()
    }
}

// Delete a topic by its ID
fun deleteTopic(topicId: Int) {
    viewModelScope.launch {
        topicDao.deleteTopicById(topicId) // Delete the topic
        fetchTopics() // Refresh the list after deletion
    }
}

fun addTestData() {
    viewModelScope.launch {
        val testTopics = listOf(
            TopicTbl(
                topicName = "Work",
                topicLastEdit = System.currentTimeMillis(),
                topicCreated = System.currentTimeMillis(),
                topicColour = 5733,
                topicCategory = "Productivity",
                topicIcon = "work_icon",
                topicPriority = 1
            ),
            TopicTbl(
                topicName = "Personal",
                topicLastEdit = System.currentTimeMillis(),
                topicCreated = System.currentTimeMillis(),
                topicColour = 222,
                topicCategory = "Wellness",
                topicIcon = "personal_icon",
                topicPriority = 2
            ),
            TopicTbl(
                topicName = "Hobby",
                topicLastEdit = System.currentTimeMillis(),
                topicCreated = System.currentTimeMillis(),
                topicColour = 333,
                topicCategory = "Leisure",
                topicIcon = "hobby_icon",
                topicPriority = 3
            )
        )

        testTopics.forEach { topic ->
            topicDao.insertTopic(topic)
        }

        fetchTopics() // Refresh the topics list
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