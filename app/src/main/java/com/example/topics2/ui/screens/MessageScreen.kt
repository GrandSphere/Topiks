package com.example.topics2.ui.screens

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.topics2.ui.components.global.chooseColorBasedOnLuminance
import com.example.topics2.ui.components.noteDisplay.InputBarMessageScreen
import com.example.topics2.ui.components.noteDisplay.MessageBubble
import com.example.topics2.ui.viewmodels.MessageViewModel
import com.example.topics2.unused.OLDMessageBubble

//import com.example.topics2.ui.viewmodels.TopicViewModel

@Composable
fun MessageScreen(navController: NavController, viewModel: MessageViewModel, topicId: Int, topicColor: Color= MaterialTheme.colorScheme.tertiary) {

    viewModel.collectMessages(topicId)


    val messages by viewModel.messages.collectAsState()
    var inputBarHeightPx by remember { mutableStateOf(0) }

    val scrollState = rememberLazyListState()
    val topicFontColor = chooseColorBasedOnLuminance(topicColor)
    val density = LocalDensity.current
    val inputBarHeight = with(density) { inputBarHeightPx.toDp() }
    //val toFocusTextbox by viewModel.ToFocusTextbox.collectAsState()
//
//
//    val toFocusTextbox by viewModel.ToFocusTextbox.collectAsState()
//    val focusRequester = remember { FocusRequester() }

//    LaunchedEffect(toFocusTextbox) {
//        if (toFocusTextbox) {
//            //focusRequester.requestFocus()
//            // Reset focus state to false after focus is requested
//            viewModel.setToFocusTextbox(false)
//        }
//        else{
//            //focusManager.clearFocus()
//
//        }
//    }

//    LaunchedEffect(Unit) {
//
//        viewModel.setToFocusTextbox(true)
//        kotlinx.coroutines.delay(100) // Optional: Give the UI time to adjust
//        viewModel.setToFocusTextbox(false)
//        focusManager.clearFocus()
//    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            //.background(Color.Red)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    viewModel.setToFocusTextbox(false)
                })
            }
    ) {
        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .fillMaxSize()
                //.background(Color.Red)
                .padding(bottom = inputBarHeight)
        ) {
            items(messages.size) { index ->
                val message = messages[index]
//                OLDMessageBubble(
//                    message = message,
//                    topicId = topicId,
//                    viewModel = viewModel,
//                    topicColor = topicColor,
//                    topicFontColor = topicFontColor,
//                )
                MessageBubble(
                    navController = navController,
                    messageContent = message.content,
                    containPictures = false,
                    containAttachments = false,
                    listOfPictures = emptyList(),
                    listOfAttachments = emptyList()
                    )
                //Log.d("aabbcc",message)
            }
            item {
                Spacer(
                    modifier = Modifier
                        .height(0.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                //.background(Color.Transparent)
                .align(Alignment.BottomCenter)
                .onSizeChanged { size ->
                    inputBarHeightPx = size.height
                }
        ) {
            InputBarMessageScreen(navController = navController, viewModel = viewModel, topicId = topicId, topicColour = topicColor)
        }
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            //scrollState.animateScrollToItem(messages.size - 1)
            scrollState.scrollToItem(messages.size - 1)
        }
    }




}