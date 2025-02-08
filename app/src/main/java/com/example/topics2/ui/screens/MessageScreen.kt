package com.example.topics2.ui.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.example.topics.utilities.determineFileType
import com.example.topics2.db.entities.FileInfoWithIcon
import com.example.topics2.ui.components.CustomSearchBox
import com.example.topics2.ui.components.global.CustomTextBox
import com.example.topics2.ui.components.global.chooseColorBasedOnLuminance
import com.example.topics2.ui.components.messageScreen.InputBarMessageScreen
import com.example.topics2.ui.components.messageScreen.MessageBubble
import com.example.topics2.ui.viewmodels.MessageViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun MessageScreen(navController: NavController, viewModel: MessageViewModel, topicId: Int,
                  messageId: Int = -1, topicColor: Color= MaterialTheme.colorScheme.tertiary) {
    viewModel.collectMessages(topicId)
    viewModel.setTopicColor(topicColor)
    viewModel.setTopicId(topicId)

    val messages by viewModel.messages.collectAsState()
    var inputBarHeightPx by remember { mutableStateOf(0) }

    val coroutineScope = rememberCoroutineScope() // this should be passed from messagescreen
    val scrollState = rememberLazyListState()
    val topicFontColor = chooseColorBasedOnLuminance(topicColor)
    viewModel.setTopicFontColor(topicFontColor)
    val density = LocalDensity.current
    val inputBarHeight = with(density) { inputBarHeightPx.toDp() } // TODO this needs to go, might still be needed when we finally fix scrolling
    val context = LocalContext.current
    var showMenu: Boolean by remember { mutableStateOf(false ) }
var bSearch: Boolean by remember { mutableStateOf(false ) }
    var inputText by remember{ mutableStateOf("")}
    val searchResults by viewModel.searchResults.observeAsState(emptyList())

    LaunchedEffect(messages.size) {
        if (messageId != -1) {
            val messageIndex = viewModel.getMessageIndexFromID(messageId)
            if (messageIndex >= 0) {
                Log.d("QQWWEE", "Scrolling to messageID: ${messageId}, and messageIndex: ${messageIndex}")
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
            .fillMaxSize()
            //.background(Color.Red)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    viewModel.setToFocusTextbox(false)
                })
            }
    ) {


        bSearch= true
        if (bSearch) {

            CustomSearchBox(
                inputText = inputText,
                sPlaceHolder = "Search Topics...",
                onValueChange = { newText ->
                    inputText = newText
                    viewModel.messageSearch(newText)
                },
                bShowSearchNav = true,
            )
        }

        LazyColumn(
            state = scrollState,
            modifier = Modifier
//                .fillMaxSize()
                .weight(1f)
                .fillMaxWidth()
                //.background(Color.Red)
                .padding(bottom = inputBarHeight)
        ) {

            if (inputText.length > 0) {
                viewModel.messageSearch(inputText)
                items(searchResults.size) { index: Int ->
                    val messageId: Int = searchResults[index].id
                    val messageObj = viewModel.getMessageObjectById(messageId)

                    val timestamp =
                        SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(11111111)
                    if (messageObj != null)
                    {

                        Log.d("QWERT","${messageObj.toString()}")
                    }
               }

            } else {
                // Checks attachments and photos before sending to messageBubble

                items(messages.size) { index ->
                    val message = messages[index]
                    //val pictureList = mutableListOf<String>()
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
                    MessageBubble(
                        navController = navController,
                        topicColor = topicColor,
                        topicFontColor = topicFontColor,
                        messageContent = message.content,
                        containsPictures = hasPictures,
                        containsAttachments = hasAttachments,
                        listOfPictures = pictureList,
                        listOfAttachmentsP = attachmentList,
                        timestamp = timestamp,
                        onDeleteClick = {
                            coroutineScope.launch {
                                viewModel.deleteMessage(message.id)
                            }
                        },
                        onViewMessage = {
                            //TemporaryDataHolder.setMessage(message.content)

                            viewModel.setTempMessageId(message.id)
                            navController.navigate("navViewMessage")
                        },
                        onEditClick = {
                            viewModel.setTempMessageId(message.id)
                            viewModel.setEditMode(true)
                        }
                    )
                }
//            item { Spacer( modifier = Modifier .height(0.dp) ) }
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
        Log.d("arst","showmenu changed")

    }
}
