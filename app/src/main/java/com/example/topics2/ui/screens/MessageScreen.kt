package com.example.topics2.ui.screens

import android.util.Log
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.topics2.ui.components.global.chooseColorBasedOnLuminance
import com.example.topics2.ui.components.noteDisplay.InputBarMessageScreen
import com.example.topics2.ui.components.noteDisplay.MessageBubble
import com.example.topics2.ui.viewmodels.MessageViewModel

//import com.example.topics2.ui.viewmodels.TopicViewModel

@Composable
fun MessageScreen(navController: NavController, viewModel: MessageViewModel, topicId: Int?, topicColor: Color= MaterialTheme.colorScheme.tertiary) {
    val topicFontColor = chooseColorBasedOnLuminance(topicColor)
    val messages by viewModel.messages.collectAsState()
    viewModel.fetchMessages(topicId)
    LocalContext.current
    val scrollState = rememberLazyListState()
    var inputBarHeightPx by remember { mutableStateOf(0) }
    val density = LocalDensity.current
    val inputBarHeight = with(density) { inputBarHeightPx.toDp() }

    Log.d("aabbccTopicId", topicId.toString())
    val focusManager = LocalFocusManager.current // For clearing focus

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
                MessageBubble(
                    message = message,
                    topicId = topicId,
                    viewModel = viewModel,
                    topicColor = topicColor,
                    topicFontColor = topicFontColor,
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
           InputBarMessageScreen(navController = navController, viewModel = viewModel, topicId = topicId)
        }
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            //scrollState.animateScrollToItem(messages.size - 1)
                    scrollState.scrollToItem(messages.size - 1)
        }
    }

}