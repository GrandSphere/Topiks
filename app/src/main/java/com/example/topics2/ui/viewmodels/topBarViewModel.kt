package com.example.topics2.ui.viewmodels

import androidx.compose.material3.MaterialTheme
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
            "navaddtopic/{topicId}" -> {
                val topicId = currentBackStackEntry?.arguments?.getInt("topicId")
                var title = ""
                title = if (topicId == -1) { "Add Topic" } else { "Edit Topic" }
                title
            }
            "newSearch" -> "Search All"
            "navViewMessage" -> "View Message"
            "navcolourpicker" -> "Pick a colour"
            "navrecentcolours" -> "Recent colours"
            "navtopicListScreen" -> "Topics"
            else -> "Unknown"
        }

        _topBarTitle.value = title
    }

     // Update the current menu items
    fun setMenuItems(items: List<MenuItem>) {
        _menuItems.value = items
     }

    fun clearMenuItems(items: List<MenuItem>) {
        _menuItems.value = emptyList()
    }

    // Add a new menu item
    fun addMenuItem(item: MenuItem, position: Int? = null) {
        //_menuItems.value = _menuItems.value + item  // Add new item to list
        val currentItems = _menuItems.value.toMutableList()
        if (position != null && position in 0..currentItems.size) {
            currentItems.add(position, item)  // Insert at the specified position
        } else {
            currentItems.add(item)  // Add at the end if no position is specified or invalid
        }
        _menuItems.value = currentItems
    }

    // Remove a menu item by label (or any unique identifier)
    fun removeMenuItem(label: String) {
        _menuItems.value = _menuItems.value.filterNot { it.label == label }  // Remove by label
    }

    // Update a menu item
    fun updateMenuItem(oldLabel: String, newItem: MenuItem) {
        _menuItems.value = _menuItems.value.map {
            if (it.label == oldLabel) newItem else it  // Replace the item with the new one
        }
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
