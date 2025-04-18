package com.GrandSphere.Topiks.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ImportExport
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SelectAll
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.GrandSphere.Topiks.db.entities.FileInfoWithIcon
import com.GrandSphere.Topiks.model.dataClasses.CustomIcon
import com.GrandSphere.Topiks.ui.components.CustomSearchBox
import com.GrandSphere.Topiks.ui.components.addTopic.chooseColorBasedOnLuminance
import com.GrandSphere.Topiks.ui.components.messageScreen.InputBarMessageScreen
import com.GrandSphere.Topiks.ui.components.messageScreen.MessageBubble
import com.GrandSphere.Topiks.ui.focusClear
import com.GrandSphere.Topiks.ui.viewmodels.GlobalViewModelHolder
import com.GrandSphere.Topiks.ui.viewmodels.MenuItem
import com.GrandSphere.Topiks.ui.viewmodels.MessageViewModel
import com.GrandSphere.Topiks.utilities.determineFileType
import com.GrandSphere.Topiks.utilities.helper.highlightSearchText
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun MessageScreen(
    navController: NavController,
    viewModel: MessageViewModel,
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
    val searchResultCount by viewModel.searchResultCount.collectAsState()
    val currentSearchMessageIndex by viewModel.currentSearchMessageIndex.collectAsState()
    val isDeleteEnabled by viewModel.isDeleteEnabled.collectAsState()
    val selectedMessageIds by viewModel.selectedMessageIds.collectAsState()
    val showDeleteDialog by viewModel.showDeleteDialog.collectAsState()
    val requestSearchFocus by viewModel.requestSearchFocus.collectAsState()
    val topicFontColor:Color by viewModel.topicFontColor.collectAsState()
    val context = LocalContext.current
    val topBarViewModel = GlobalViewModelHolder.getTopBarViewModel()
    var inputBarHeightPx by remember { mutableStateOf(0) }
    val density = LocalDensity.current
    val inputBarHeight = with(density) { inputBarHeightPx.toDp() }

    // Initialize ViewModel
    LaunchedEffect(Unit) {
        viewModel.initialize(
            topicId = topicId,
            topicColor = topicColor,
            messageId = messageId,
        )
        viewModel.updateTopBar(topBarViewModel)
    }

    // Handle search focus
    LaunchedEffect(requestSearchFocus) {
        if (requestSearchFocus) {
            focusManager.clearFocus()
            focusRequester.requestFocus()
            viewModel.clearSearchFocusRequest()
        }
    }

    // Show toast message
    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    // Scroll to message on search or initial load
    LaunchedEffect(searchResults, messages.size) {
        if (isSearchActive && searchResults.isNotEmpty() && currentSearchMessageIndex >= 0) {
            scrollState.scrollToItem(currentSearchMessageIndex)
        } else if (messageId != -1) {
            val messageIndex = viewModel.getMessageIndexFromID(messageId)
            if (messageIndex >= 0) {
                scrollState.scrollToItem(messageIndex)
            }
        } else if (messages.isNotEmpty()) {
            scrollState.scrollToItem(messages.size - 1)
        }
    }
    LaunchedEffect (selectMultiple){
        viewModel.updateTopBar(topBarViewModel)
    }
    // Update top bar on delete toggle
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
                val pictureList = mutableListOf<FileInfoWithIcon>()
                val attachmentList = mutableListOf<String>()
                var hasPictures = false
                var hasAttachments = false
                val filesForMessage by viewModel.getFilesByMessageIdFlow(message.id)
                    .collectAsState(initial = emptyList())
                for (fileInfo in filesForMessage) {
                    val filePath = fileInfo.filePath
                    val fileType = determineFileType(context, filePath.toUri())
                    when (fileType) {
                        "Image" -> {
                            pictureList.add(fileInfo)
                            hasPictures = true
                        }
                        else -> {
                            attachmentList.add(filePath)
                            hasAttachments = true
                        }
                    }
                }

                val timestamp = SimpleDateFormat(
                    "yyyy-MM-dd HH:mm",
                    Locale.getDefault()
                ).format(message.createTime)

                var messageChecked by remember { mutableStateOf(message.id in selectedMessageIds) }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.padding(0.dp)
                ) {
                    val contentToDisplay = if (isSearchActive && index == currentSearchMessageIndex) {
                        highlightSearchText(
                            messageContent = message.content,
                            searchQuery = searchQuery,
                            topicColor = topicColor,
                            topicFontColor = topicFontColor
                        )
                    } else {
                        AnnotatedString(message.content)
                    }
                    if (selectMultiple) {
                        Checkbox(
                            checked = message.id in selectedMessageIds,
                            onCheckedChange = {
                                Log.d("QQEE: " , "Supposed to check here")
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
                        annotatedMessageContent = contentToDisplay,
                        containsPictures = hasPictures,
                        selectMultile = selectMultiple,
                        containsAttachments = hasAttachments,
                        listOfPictures = pictureList,
                        listOfAttachmentsP = attachmentList,
                        timestamp = timestamp,
                        onDeleteClick = {
                            coroutineScope.launch {
                                viewModel.deleteMessage(message.id)
                            }
                        },
                        onSelectedClick = {
                            viewModel.setMultipleMessageSelected(true)
                            viewModel.toggleMessageSelection(message.id)
                            messageChecked = message.id in selectedMessageIds
                        },
                        onViewMessage = {
                            viewModel.setTempMessageId(message.id)
                            navController.navigate("navViewMessage")
                        },
                        onEditClick = {
                            viewModel.setTempMessageId(message.id)
                            viewModel.setEditMode(true)
                        },
                        onExportClick = {
                            coroutineScope.launch {
                                viewModel.exportMessagesToPDF(setOf(message.id))
                            }
                        },
                        bDeleteEnabled = isDeleteEnabled,
                        messageSelected = messageChecked
                    )
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            InputBarMessageScreen(
                navController = navController,
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
