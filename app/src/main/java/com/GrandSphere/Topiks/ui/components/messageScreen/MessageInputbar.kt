package com.GrandSphere.Topiks.ui.components.messageScreen
// Moved to viewmodel

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.GrandSphere.Topiks.ui.components.global.CustomButton
import com.GrandSphere.Topiks.ui.components.global.CustomTextBox
import com.GrandSphere.Topiks.ui.viewmodels.MessageViewModel
import com.GrandSphere.Topiks.utilities.helper.compareFileLists
import com.GrandSphere.Topiks.utilities.copyFileToUserFolder
import com.GrandSphere.Topiks.utilities.determineFileType
import com.GrandSphere.Topiks.utilities.getFileNameFromUri
import kotlinx.coroutines.launch
import multipleFilePicker

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun InputBarMessageScreen(
    viewModel: MessageViewModel,
    topicId: Int,
    topicColour: Color = MaterialTheme.colorScheme.onPrimary,
    onFocus: () -> Unit = {}
) {
    val vFontSize: TextUnit = 18.sp
    val vButtonSize: Dp = 40.dp
    val vClearButtonSize: Dp = 15.dp
    val vIconSize: Dp = 25.dp
    val vMaxLinesSize: Dp = 80.dp

    val context = LocalContext.current
    val colours = MaterialTheme.colorScheme
    val inputText by viewModel.inputText.collectAsState()
    val selectedFiles by viewModel.selectedFiles.collectAsState()
    val bEditMode by viewModel.bEditMode.collectAsState()
    val toFocusTextbox by viewModel.toFocusTextbox.collectAsState()

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val openFileLauncher = multipleFilePicker(
        onUserFilesSelected = { uris -> viewModel.addSelectedFiles(uris) }
    )

    LaunchedEffect(toFocusTextbox) {
        if (toFocusTextbox) {
            focusManager.clearFocus()
            focusRequester.requestFocus()
            viewModel.setToFocusTextbox(false)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.initializeInputBar()
    }

    Column {
        // File attachments
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 200.dp)
                .verticalScroll(rememberScrollState())
        ) {
            selectedFiles.forEachIndexed { index, attachment ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .background(colours.background)
                ) {
                    Text(
                        text = "\u2022 " + getFileNameFromUri(LocalContext.current, attachment),
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .padding(start = 1.dp, top = 5.dp, bottom = 5.dp, end = 10.dp),
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = topicColour
                        )
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = { viewModel.removeSelectedFile(index) },
                        modifier = Modifier
                            .size(vClearButtonSize)
                            .align(Alignment.Bottom)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = "Clear",
                            tint = topicColour,
                            modifier = Modifier.height(vClearButtonSize)
                        )
                    }
                }
            }
        }

        // Input bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 3.dp, start = 5.dp, end = 0.dp, bottom = 3.dp)
        ) {
            CustomTextBox(
                inputText = inputText,
                onValueChange = { viewModel.updateInputText(it) },
                vMaxLinesSize = vMaxLinesSize,
                vFontSize = vFontSize,
                sPlaceHolder = "Type a message...",
                isFocused = true,
                focusModifier = Modifier.focusRequester(focusRequester),
                boxModifier = Modifier
                    .weight(1f)
                    .padding(0.dp)
                    .align(Alignment.CenterVertically),
                onFocus = onFocus
            )

            Spacer(modifier = Modifier.width(8.dp))

            CustomButton(
                onClick = { openFileLauncher.launch(arrayOf("*/*")) },
                buttonModifier = Modifier
                    .size(vButtonSize)
                    .align(Alignment.Bottom),
                imageVector = Icons.Filled.Add,
                contentDescription = "Attach",
                tint = topicColour,
                iconModifier = Modifier.height(vIconSize)
            )

            Spacer(modifier = Modifier.width(5.dp))

            CustomButton(
                onClick = { viewModel.sendMessage(topicId, context) },
                onLongPress = { viewModel.clearInputBar() },
                buttonModifier = Modifier
                    .size(vButtonSize)
                    .align(Alignment.Bottom),
                imageVector = if (bEditMode) Icons.Filled.Check else Icons.AutoMirrored.Filled.Send,
                contentDescription = "Send",
                tint = topicColour,
                iconModifier = Modifier.size(vIconSize)
            )

            Spacer(modifier = Modifier.width(12.dp))
        }
    }
}