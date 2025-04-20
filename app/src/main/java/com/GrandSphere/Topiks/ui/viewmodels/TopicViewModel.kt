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

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.GrandSphere.Topiks.DbTopics
import com.GrandSphere.Topiks.db.dao.TopicDao
import com.GrandSphere.Topiks.db.enitities.TopicTbl
import com.GrandSphere.Topiks.model.TopicSearchHandler
import com.GrandSphere.Topiks.model.tblTopicIdName
import com.GrandSphere.Topiks.ui.components.addTopic.argbToColor
import com.GrandSphere.Topiks.ui.components.addTopic.colorToArgb
import com.GrandSphere.Topiks.utilities.copyFileToUserFolder
import com.GrandSphere.Topiks.utilities.logFunc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


class TopicViewModel(private val topicDao: TopicDao, private val context: Context) : ViewModel() {

    val cTopicID: Int = 1
    val defaultColor: Color = Color(0xFFDCD0FF)
    var cTopicColor: Color = defaultColor
    var cFontColor: Color = Color.Black

    // Get and refresh the topic list
    private val _topics = MutableStateFlow<List<TopicTbl>>(emptyList())
    val topics: StateFlow<List<TopicTbl>> = _topics

    fun collectTopics() {
        try {
            topicDao.getAllTopics()
                .distinctUntilChanged()
                .onEach { topicList ->
                _topics.value = topicList
                createTopicsSubset(topicList)
            }.launchIn(viewModelScope)
        } catch (e: Exception) {
            logFunc(context = context, "TopicViewModel: Error collecting topics: ${e.message}")
        }
    }

    private val topicSearchHandler: TopicSearchHandler = TopicSearchHandler(emptyList())
    private val _topicsSubset = MutableStateFlow<List<tblTopicIdName>>(emptyList())
    private val _topicsMap = MutableStateFlow<Map<Int, TopicTbl>>(emptyMap())

    private val _selectedFileStringBeforeEdit = MutableStateFlow<String>("")
    val selectedFileStringBeforeEdit: StateFlow<String> = _selectedFileStringBeforeEdit

    private val _selectedFileChanged = MutableStateFlow<Boolean>(false)
    val selectedFileChanged: StateFlow<Boolean> = _selectedFileChanged

    // For search results
    private val _searchResults = MutableLiveData<List<tblTopicIdName>>()
    val searchResults: LiveData<List<tblTopicIdName>> get() = _searchResults

    fun createTopicsSubset(topicList: List<TopicTbl>) {
        try {
            _topicsSubset.value = topicList.map { tblTopicIdName(it.id, it.name) }
            // Create a Map for fast lookup by id
            val topicsMap = topicList.associateBy { it.id }
            _topicsMap.value = topicsMap
            topicSearchHandler.updateDataset(_topicsSubset.value)
        } catch (e: Exception) {
            logFunc(context, "TopicViewModel: Error creating topic subset: ${e.message}")
        }
    }

    // States whether you are editing or adding
    private val _bEditMode = MutableStateFlow<Boolean>(false)
    val bEditMode: StateFlow<Boolean> = _bEditMode
    fun setEditMode(newValue: Boolean) { _bEditMode.value = newValue }

    // States whether you are editing or adding
    private val _bEditedMode = MutableStateFlow<Boolean>(false)
    val bEditedMode: StateFlow<Boolean> = _bEditedMode
    fun setEditedMode(newValue: Boolean) { _bEditedMode.value = newValue }

    // Access the full TopicTbl based on search result id
    fun getTopicObjectById(topicId: Int): TopicTbl? {
        return _topicsMap.value[topicId]
    }

    fun search(query: String, debounceTime: Long = 150L) {
        try {
            topicSearchHandler.search(query, debounceTime) { results ->
                _searchResults.postValue(results)
            }
        } catch (e: Exception) {
            logFunc(context, "TopicViewModel: Error during search: ${e.message}")
        }
    }

    fun launchFilePicker(
        launcher: androidx.activity.result.ActivityResultLauncher<String>,
        mimeTypes: Array<String>
    ) {
        val mimeType = mimeTypes.firstOrNull() ?: "*/*"
        try {
            launcher.launch(mimeType)
        } catch (e: Exception) {
            logFunc(context, "TopicViewModel: Error launching file picker: ${e.message}")
        }
    }

