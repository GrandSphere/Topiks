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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.GrandSphere.Topiks.model.MessageSearchContent
import com.GrandSphere.Topiks.model.allSearchHandler

class searchViewModel : ViewModel() {

    private val allSearchHandler: allSearchHandler = allSearchHandler(emptyList())
    private val _searchResults = MutableLiveData<List<MessageSearchContent>>()
    val searchResults: LiveData<List<MessageSearchContent>> get() = _searchResults

    fun setSearchResultsEmpty()
    {
        _searchResults.value = emptyList()
    }


    fun allSearch(query: String, debounceTime: Long = 150L) {
        allSearchHandler.search(query, debounceTime) { results ->
            _searchResults.postValue(results)
        }
    }
    fun updateAllSearchDataset(newDataset: List<MessageSearchContent>)
    {
       allSearchHandler.updateDataset(newDataset)
    }
}