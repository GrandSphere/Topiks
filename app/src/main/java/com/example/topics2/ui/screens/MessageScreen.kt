package com.example.topics2.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.focus.onFocusChanged
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
import com.example.topics.utilities.determineFileType
import com.example.topics2.db.entities.FileInfoWithIcon
import com.example.topics2.model.dataClasses.CustomIcon
import com.example.topics2.ui.components.CustomSearchBox
import com.example.topics2.ui.components.addTopic.chooseColorBasedOnLuminance
import com.example.topics2.ui.components.messageScreen.InputBarMessageScreen
import com.example.topics2.ui.components.messageScreen.MessageBubble
import com.example.topics2.ui.viewmodels.GlobalViewModelHolder
import com.example.topics2.ui.viewmodels.MenuItem
import com.example.topics2.ui.viewmodels.MessageViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.time.toDuration

@Composable
fun MessageScreen(navController: NavController, viewModel: MessageViewModel, topicId: Int,
                  messageId: Int = -1, topicColor: Color= MaterialTheme.colorScheme.tertiary) {
    viewModel.collectMessages(topicId)
    viewModel.setTopicColor(topicColor)
    viewModel.setTopicId(topicId)

    var showDialog by remember { mutableStateOf(false) }
    val messages by viewModel.messages.collectAsState()
    var inputBarHeightPx by remember { mutableStateOf(0) }
    val toastMessage by viewModel.toastMessage.collectAsState()
    val focusManager = LocalFocusManager.current // For clearing focus
    val focusRequester = remember { FocusRequester() }
    val selectMultiple by viewModel.multipleMessageSelected.collectAsState()
    val colors = MaterialTheme.colorScheme
    val coroutineScope = rememberCoroutineScope() // this should be passed from messagescreen
    val scrollState = rememberLazyListState()
    val topicFontColor = chooseColorBasedOnLuminance(topicColor)
    viewModel.setTopicFontColor(topicFontColor)
    val density = LocalDensity.current
    val inputBarHeight = with(density) { inputBarHeightPx.toDp() } // TODO this needs to go, might still be needed when we finally fix scrolling
    val context = LocalContext.current
    var showMenu: Boolean by remember { mutableStateOf(false ) }

    var showSearchNav: Boolean by remember { mutableStateOf(true ) }
    var bSearch: Boolean by remember { mutableStateOf(false ) }
    var inputText by remember{ mutableStateOf("")}
    val searchResults by viewModel.searchResults.observeAsState(emptyList())
    var searchResultCount: Int by remember { mutableStateOf(0 ) }

    var iTempMessage by remember { mutableStateOf(-1) }
    var bDeleteEnabled:Boolean by remember { mutableStateOf(false) }
    val topBarViewModel = GlobalViewModelHolder.getTopBarViewModel()

    val selectedMessageIds = remember { mutableStateOf<Set<Int>>(emptySet()) }


    val focusModifier = Modifier // Used to set edit cursor
        .focusRequester(focusRequester)

    var toFocusSearchBox by  remember { mutableStateOf(false) }
    LaunchedEffect(toFocusSearchBox) {
        if (toFocusSearchBox) {
            focusManager.clearFocus()
            focusRequester.requestFocus()
            toFocusSearchBox = false
        }
    }
    // Show toast message when state changes
    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }
    LaunchedEffect(Unit) {
        topBarViewModel.setCustomIcons(listOf(
            CustomIcon(icon = Icons.Default.Search,
                {
                    bSearch = !bSearch
                    iTempMessage=-1
                    if (bSearch) { toFocusSearchBox = true } else {toFocusSearchBox = false}
                },
                "Search"
            )
        ))
        topBarViewModel.setMenuItems(
            listOf(
                MenuItem("Search") {
                    bSearch = !bSearch
                    iTempMessage=-1
                    if (bSearch) { toFocusSearchBox = true } else {toFocusSearchBox = false}
                },
                MenuItem("Select Messages") {
                    viewModel.setMultipleMessageSelected(!selectMultiple)
                },
                MenuItem( "Enable Delete") {
                    bDeleteEnabled= !bDeleteEnabled
                },
                MenuItem("Back") {
                    navController.popBackStack()
                },
            )
        )
    }
    LaunchedEffect(bDeleteEnabled){
        if (bDeleteEnabled)
        {
            topBarViewModel.updateMenuItem("Enable Delete",
                MenuItem("Disable Delete", {bDeleteEnabled = false}) )
        }
        else {
            topBarViewModel.updateMenuItem("Disable Delete",
                MenuItem("Enable Delete", {bDeleteEnabled= true}) )
        }
    }
    LaunchedEffect(selectMultiple){
        if (selectMultiple){
            topBarViewModel.setCustomIcons(
                listOf(
                    CustomIcon(Icons.Default.SelectAll, {
                        if (selectedMessageIds.value.size < messages.size) {
                            selectedMessageIds.value = viewModel._messageIndexMap.value.keys
                        }
                        else {
                            selectedMessageIds.value = emptySet()
                        }
                    }, "Select All"),
                    CustomIcon(Icons.Default.ImportExport, {
                        coroutineScope.launch {
                            viewModel.exportMessagesToPDF(selectedMessageIds.value)
                        }
                    }, "Export Selected Messages"),
                    CustomIcon(Icons.Default.Delete, {
                        showDialog = true
                    }, "Delete Selected Messages"),
                )
            )

        } else{
//     topBarViewModel.setCustomIcons(emptyList())
            topBarViewModel.setCustomIcons(listOf(
                CustomIcon(icon = Icons.Default.Search,
                    {
                        bSearch = !bSearch
                        iTempMessage=-1
                        if (bSearch) { toFocusSearchBox = true } else {toFocusSearchBox = false}
                    },
                    "Search"
                )
            ))
            selectedMessageIds.value = emptySet()
        }

    }

    fun scrollMessage()
    {
        iTempMessage=-1
        if ((searchResults.isEmpty()) || (searchResultCount > searchResults.size) || (searchResultCount < 0)) {
            return
        }

        if (searchResultCount == 0){ searchResultCount = 1}
        val messageId = searchResults[searchResultCount -1].id
        val messageIndex = viewModel.getMessageIndexFromID(messageId)

        if (messageIndex >= 0) {
            iTempMessage=messageIndex
            coroutineScope.launch {
                scrollState.scrollToItem(messageIndex)
            }

        }
    }

    LaunchedEffect(searchResults) {
        if(searchResults.isEmpty())
        {
            searchResultCount = 0
        }
        else {
            searchResultCount = searchResults.size
            scrollMessage()
        }
    }
    LaunchedEffect(inputText) {

        if (inputText.length > 0) {
            viewModel.messageSearch(inputText)
        }
    }
    LaunchedEffect(messages.size) {
        if (messageId != -1) {
            val messageIndex = viewModel.getMessageIndexFromID(messageId)
            if (messageIndex >= 0) {
                scrollState.scrollToItem(messageIndex)
            }
        } else {
            // Scroll to the last message if no specific message ID is selected
            if (messages.isNotEmpty()) {
                scrollState.scrollToItem(messages.size - 1)
            }
        }
    }
    Column(
        modifier = Modifier
            .padding(top = 0.dp)
            .fillMaxSize()
            //.background(Color.Red)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    viewModel.setToFocusTextbox(false)
                })
            }
    ) {
//        bSearch= true
        if (bSearch) {
            CustomSearchBox(
                focusModifier = focusModifier.focusRequester(focusRequester),
                inputText = inputText,
                sPlaceHolder = "Search Messages...",
                onValueChange = { newText ->
                    inputText = newText
                    viewModel.messageSearch(newText)
                },
                bShowSearchNav = showSearchNav,
                onNextClick = {
                    if ( searchResultCount  < searchResults.size) {
                        searchResultCount++
                        scrollMessage()
                    } else{
                        searchResultCount = 0
                        scrollMessage()
                    }
                },
                onPreviousClick = {
                    if (searchResultCount > 1) {
                        searchResultCount--
                        scrollMessage()
                    } else{
                        searchResultCount = searchResults.size
                        scrollMessage()
                    }
                },
                onClick = { toFocusSearchBox = true},
                iSearchCount = searchResults.size,
                iCurrentSearch = searchResultCount,
            )
        }

        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .pointerInput(Unit) {
                    detectTapGestures(onPress = {
                        focusManager.clearFocus()
                    })
                }
//                .fillMaxSize()
                .weight(1f)
                .fillMaxWidth()
                //.background(Color.Red)
                .padding(bottom = inputBarHeight)
        ) {

            // Checks attachments and photos before sending to messageBubble
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
                        "Image" -> { // Contain Picture
                            pictureList.add(fileInfo)
                            hasPictures = true
                        }

                        else -> { // Contain other file types
                            attachmentList.add(filePath)
                            hasAttachments = true
                        }
                    }
                }

                // Format timestamp
                val timestamp = SimpleDateFormat(
                    "yyyy-MM-dd HH:mm",
                    Locale.getDefault()
                ).format(message.createTime)

                // Call MessageBubble
                var messageChecked: Boolean by remember { mutableStateOf(false) }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start ,
                    modifier = Modifier
                        .padding(0.dp)
                ) {
                    var highlightedSearchText by remember { mutableStateOf<AnnotatedString?>(null) }
                    highlightedSearchText=null
//                    if (index==iTempMessage)
                    if ((bSearch) && (index == iTempMessage))
                    { // Put Search highlighting here
                        highlightedSearchText = buildAnnotatedString {
                            val normalizedQuery = inputText.split(" ").map { it.trim() }.filter { it.isNotEmpty() }
                            val contentWords = message.content.split(" ")

                            contentWords.forEach { word ->
                                var currentIndex = 0 // Track the current position in the word

                                normalizedQuery.forEach { substring ->
                                    while (true) {
                                        val matchIndex = word.indexOf(substring, currentIndex, ignoreCase = true)
                                        if (matchIndex == -1) break

                                        // Append the part before the match
                                        withStyle(style = SpanStyle(color = topicFontColor)) {
                                            append(word.substring(currentIndex, matchIndex))
                                        }

                                        // Append the matched part
//                                       withStyle(style = SpanStyle(background = colors.surfaceVariant ,color = colors.onSurfaceVariant)) {
//                                       withStyle(style = SpanStyle(background = topicFontColor.copy(alpha=0.3f) ,color = topicFontColor)) {
                                        withStyle(style = SpanStyle(background = topicFontColor ,color = topicColor)) {
                                            append(word.substring(matchIndex, matchIndex + substring.length))
                                        }

                                        // Move the index forward
                                        currentIndex = matchIndex + substring.length
                                    }
                                }

                                // Append the remaining part of the word
                                if (currentIndex < word.length) {
                                    withStyle(style = SpanStyle(color = topicFontColor)) {
                                        append(word.substring(currentIndex))
                                    }
                                }
                                append(" ") // Add space between words
                            }
                        }
                    }

                    val contentToDisplay = highlightedSearchText ?: AnnotatedString(message.content)
                    if (selectMultiple) {
                        Checkbox(
                            checked =  (message.id in selectedMessageIds.value),
                            onCheckedChange = {
                                messageChecked = it
                                if ( messageChecked ) {
                                    selectedMessageIds.value += message.id
                                } else
                                {
                                    selectedMessageIds.value -= message.id
                                }
                            },
                            modifier = Modifier
                                .scale(0.75f)
                                .padding(start = 10.dp, end = 4.dp, bottom = 0.dp, top = 0.dp)
                                .size(1.dp),
                            colors = CheckboxDefaults.colors(
                                checkedColor = topicColor,
                                uncheckedColor = colors.onBackground,
                                checkmarkColor = topicFontColor,
                            )
                        )
                    }

                    MessageBubble(
                        onFocusClear = {focusManager.clearFocus()},
                        navController = navController,
                        topicColor = topicColor,
                        topicFontColor = topicFontColor,
                        annotatedMessageContent = contentToDisplay,
//                        messageContent = if (bSearch) "a"  else  message.content,
//                        messageContent = message.content,
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
                            if(message.id in selectedMessageIds.value) {
                                selectedMessageIds.value -= message.id
                                messageChecked = false
                            } else{
                                messageChecked = true
                                selectedMessageIds.value += message.id
                            }
                        } ,
                        onViewMessage = {
                            //TemporaryDataHolder.setMessage(message.content)
                            viewModel.setTempMessageId(message.id)
                            navController.navigate("navViewMessage")
                        },
                        onEditClick = {
                            viewModel.setTempMessageId(message.id)
                            viewModel.setEditMode(true)
                        },
                        onExportClick = {
                            coroutineScope.launch {
                                viewModel.exportMessagesToPDF(setOf(message.id))}
                        },
                        bDeleteEnabled = bDeleteEnabled,
                        messageSelected = messageChecked,
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
            //.background(Color.Transparent)
//                .align(Alignment.BottomCenter)
//                .align(Alignment.Bottom)
//                .onSizeChanged { size ->
//                    inputBarHeightPx = size.height
//                }
        ) {
            InputBarMessageScreen(navController = navController, viewModel = viewModel, topicId = topicId, topicColour = topicColor)
        }
    }
    LaunchedEffect(showMenu) {

    }


    if (showDialog) {
        val styledText = buildAnnotatedString {
            append("Delete ") // Default text
//                withStyle(style = SpanStyle(color = argbToColor(topic.colour))) {
            withStyle(style = SpanStyle(color = colors.error)) {
                append(selectedMessageIds.value.size.toString()) // Styled part (topic.name)
            }
            append(" selected messages ?") // End of the text
        }
        AlertDialog(

            modifier = Modifier
                .background(Color.Transparent, shape = RoundedCornerShape(8.dp)),
//                containerColor = colors.background,
            containerColor = colors.surface,
//                 containerColor = Color.Red.copy(alpha = 0.7f),
            tonalElevation = 0.dp,
            onDismissRequest = { showDialog = false }, // Close on outside tap
            title = { Text("Confirm Delete", color = colors.onSurface) },
            text = {
                Text(
                    styledText,
                    color = colors.onSurface
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    coroutineScope.launch {
                        viewModel.deleteMultipleMessages(selectedMessageIds.value)
                        viewModel.setMultipleMessageSelected(false)
                        selectedMessageIds.value = emptySet()
                    }
                }) {
                    Text("Delete", color = colors.onSurface)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel", color = colors.onSurface)
                }
            }
        )
    }
    DisposableEffect(topicId) {
        onDispose {
            topBarViewModel.setCustomIcons(emptyList())
//            topBarViewModel.setMenuItems(emptyList())
            viewModel.resetState()
        }
    }
}