    // This is when adding topic colour
    private val _colour = MutableStateFlow<Color>(defaultColor)
    val colour: StateFlow<Color> = _colour
    fun setColour(newColor: Color) {
        _colour.value = newColor
    }

    private val _tempColour = MutableStateFlow<Color>(defaultColor)
    val tempColour: StateFlow<Color> = _tempColour
    fun setTempColour(newColor: Color) {
        _tempColour.value = newColor
    }

    // File URI for image imports
    private val _fileURI = MutableStateFlow<String>("")
    val fileURI: StateFlow<String> = _fileURI
    fun setFileURI(newURI: String) {
        _fileURI.value = newURI
    }

    // Only used in addTopic, to store values before adding to DB
    private val _category = MutableStateFlow<String>("")
    val category: StateFlow<String> = _category
    fun setCategory(newCategory: String) {
        _category.value = newCategory
    }

    // Only used in addTopic, to store values when adding Colour
    private val _tempcategory = MutableStateFlow<String>("")
    val tempcategory: StateFlow<String> = _tempcategory
    fun setTempCategory(newCategory: String) {
        _tempcategory.value = newCategory
    }

    // Only used in addTopic, to store values when adding Colour
    private val _temptopicname = MutableStateFlow<String>("")
    val temptopicname: StateFlow<String> = _temptopicname
    fun setTempTopicName(newTopicName: String) {
        _temptopicname.value = newTopicName
    }

    private val _recentColoursList = MutableStateFlow<List<Color>>(emptyList())
    val recentColoursList: StateFlow<List<Color>> = _recentColoursList

    init {
        collectTopics()
        viewModelScope.launch {
            try {
                loadDistinctColours()
            } catch (e: Exception) {
                logFunc(context, "TopicViewModel: Error loading distinct colours: ${e.message}")
            }
        }
    }

    // Suspend function to get distinct recent colors
    suspend fun loadDistinctColors(): List<Color> {
        try {
            val colorList = getColorsFromDistinctARGB()
            _recentColoursList.value = colorList // Update LiveData with the new colors
            return colorList
        } catch (e: Exception) {
            logFunc(context, "TopicViewModel: Error loading distinct colors: ${e.message}")
            return emptyList()
        }
    }

    fun initializeEditMode(topicId: Int) {
        viewModelScope.launch {
            try {
                setEditedMode(true)
                val topicObj = getTopicObjectById(topicId)
                if (topicObj != null) {
                    setTempTopicName(topicObj.name)
                    val color = argbToColor(topicObj.colour)
                    setColour(color)
                    setTempColour(color)
                    setFileURI(topicObj.iconPath ?: "")
                } else {
                    logFunc(context, "TopicViewModel: Topic with ID $topicId not found")
                }
                setEditMode(false)
            } catch (e: Exception) {
                logFunc(context, "TopicViewModel: Error initializing edit mode: ${e.message}")
            }
        }
    }

    suspend fun getIconPathByID(topicId: Int):String{
        return topicDao.getIconPathById(topicId)
    }

    suspend fun loadDistinctColours() {
        try {
            topicDao.getDistinctColorsOrdered()
                .map { argbList -> argbList.map { argbToColor(it) } } // Convert ARGB to Color
                .collect { colors ->
                    _recentColoursList.value = colors
                }
        } catch (e: Exception) {
            logFunc(context, "TopicViewModel: Error loading distinct colours: ${e.message}")
        }
    }

    suspend fun getColorsFromDistinctARGB(): List<Color> {
        try {
            return topicDao.getDistinctColorsOrdered()
                .first() // Collect the first emission (assuming you're fetching the data only once)
                .map { argbToColor(it) }
        } catch (e: Exception) {
            logFunc(context, "TopicViewModel: Error getting distinct ARGB colors: ${e.message}")
            return emptyList()
        }
    }

    // Delete Messages for topic
    suspend fun deleteMessagesForTopic(topicId: Int) {
        try {
            topicDao.deleteTopicById(topicId)
        } catch (e: Exception) {
            logFunc(context, "TopicViewModel: Error deleting messages for topic: ${e.message}")
        }
    }

