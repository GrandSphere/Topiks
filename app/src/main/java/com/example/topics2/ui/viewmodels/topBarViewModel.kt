package com.example.topics2.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// TopBarViewModel.kt
class TopBarViewModel : ViewModel() {
    private val _topBarTitle = MutableStateFlow("Unknown")  // Default title
    val topBarTitle: StateFlow<String> get() = _topBarTitle

    fun updateTopBarTitle(currentRoute: String?, currentBackStackEntry: NavBackStackEntry?) {
        val title = when (currentRoute) {
            "navnotescreen/{topicId}/{topicName}" -> {
                val topicName = currentBackStackEntry?.arguments?.getString("topicName") ?: "Chat"
                topicName
            }
            "navcolourpicker" -> "Pick a colour"
            "navrecentcolours" -> "Recent colours"
            "navtopicListScreen" -> "Topics"
            "navaddtopic" -> "Add Topic"
            else -> "Unknown"
        }
        _topBarTitle.value = title
    }
}