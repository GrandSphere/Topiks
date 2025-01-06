package com.example.topics2.ui.components.noteDisplay

import android.util.Log
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
    var inputText by remember { mutableStateOf("" ) }

    val shouldUpdate by viewModel.shouldupdate.collectAsState()
    //val inputText by viewModel.tempMessage.collectAsState(tempAbc)
    val messagePriority = 0
    val colors = MaterialTheme.colorScheme
    val density = LocalDensity.current.density // Get screen density
    //var inputText by remember { mutableStateOf("" ) }

    val sPlaceHolder = "Type a message..."
    //val iMaxLines = 5
    val vFontSize: TextUnit = 18.sp // You can change this value as needed
    val vButtonSize: Dp = 40.dp // You can change this value as needed
    val vIconSize: Dp = 25.dp // You can change this value as needed
    //val vMaxLinesSize: Dp = (vFontSize.value * density).dp *iMaxLines+ 6.dp
    val vMaxLinesSize: Dp = 80.dp

    val vLineHeight : TextUnit = 20.sp // You can change this value as needed
    //val iButtonSize=40.dp

    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }

    // Focus change listener to update isFocused state
    val focusModifier = Modifier
        .focusRequester(focusRequester)
        .onFocusChanged { focusState ->
            isFocused = focusState.isFocused
        }



    var tempMessageID by remember { mutableStateOf(-1 ) }
    //var tempMessageID: Int// = -1
    LaunchedEffect(shouldUpdate) {
        if (viewModel.shouldupdate.value) {
            inputText = viewModel.tempMessage.value
            viewModel.setShouldUpdate(false)
            tempMessageID=viewModel.tempMessageId.value
            viewModel.setTempMessageId(-1)

        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()

            //.background(Color.Red)
            .padding(top = 3.dp, start = 5.dp, end = 0.dp, bottom = 3.dp),
            //verticalAlignment = Alignment.CenterVertically
    ) {
        CustomTextBox(
            inputText = inputText,
            onValueChange = { newText -> inputText = newText },
            vMaxLinesSize = vMaxLinesSize,
            vFontSize = vFontSize,
            sPlaceHolder = sPlaceHolder,
            isFocused = true, // Can dynamically change based on focus state
            focusModifier = focusModifier, // Pass the focus modifier here
            boxModifier = Modifier
                .weight(1f)
                .padding(0.dp)
                .align(Alignment.CenterVertically) // Align button vertically in the center
        )

        Spacer(modifier = Modifier.width(8.dp))
        IconButton( // ADD BUTTON
            onClick = {
                // Handle button click (e.g., show file picker or attachment options)
            },
            modifier = Modifier.size(vButtonSize)
                .align(Alignment.Bottom) // Align button vertically in the center

        ) {
            Icon(
                imageVector = Icons.Filled.Add, // Attach file icon
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
                //.height(30.dp)
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
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}