    // Delete a topic by its ID
    fun deleteTopic(topicId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                topicDao.deleteTopicById(topicId)
            } catch (e: Exception) {
                logFunc(context, "TopicViewModel: Error deleting topic: ${e.message}")
            }
        }
    }
    fun getTopicById(topicId: Int): TopicTbl {
        return topicDao.getTopicById(topicId)
    }

    fun loadIconPathForEdit(topicId: Int) {
        viewModelScope.launch {
            try {
                val topicIconPath = topicDao.getIconPathById(topicId) ?: ""
                _selectedFileStringBeforeEdit.value = topicIconPath
            } catch (e: Exception) {
                logFunc(context, "TopicViewModel: Error loading icon path: ${e.message}")
            }
        }
    }

    fun clearViewModelState() {
        setFileURI("")
        setTempTopicName("")
        setEditMode(false)
        setEditedMode(false)
        _selectedFileChanged.value = false
    }

    fun confirmTopic(
        topicId: Int,
        topicName: String,
        widthSetting: Int = 100,
        heightSetting: Int = 100
    ) {
        viewModelScope.launch {
            try {
                if (topicName.isBlank()) return@launch

                val nColor: Color = colour.value
                val iColor: Int = colorToArgb(nColor)
                var topicIconPath = selectedFileStringBeforeEdit.value

                // Handle file copying if a new file is selected
                val selectedFileUri = fileURI.value
                if (selectedFileUri.isNotEmpty() && selectedFileUri != selectedFileStringBeforeEdit.value) {
                    _selectedFileChanged.value = true
                    val tempPath = copyFileToUserFolder(
                        context = context,
                        currentUri = selectedFileUri.toUri(),
                        directoryName = "",
                        width = widthSetting,
                        height = heightSetting,
                        thumbnailOnly = true
                    )
                    topicIconPath = tempPath.first
                }

                if (bEditedMode.value) {
                    editTopic(
                        topicId = topicId,
                        topicName = topicName,
                        topicColour = iColor,
                        topicCategory = 1,
                        topicIconPath = topicIconPath,
                        topicPriority = 0
                    )
                } else {
                    addTopic(
                        topicName = topicName,
                        topicColour = iColor,
                        topicCategory = 1,
                        topicIconPath = topicIconPath,
                        topicPriority = 0
                    )
                }
                clearViewModelState()
            } catch (e: Exception) {
                logFunc(context, "TopicViewModel: Error confirming topic: ${e.message}")
            }
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
            try {
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
            } catch (e: Exception) {
                logFunc(context, "TopicViewModel: Error adding topic: ${e.message}")
            }
        }
    }

    fun editTopic(
        topicId: Int,
        topicName: String,
        topicColour: Int,
        topicCategory: Int = 1,
        topicIconPath: String,
        topicPriority: Int
    ) {
        viewModelScope.launch {
            try {
                val createdTime: Long = topicDao.getCreatedTimeByID(topicId)
                val editTopic = TopicTbl(
                    id = topicId,
                    name = topicName,
                    lastEditTime = System.currentTimeMillis(),
                    createTime = createdTime,
                    colour = topicColour,
                    categoryId = topicCategory,
                    iconPath = topicIconPath,
                    priority = topicPriority
                )
                topicDao.editTopic(editTopic)
            } catch (e: Exception) {
                logFunc(context, "TopicViewModel: Error editing topic: ${e.message}")
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                // Get the Application object from extras
                val application = checkNotNull(extras[APPLICATION_KEY])
                val context = application.applicationContext // Get the context

                // Get the TopicDao from the Application class
                val myApplication = application as DbTopics
                return TopicViewModel(myApplication.topicDao, context) as T
            }
        }
    }
    //companion object {
    //    // Factory to create the ViewModel
    //    val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
    //        @Suppress("UNCHECKED_CAST")
    //        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
    //            // Get the Application object from extras
    //            val application = checkNotNull(extras[APPLICATION_KEY])

    //            // Get the TopicDao from the Application class
    //            val myApplication = application as DbTopics
    //            return TopicViewModel(myApplication.topicDao) as T
    //        }
    //    }
    //}
}
