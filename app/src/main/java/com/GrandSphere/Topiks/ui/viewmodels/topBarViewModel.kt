/*******************************************************************************
 * Copyright (C) <2025> GrandSphere <GrandSphereStudios@protonmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.GrandSphere.Topiks.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import com.GrandSphere.Topiks.model.dataClasses.CustomIcon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class MenuItem(val label: String, val onClick: () -> Unit)
// TopBarViewModel.kt
class TopBarViewModel : ViewModel() {

    private val _topBarTitle = MutableStateFlow("Topics")  // Default title
    val topBarTitle: StateFlow<String> get() = _topBarTitle

    // Add StateFlow for menu items
    private val _menuItems = MutableStateFlow<List<MenuItem>>(emptyList())
    val menuItems: StateFlow<List<MenuItem>> get() = _menuItems

    // StateFlow for custom icons (can be updated dynamically)
    private val _customIcons = MutableStateFlow<List<CustomIcon>>(emptyList())
    val customIcons: StateFlow<List<CustomIcon>> get() = _customIcons

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
            "navAboutScreen" -> "About"
            "newSearch" -> "Search All"
            "navViewMessage" -> "View Message"
            "navcolourpicker" -> "Pick a colour"
            "navrecentcolours" -> "Recent colours"
            "navtopicListScreen" -> "Topics"
            "navShowMorePictures" -> "Pictures"
            else -> "Unknown"
        }

        _topBarTitle.value = title
    }

     // Update the current menu items
    fun setMenuItems(items: List<MenuItem>) {
        _menuItems.value = items
     }

    fun clearMenuItems() {
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

     // Set custom icons (this will replace the current list)
    fun setCustomIcons(icons: List<CustomIcon>) {
        _customIcons.value = icons
    }

    // Add a custom icon
    fun addCustomIcon(icon: CustomIcon) {
        _customIcons.value = _customIcons.value + icon
    }

    // Remove a custom icon
    fun removeCustomIcon(icon: CustomIcon) {
        _customIcons.value = _customIcons.value.filterNot { it.contentDescription == icon.contentDescription }
    }

    // Update an existing custom icon
    fun updateCustomIcon(oldIcon: CustomIcon, newIcon: CustomIcon) {
        _customIcons.value = _customIcons.value.map {
            if (it == oldIcon) newIcon else it
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
