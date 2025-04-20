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

package com.GrandSphere.Topiks.ui.viewmodels.messageViewmodelRepos

import androidx.lifecycle.LiveData
import com.GrandSphere.Topiks.model.Message

/**
 * Handles searching messages based on a query.
 */
interface SearchRepository {
    /** Search results for the current query */
    val searchResults: LiveData<List<Message>>

    /** Search messages with a query */
    fun searchMessages(query: String, debounceTime: Long = 150L)

    /** Clear all search results */
    fun clearSearchResults()

    /** Update the dataset used for searching */
    fun updateSearchDataset(messages: List<Message>)
}