package com.example.topics2.ui.components.noteDisplay

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.topics2.ui.components.global.CustomTextBox
import com.example.topics2.ui.viewmodels.MessageViewModel
import kotlinx.coroutines.launch

@Composable
fun InputBarMessageScreen(
    navController: NavController, viewModel: MessageViewModel, topicId: Int?
) {
    val vFontSize: TextUnit = 18.sp // You can change this value as needed
    val vButtonSize: Dp = 40.dp // You can change this value as needed
    val vIconSize: Dp = 25.dp // You can change this value as needed
    val vMaxLinesSize: Dp = 80.dp

    var inputText by remember { mutableStateOf("" ) }
    val messagePriority = 0
    val colors = MaterialTheme.colorScheme

    val focusManager = LocalFocusManager.current // For clearing focus
    val toFocusTextbox by viewModel.ToFocusTextbox.collectAsState()
    val focusRequester = remember { FocusRequester() }


    var isFocused by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        //viewModel.setToFocusTextbox(true)
    }
    LaunchedEffect(toFocusTextbox) {
        if (toFocusTextbox) {
            focusRequester.requestFocus()
            // Reset focus state to false after focus is requested
            viewModel.setToFocusTextbox(false)
        } else {
            focusManager.clearFocus()

        }
    }

//    LaunchedEffect(toFocusTextbox) {
//        if (toFocusTextbox) {
//            focusRequester.requestFocus()
//            // Reset focus state to false after focus is requested
//            viewModel.setToFocusTextbox(false)
//        }
//    }


//    LaunchedEffect(Unit) {
////        viewModel.setToFocusTextbox(true)
////        kotlinx.coroutines.delay(100) // Optional: Give the UI time to adjust
////        viewModel.setToFocusTextbox(false)
//        //viewModel.setToFocusTextbox(false)
//        //focusRequester.requestFocus()
//    }
    // Focus change listener to update isFocused state
    val focusModifier = Modifier // Used to set edit cursor
        .focusRequester(focusRequester)
        .onFocusChanged { focusState ->
            isFocused = focusState.isFocused
        }

    var tempMessageID by remember { mutableStateOf(-1 ) }
    val amEditing by viewModel.amEditing.collectAsState()
    LaunchedEffect(amEditing) {
        if (viewModel.amEditing.value) {
            inputText = viewModel.tempMessage.value
            viewModel.setAmEditing(false)
            tempMessageID=viewModel.tempMessageId.value
            viewModel.setTempMessageId(-1)

        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 3.dp, start = 5.dp, end = 0.dp, bottom = 3.dp),
    ) {
        CustomTextBox(
            inputText = inputText,
            onValueChange = { newText -> inputText = newText },
            vMaxLinesSize = vMaxLinesSize,
            vFontSize = vFontSize,
            sPlaceHolder = "Type a message...",
            isFocused = true,
            focusModifier = focusModifier.focusRequester(focusRequester),
            boxModifier = Modifier
                .weight(1f)
                .padding(0.dp)
                .align(Alignment.CenterVertically)
        )

        Spacer(modifier = Modifier.width(8.dp))
        IconButton( // ADD BUTTON
            onClick = { },
            modifier = Modifier
                .size(vButtonSize)
                .align(Alignment.Bottom)
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Attach",
                tint = colors.tertiary,
                modifier = Modifier
                    .height(vIconSize)
            )
        }
        val coroutineScope = rememberCoroutineScope()
        Spacer(modifier = Modifier.width(5.dp))
        IconButton( // SEND BUTTON
            onClick = {
                viewModel.setToFocusTextbox(false)
                if (inputText.isNotBlank()) {
                    val tempInput = inputText
                    inputText = ""
                    if (tempMessageID > -1){ // Edit Mode
                        coroutineScope.launch {
                            viewModel.editMessage(
                                tempMessageID,
                                topicId,
                                tempInput,
                                messagePriority
                            )
                            tempMessageID=-1
                        }
                    }
                    else { //Send Mode
                        coroutineScope.launch {
                            viewModel.addMessage(topicId, tempInput, messagePriority)
                        }
                    }
                }
            },
            modifier = Modifier
                .size(vButtonSize)
                .fillMaxWidth(1f)
                .background(Color.Transparent)
                .align(Alignment.Bottom)
        ) {
            Icon(
                imageVector = if (tempMessageID > 0) Icons.Filled.Check else Icons.AutoMirrored.Filled.Send,
                //imageVector = Icons.Filled.Send, // Attach file icon
                contentDescription = "Attach",
                tint = colors.tertiary,
                modifier = Modifier
                    .size(vIconSize)
                //.aspectRatio(2.5f)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
    }
    // Request focus initially
}