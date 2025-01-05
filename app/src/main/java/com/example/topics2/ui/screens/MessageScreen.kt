package com.example.topics2.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.topics2.db.enitities.MessageTbl
import com.example.topics2.ui.components.noteDisplay.InputBarMessageScreen
import com.example.topics2.ui.components.noteDisplay.MessageBubble
import com.example.topics2.ui.viewmodels.MessageViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

//import com.example.topics2.ui.viewmodels.TopicViewModel

@Composable
fun MessageScreen(navController: NavController, messageViewModel: MessageViewModel, topicId: Int?) {
    val messages by messageViewModel.messages.collectAsState()
    messageViewModel.fetchMessages(topicId)
    val context = LocalContext.current
   //val coroutineScope = rememberCoroutineScope()
    //val messages = remember { mutableStateListOf<MessageTbl>() }
    val scrollState = rememberLazyListState()
    var inputBarHeightPx by remember { mutableStateOf(0) }
    val density = LocalDensity.current
    val inputBarHeight = with(density) { inputBarHeightPx.toDp() }

    Log.d("aabbccTopicId", topicId.toString())
    val focusManager = LocalFocusManager.current // For clearing focus

    // Fetch messages for the topic when the screen is shown
    //LaunchedEffect(topicId) {
    //    if (messages.isNotEmpty()) {
    //        //scrollState.scrollToItem(messages.size - 1)
    //        scrollState.scrollToItem(messages.size - 1)
    //    }
    //}

    // Scroll to the bottom when messages are updated
//    LaunchedEffect(messages.size) {
//        if (messages.isNotEmpty()) {
//            scrollState.animateScrollToItem(messages.size - 1)
//        }
//    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            //.background(Color.Red)
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
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
                MessageBubble(message = message, topicId = topicId, messageViewModel = messageViewModel)
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
                .background(Color.Transparent)
                .align(Alignment.BottomCenter)
                .onSizeChanged { size ->
                    inputBarHeightPx = size.height
                }
        ) {
           InputBarMessageScreen(navController = navController, messageViewModel = messageViewModel, topicId = topicId)
        }
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            //scrollState.animateScrollToItem(messages.size - 1)
                    scrollState.scrollToItem(messages.size - 1)
        }
    }

}

