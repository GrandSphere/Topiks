package com.example.topics2.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class MenuItem(val label: String, val onClick: () -> Unit)
// TopBarViewModel.kt
class TopBarViewModel : ViewModel() {
    private val _topBarTitle = MutableStateFlow("Unknown")  // Default title
    val topBarTitle: StateFlow<String> get() = _topBarTitle

    // Add StateFlow for menu items
    private val _menuItems = MutableStateFlow<List<MenuItem>>(emptyList())
    val menuItems: StateFlow<List<MenuItem>> get() = _menuItems

    fun updateTopBarTitle(currentRoute: String?, currentBackStackEntry: NavBackStackEntry?) {
        val title = when (currentRoute) {
            "navnotescreen/{topicId}/{topicName}/{messageId}" -> {
                val topicName = currentBackStackEntry?.arguments?.getString("topicName") ?: "Chat"
                topicName
            }
            "newSearch" -> "Search All"
            "navViewMessage" -> "View Message"
            "navcolourpicker" -> "Pick a colour"
            "navrecentcolours" -> "Recent colours"
            "navtopicListScreen" -> "Topics"
            "navaddtopic/{topicId}" -> "Add Topic"
            else -> "Unknown"
        }

        _topBarTitle.value = title
    }

     // Update the current menu items
    fun setMenuItems(items: List<MenuItem>) {
        _menuItems.value = items
    }
}

object GlobalViewModelHolder {
    private var topBarViewModel: TopBarViewModel? = null

    fun setTopBarViewModel(viewModel: TopBarViewModel) {
        topBarViewModel = viewModel
    }

    fun getTopBarViewModel(): TopBarViewModel {
        return topBarViewModel ?: throw IllegalStateException("TopBarViewModel not initialized")
    }
}
