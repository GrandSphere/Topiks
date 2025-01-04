package com.example.topics2.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.topics2.db.dao.TopicDao
import com.example.topics2.model.enitities.TopicTbl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TopicViewModel (private val topicDao: TopicDao): ViewModel() {
    private val _topics = MutableStateFlow<List<TopicTbl>>(emptyList())
    val topics: StateFlow<List<TopicTbl>> = _topics

    init {
        fetchTopics()
    }

    private fun fetchTopics() {
        viewModelScope.launch {
            _topics.value = topicDao.getAllTopics()
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
                    topicColour = "#FF5733",
                    topicCategory = "Productivity",
                    topicIcon = "work_icon",
                    topicPriority = 1
                ),
                TopicTbl(
                    topicName = "Personal",
                    topicLastEdit = System.currentTimeMillis(),
                    topicCreated = System.currentTimeMillis(),
                    topicColour = "#33FF57",
                    topicCategory = "Wellness",
                    topicIcon = "personal_icon",
                    topicPriority = 2
                ),
                TopicTbl(
                    topicName = "Hobby",
                    topicLastEdit = System.currentTimeMillis(),
                    topicCreated = System.currentTimeMillis(),
                    topicColour = "#3357FF",
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

}
