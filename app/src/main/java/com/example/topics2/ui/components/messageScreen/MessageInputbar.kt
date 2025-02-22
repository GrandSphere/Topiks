package com.example.topics2.ui.components.messageScreen

import android.net.Uri
import android.util.Log
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
import androidx.navigation.NavController
import com.example.topics.utilities.copyFileToUserFolder
import com.example.topics.utilities.determineFileType
import com.example.topics2.ui.components.global.CustomButton
import com.example.topics2.ui.components.global.CustomTextBox
import com.example.topics2.ui.viewmodels.MessageViewModel
import com.example.topics2.unused.old.getFileNameFromString
import com.example.topics2.utilities.helper.compareFileLists
import kotlinx.coroutines.launch
import multipleFilePicker

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun InputBarMessageScreen(
    navController: NavController, viewModel: MessageViewModel, topicId: Int,
    topicColour: Color = MaterialTheme.colorScheme.onPrimary,
    onFocus: () -> Unit ={},
) {
    val vFontSize: TextUnit = 18.sp // You can change this value as needed
    val vButtonSize: Dp = 40.dp // You can change this value as needed
    val vClearButtonSize: Dp = 15.dp // You can change this value as needed
    val vIconSize: Dp = 25.dp // You can change this value as needed
    val vMaxLinesSize: Dp = 80.dp

    var inputText by remember { mutableStateOf("") }
    val messagePriority = 0
    val colours = MaterialTheme.colorScheme

    var isFocused by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current // For clearing focus
    val focusRequester = remember { FocusRequester() }
    val toFocusTextbox by viewModel.ToFocusTextbox.collectAsState()
    val toUnFocusTextbox by viewModel.ToFocusTextbox.collectAsState()
    val focusRequester2 = remember { FocusRequester() }
    var bEditedMode by remember { mutableStateOf(false) }

    val widthSetting = 500
    val heightSetting = 500
    // FilePicker Logic

    val selectedFileUris: MutableState<List<Uri>?> = remember { mutableStateOf(emptyList()) }
    val selectedFileUrisBeforeEdit: MutableState<List<Uri>?> = remember { mutableStateOf(emptyList()) }
    val openFileLauncher = multipleFilePicker(
        onUserFilesSelected = { uris ->
            selectedFileUris.value = (selectedFileUris.value ?: emptyList()) + (uris ?: emptyList())
        }
    )
    //var compareFilesResult: Pair<List<Uri>, List<Uri>> by remember {mutableStateOf(Pair(emptyList(), emptyList()))}
    LaunchedEffect(toFocusTextbox) {
        if (toFocusTextbox) {
            focusManager.clearFocus()
            focusRequester.requestFocus()
            viewModel.setToFocusTextbox(false)
        } else {
        }
    }

    LaunchedEffect(Unit) {
        // kotlinx.coroutines.delay(100) // Optional: Give the UI time to adjust
        viewModel.setEditMode(false)
        bEditedMode = false
        viewModel.setTempMessageId(-1)
        viewModel.setToFocusTextbox(true)
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

//    var tempMessageID by remember { mutableStateOf(viewModel.tempMessageId.value) }

    val tempMessageID: Int by viewModel.tempMessageId.collectAsState()
    val bEditMode by viewModel.bEditMode.collectAsState()
    val iNumToTake: Int = 10

    LaunchedEffect(bEditMode) {
        if (bEditMode) {
            bEditedMode = true
            inputText = viewModel.getMessageContentById(tempMessageID) ?: ""
            // use tempMessageID to get inputText
            selectedFileUris.value = viewModel.getFilesByMessageId(tempMessageID)
            selectedFileUrisBeforeEdit.value = selectedFileUris.value
            viewModel.setEditMode(false)
        }
        else {
            //viewModel.setTempMessageId(-1)
        }
    }

    Column() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 200.dp) // Set your maximum height here
                .verticalScroll(rememberScrollState()) // Makes the content scrollable
        ) {
            selectedFileUris.value?.forEachIndexed() { index, attachment ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                ) {
                    Text( // get text name from path
                        text = "\u2022 " + getFileNameFromString(attachment.toString()), // TODO do this somewhere it might be more effecient. Maybe even in database
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .padding(start = 1.dp, top = 5.dp, bottom = 5.dp, end = 10.dp)
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onPress = {
                                    },
                                    onLongPress = {
                                    }
                                )
                            },
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = topicColour,
//                    textDecoration = TextDecoration.Underline
                        )
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton( // CLEAR BUTTON
                        onClick = {
                            selectedFileUris.value =
                                selectedFileUris.value!!.toMutableList().apply { removeAt(index) }
                        },
                        modifier = Modifier
                            .size(vClearButtonSize)
                            //.align(Alignment.Bottom)
                            .align(Alignment.Bottom)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = "clear",
                            //tint = colors.tertiary,
                            tint = topicColour,
                            modifier = Modifier
                                .height(vClearButtonSize)
                        )
                    }
                }
            }

        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 3.dp, start = 5.dp, end = 0.dp, bottom = 3.dp)
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
                    .align(Alignment.CenterVertically),
                onFocus = onFocus
            )

            Spacer(modifier = Modifier.width(8.dp))

            CustomButton( // ADD BUTTON
                onClick = {
                    openFileLauncher.launch(arrayOf("*/*"))
                },
                buttonModifier = Modifier
                    .size(vButtonSize)
                    .align(Alignment.Bottom),
                imageVector = Icons.Filled.Add,
                contentDescription = "Attach",
                //tint = colors.tertiary,
                tint = topicColour,
                iconModifier = Modifier
                    .height(vIconSize)
            )

            Spacer(
                modifier = focusModifier
                    .width(5.dp)
                    .focusRequester(focusRequester)
            )
            var tempInputText: String = inputText
            val coroutineScope = rememberCoroutineScope()
            val context = LocalContext.current
            val copiedFilePathList = mutableListOf<String>()
            var tempFilePath = Pair("","")
            CustomButton( // SEND BUTTON
                onLongPress = {
                    inputText=""
                    selectedFileUris.value = emptyList()
                    viewModel.setTempMessageId(-1)
                    viewModel.setEditMode(false)
                    bEditedMode=false
                },
                onClick = {
                    copiedFilePathList.clear()
                    tempFilePath = Pair("","")
                    Log.d("THIS IS BEFORE COPYING: ", "${copiedFilePathList}")
                    tempInputText = inputText
                    inputText = ""
                    viewModel.setToFocusTextbox(false)

                    if (!selectedFileUris.value.isNullOrEmpty() || (tempInputText.length > 0)) {
                        // Edit mode
                        if ((bEditedMode) && (viewModel.tempMessageId.value > -1)){
                            coroutineScope.launch {
                                val messageID: Int = tempMessageID
                                viewModel.editMessage(
                                    messageId = messageID,
                                    topicId = topicId,
                                    content = tempInputText,
                                    priority = messagePriority,
                                    categoryId = 1,
                                    type = 1
                                )

                                val(deletedFiles, addedFiles) = compareFileLists(
                                    selectedFileUrisBeforeEdit.value,
                                    selectedFileUris.value
                                )
                                // Add any additional files to the DB
                                if (!addedFiles.isNullOrEmpty()) {
                                    tempFilePath = Pair("","")
                                    // Copy file, get list of paths as return value
                                    addedFiles.forEach { uri ->
                                        val filetype = determineFileType(context, uri)
                                        tempFilePath = copyFileToUserFolder(
                                            context = context,
                                            currentUri = uri,
                                            directoryName = filetype,
                                            height =  heightSetting,
                                            width = widthSetting,
                                            )
                                        val normalFilePath =tempFilePath.first
                                        val thumbnailFilePath =tempFilePath.second
                                        // add list of paths to DB
                                        viewModel.addFile(
                                            topicId = topicId,
                                            messageId = messageID,
                                            fileType = filetype,
                                            filePath = normalFilePath,
                                            description = "",
                                            categoryId = 1,
                                            iconPath = thumbnailFilePath
                                        )
                                    }
                                }
                                // Remove deleted files from db
                                if (!deletedFiles.isNullOrEmpty()) {
                                    val fileIDsToDelete: List<Int> =
                                        deletedFiles.mapNotNull { uri ->
                                            val filePath = uri.path
                                            filePath?.let { viewModel.getIdForFilePath(it) }
                                        }
                                    if (fileIDsToDelete.isNotEmpty()) {
                                        viewModel.deleteFiles(fileIDsToDelete)
                                    }
                                }
                                selectedFileUris.value = emptyList()
                                viewModel.setEditMode(false)
                                bEditedMode = false
                            }

                        } else {
                            // Write message to db
                            coroutineScope.launch {

                                val messageId = viewModel.addMessage(
                                    topicId = topicId,
                                    content = tempInputText,
                                    priority = messagePriority,
                                    type = 0, //based on if check to see what type - message or image or file etc
                                    categoryID = 1
                                )

                                if (!selectedFileUris.value.isNullOrEmpty()) {
                                    tempFilePath = Pair("","")
                                    // Copy file, get list of paths as return value
                                    selectedFileUris.value?.forEach { uri ->
                                        val filetype = determineFileType(context, uri)
                                        tempFilePath = copyFileToUserFolder(
                                            context = context,
                                            currentUri = uri,
                                            directoryName = filetype,
                                            height = heightSetting,
                                            width = widthSetting,
                                            )

                                        val normalFilePath =tempFilePath.first
                                        val thumbnailFilePath =tempFilePath.second

                                        //copiedFilePathList.add(tempFilePath)
                                        // add list of paths to DB
                                        viewModel.addFile(
                                            topicId = topicId,
                                            messageId = messageId.toInt(),
                                            fileType = determineFileType(context, uri),
                                            filePath = normalFilePath,
                                            iconPath =  thumbnailFilePath,
                                            description = "",
                                            categoryId = 1,
                                        )
                                    }
                                    selectedFileUris.value = emptyList()
                                }
                            }
                        }
                    }
                    viewModel.setTempMessageId(-1)
                },
                buttonModifier = Modifier
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = { offset ->
                                inputText=""
                                selectedFileUris.value = emptyList()
                                viewModel.setTempMessageId(-1)
                                viewModel.setEditMode(false)
                                bEditedMode=false
                            }
                        )
                    }
                    .size(vButtonSize)
                    .fillMaxWidth(1f)
                    //.background(Color.Transparent)
                    .align(Alignment.Bottom),
                imageVector = if (bEditedMode) Icons.Filled.Check else Icons.AutoMirrored.Filled.Send,
                //imageVector = Icons.Filled.Send, // Attach file icon
                contentDescription = "Send",
                //tint = colors.tertiary,
                tint = topicColour,
                iconModifier = Modifier
                    .size(vIconSize)
                //.aspectRatio(2.5f)
            )
            Spacer(modifier = Modifier.width(12.dp))
        }
    }
}