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

package com.GrandSphere.Topiks.ui.screens

// Moved to viewmodel

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.GrandSphere.Topiks.ui.components.CustomSearchBox
import com.GrandSphere.Topiks.ui.components.messageScreen.InputBarMessageScreen
import com.GrandSphere.Topiks.ui.components.messageScreen.MessageBubble
import com.GrandSphere.Topiks.ui.focusClear
import com.GrandSphere.Topiks.ui.viewmodels.GlobalViewModelHolder
import com.GrandSphere.Topiks.ui.viewmodels.MessageViewModelContract

@Composable
fun MessageScreen(
    navController: NavController,
    viewModel: MessageViewModelContract,
    topicId: Int,
    messageId: Int = -1,
    topicColor: Color = MaterialTheme.colorScheme.tertiary
) {
    val messages by viewModel.messages.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val selectMultiple by viewModel.multipleMessageSelected.collectAsState()
    val colors = MaterialTheme.colorScheme
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()
    val isSearchActive by viewModel.isSearchActive.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.observeAsState(emptyList())
    val searchResultCount by viewModel.currentSearchNav.collectAsState()
    val currentSearchMessageIndex by viewModel.searchedMessageIndex.collectAsState()
    val currentSearchNav by viewModel.currentSearchNav.collectAsState()
    val isDeleteEnabled by viewModel.isDeleteEnabled.collectAsState()
    val selectedMessageIds by viewModel.selectedMessageIds.collectAsState()
    val showDeleteDialog by viewModel.showDeleteDialog.collectAsState()
    val requestSearchFocus by viewModel.requestSearchFocus.collectAsState()
    val topicFontColor by viewModel.topicFontColor.collectAsState()
    val context = LocalContext.current
    val topBarViewModel = GlobalViewModelHolder.getTopBarViewModel()
    var inputBarHeightPx by remember { mutableStateOf(0) }
    val density = LocalDensity.current
    val inputBarHeight = with(density) { inputBarHeightPx.toDp() }

   val tempMessageId = viewModel.tempMessageId
    LaunchedEffect (tempMessageId){
        //Log.d("QQMESSAGE TEMPMSG ID: ", viewModel.getTempMessageId().toString())
        //Log.d("QQMESSAGE test : ", viewModel.gettest().toString())

    }
    LaunchedEffect(Unit) {
        viewModel.initialize(
            topicId = topicId,
            topicColor = topicColor,
            messageId = messageId,
            context = context
        )
        viewModel.updateTopBar(topBarViewModel)
    }


    LaunchedEffect(requestSearchFocus) {
        if (requestSearchFocus) {
            focusManager.clearFocus()
            focusRequester.requestFocus()
            //viewModel.clearSearchFocusRequest()
            viewModel.clearSearchFocusFocus()
        }
    }

    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    LaunchedEffect(currentSearchNav){
        if (isSearchActive && searchResults.isNotEmpty() && currentSearchMessageIndex >= 0) {
            scrollState.scrollToItem(currentSearchMessageIndex)
        }
    }

    LaunchedEffect(searchResults, messages.size) {
        if (searchResults.isEmpty()) viewModel.resetCurrentSearchNav()
        if (messageId != -1) {
            val messageIndex = viewModel.getMessageIndexFromID(messageId)
            if (messageIndex >= 0) {
                scrollState.scrollToItem(messageIndex)
            }
        } else if (messages.isNotEmpty()) {
            scrollState.scrollToItem(messages.size - 1)
        }
    }

    LaunchedEffect(selectMultiple) {
        viewModel.updateTopBar(topBarViewModel)
    }

    LaunchedEffect(isDeleteEnabled) {
        viewModel.updateTopBar(topBarViewModel)
    }

    Column(
        modifier = Modifier
            .padding(top = 0.dp)
            .fillMaxSize()
            .focusClear()
    ) {
        if (isSearchActive) {
            CustomSearchBox(
                focusModifier = Modifier.focusRequester(focusRequester),
                inputText = searchQuery,
                sPlaceHolder = "Search Messages...",
                onValueChange = { viewModel.updateSearchQuery(it) },
                bShowSearchNav = true,
                onNextClick = { viewModel.navigateNextSearchResult() },
                onPreviousClick = { viewModel.navigatePreviousSearchResult() },
                onClick = { viewModel.toggleSearch() },
                iSearchCount = searchResults.size,
                iCurrentSearch = searchResultCount
            )
        }

        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .focusClear()
                .weight(1f)
                .fillMaxWidth()
                .padding(bottom = inputBarHeight)
        ) {
            items(messages.size) { index ->
                val message = messages[index]
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.padding(0.dp)
                ) {
                    if (selectMultiple) {
                        Checkbox(
                            checked = message.id in selectedMessageIds,
                            onCheckedChange = {
                                viewModel.toggleMessageSelection(message.id)
                            },
                            modifier = Modifier
                                .scale(0.75f)
                                .padding(start = 10.dp, end = 4.dp, bottom = 0.dp, top = 0.dp)
                                .size(1.dp),
                            colors = CheckboxDefaults.colors(
                                checkedColor = topicColor,
                                uncheckedColor = colors.onBackground,
                                checkmarkColor = topicFontColor
                            )
                        )
                    }

                    MessageBubble(
                        onFocusClear = { focusManager.clearFocus() },
                        navController = navController,
                        topicColor = topicColor,
                        topicFontColor = topicFontColor,
                        annotatedMessageContent = viewModel.createContentToDisplay(message.messageContent, message.id),
                        containsPictures = message.hasPictures,
                        containsAttachments = message.hasAttachments,
                        listOfPictures = message.pictures,
                        listOfAttachmentsP = message.attachments,
                        timestamp = message.timestamp,
                        onDeleteClick = message.onDelete,
                        onSelectedClick = message.onSelect,
                        onViewMessage = {
                            message.onView()
                            navController.navigate("navViewMessage")
                        },
                        onEditClick = message.onEdit,
                        onExportClick = message.onExport,
                        bDeleteEnabled = isDeleteEnabled,
                        messageSelected = message.isSelected
                    )
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            InputBarMessageScreen(
                viewModel = viewModel,
                topicId = topicId,
                topicColour = topicColor
            )
        }
    }

    if (showDeleteDialog) {
        val styledText = buildAnnotatedString {
            append("Delete ")
            withStyle(style = SpanStyle(color = colors.error)) {
                append(selectedMessageIds.size.toString())
            }
            append(" selected messages ?")
        }
        AlertDialog(
            modifier = Modifier.background(Color.Transparent, shape = RoundedCornerShape(8.dp)),
            containerColor = colors.surface,
            tonalElevation = 0.dp,
            onDismissRequest = { viewModel.cancelDeleteDialog() },
            title = { Text("Confirm Delete", color = colors.onSurface) },
            text = { Text(styledText, color = colors.onSurface) },
            confirmButton = {
                TextButton(onClick = { viewModel.confirmDeleteSelectedMessages() }) {
                    Text("Delete", color = colors.onSurface)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.cancelDeleteDialog() }) {
                    Text("Cancel", color = colors.onSurface)
                }
            }
        )
    }

    DisposableEffect(topicId) {
        onDispose {
            topBarViewModel.setCustomIcons(emptyList())
            viewModel.resetState()
        }
    }
}