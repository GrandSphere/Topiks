package com.example.topics2.ui.viewmodels

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
    private val _topics = MutableStateFlow<List<TopicTbl>>(emptyList())
    val topics: StateFlow<List<TopicTbl>> = _topics
    private fun fetchTopics() { viewModelScope.launch { _topics.value = topicDao.getAllTopics() } }

    init { fetchTopics() }

    private val _category = MutableStateFlow<String>("")
    val category: StateFlow<String> = _category
    fun setCategory(newCategory: String) { _category.value = newCategory }

    private val _colour = MutableStateFlow<Color>(Color.Red)  // Default color as Gray
    val colour: StateFlow<Color> = _colour  // Expose as immutable StateFlow
    fun setColour(newColor: Color) { _colour.value = newColor }


    private val _tempcategory = MutableStateFlow<String>("")
    val tempcategory: StateFlow<String> = _tempcategory
    fun setTempCategory(newCategory: String) {_tempcategory.value = newCategory}


    private val _temptopicname = MutableStateFlow<String>("")
    val temptopicname: StateFlow<String> = _temptopicname
    fun settemptopicname(newCategory: String) {_temptopicname.value = newCategory}

    private val _topicname = MutableStateFlow<String>("")
    val topicname: StateFlow<String> = _topicname
    fun settopicname(newCategory: String) {_topicname.value = newCategory}



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
