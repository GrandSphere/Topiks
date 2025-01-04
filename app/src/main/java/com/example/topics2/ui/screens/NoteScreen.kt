package com.example.topics2.ui.screens

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.topics2.model.enitities.MessageTbl
import com.example.topics2.ui.components.noteDisplay.InputBarNoteScreen
import com.example.topics2.ui.viewmodels.TopicViewModel
import java.text.SimpleDateFormat
import java.util.Locale

//import com.example.topics2.ui.viewmodels.TopicViewModel

@Composable
fun NoteScreen(navController: NavController, viewModel: TopicViewModel) {
    val topicId = "changethis"
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val messages = remember { mutableStateListOf<MessageTbl>() }
    val scrollState = rememberLazyListState()
    var inputBarHeightPx by remember { mutableStateOf(0) }
    val density = LocalDensity.current
    val inputBarHeight = with(density) { inputBarHeightPx.toDp() }

    val focusManager = LocalFocusManager.current // For clearing focus

    // Fetch messages for the topic when the screen is shown
    LaunchedEffect(topicId) {
        //val fetchedMessages = messageController.getMessagesForTopic(topicId)
        //messages.clear()
        //messages.addAll(fetchedMessages)

        if (messages.isNotEmpty()) {
            scrollState.scrollToItem(messages.size - 1)
        }
    }

    // Scroll to the bottom when messages are updated
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            scrollState.animateScrollToItem(messages.size - 1)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            }
    ) {
        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = inputBarHeight)
        ) {
            items(messages.size) { index ->
                val message = messages[index]
                MessageBubble(message)
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
            //InputBarNoteScreen {
               // messageText ->
               // coroutineScope.launch {
               //     messageController.addMessage(topicId, messageText, 1)
               //     topicController.updateLastModified(topicId, System.currentTimeMillis())

               //     val newMessages = messageController.getMessagesForTopic(topicId)
               //     messages.clear()
               //     messages.addAll(newMessages)

                    //if (messages.isNotEmpty()) {
                        //scrollState.animateScrollToItem(messages.size - 1)
                    //}
                //}
            //}
        }
    }
}

@Composable
fun MessageBubble(
    message: MessageTbl,
    cColor: Color = MaterialTheme.colorScheme.secondary  // Default to secondary color from theme
) {
    // Format timestamp (you can format this as needed)
    val formattedTimestamp =
        SimpleDateFormat("HH:mm dd/MM/yy", Locale.getDefault()).format(message.messageTimestamp)

    //val colors = MaterialTheme.colorScheme // Use the theme color scheme
    Row(
        modifier = Modifier
            .fillMaxWidth()
            //.background(Color.Red)
            .padding(3.dp),  // Reduced padding between messages

        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Message bubble with some padding and rounded corners
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = cColor,
            //color = Color.Red,
            modifier = Modifier.padding(1.dp),
            tonalElevation = 0.dp, // Remove shadow
            border = null // Remove border
        ) {
            Column(
                modifier = Modifier
                    //.fillMaxWidth() //messages take up entire width
                    //.background(Color.Red)
                    .padding(6.dp), //space around message
            ) {
                Text(
                    text = message.messageContent,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.height(1.dp)) //space between message and date
                Text(
                    text = formattedTimestamp,
                    style = MaterialTheme.typography.bodySmall,
                    // color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
                )
            }
        }
    }
}
