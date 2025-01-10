package com.example.topics2.ui.screens

import MessageBubble
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
import com.example.topics2.ui.viewmodels.MessageViewModel
import com.example.topics2.unused.OLDMessageBubble

//import com.example.topics2.ui.viewmodels.TopicViewModel

@Composable
fun MessageScreen(navController: NavController,
                  viewModel: MessageViewModel,
                  topicId: Int, topicColor: Color= MaterialTheme.colorScheme.tertiary) {

    viewModel.collectMessages(topicId)


    val messages by viewModel.messages.collectAsState()
    var inputBarHeightPx by remember { mutableStateOf(0) }

    val scrollState = rememberLazyListState()
    val topicFontColor = chooseColorBasedOnLuminance(topicColor)
    val density = LocalDensity.current
    val inputBarHeight = with(density) { inputBarHeightPx.toDp() }


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
                MessageBubble(navController)
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


/*

fun MessageBubble( // New Message Bubble
    //topicColor: Color = MaterialTheme.colorScheme.tertiary,
    navController: NavController,
    topicColor: Color = Color.Cyan,
    topicFontColor: Color = Color.Black,
    messageContent: String,
    containPictures: Boolean,
    containAttachments: Boolean,
    listOfPictures: List<String>,
    listOfAttachments: List<String>,
    timestamp: String
) { // Main screen


    var messagecontent = messageContent
    val imagePaths: List<String> = listOfPictures
  //  val imagePaths = getTestImagePaths()
    val listOfAttachments: List<String> =  listOfAttachments
    val iPictureCount: Int = imagePaths.size
    var containsPictures: Boolean = containPictures
  //  var containsPictures: Boolean = true
    var containsAttachments: Boolean = containAttachments

    Log.d("DEBUG_LOG", "Message Content: $messagecontent")
    Log.d("DEBUG_LOG", "Image Paths: $imagePaths")
    Log.d("DEBUG_LOG", "List of Attachments: $listOfAttachments")
    Log.d("DEBUG_LOG", "Picture Count: $iPictureCount")
    Log.d("DEBUG_LOG", "Contains Pictures: $containsPictures")
    Log.d("DEBUG_LOG", "Contains Attachments: $containsAttachments")



 */