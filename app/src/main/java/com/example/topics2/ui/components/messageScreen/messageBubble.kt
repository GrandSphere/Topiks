package com.example.topics2.ui.components.messageScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.topics.ui.themes.cDateStampFont
import com.example.topics.ui.themes.cMessageFont
import com.example.topics.ui.themes.cShowMoreFont
import com.example.topics2.db.entities.FileInfoWithIcon
import picturesPreview


@Composable
fun MessageBubble( // New Message Bubble
    //topicColor: Color = MaterialTheme.colorScheme.tertiary,
    navController: NavController,
    topicColor: Color = Color.Cyan,
    topicFontColor: Color = Color.Black,
//    messageContent: String,
    bSearch: Boolean = false,
    annotatedMessageContent: AnnotatedString,
    containsPictures: Boolean= false,
    containsAttachments: Boolean = false,
    containsMessage: Boolean = true,
    listOfPictures: List<FileInfoWithIcon> = emptyList<FileInfoWithIcon>(),
    listOfAttachmentsP: List<String> = emptyList<String>(),
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onViewMessage: () -> Unit = {},
    bDeleteEnabled: Boolean = false,
    timestamp: String
) {
//    var messagecontent = messageContent

    val colours = MaterialTheme.colorScheme
    var showMenu by remember { mutableStateOf(false) }
    val clipboardManager = LocalClipboardManager.current
    val listOfAttachments: List<String> =  listOfAttachmentsP
    val iPictureCount: Int = listOfPictures.size

    var showMore by remember { mutableStateOf(false) }
    val withContentWidth: Float = 0.8f
    val opacity: Float = 0.2f
    var focusing = true
    Surface( // Message bubble
        shape = RoundedCornerShape(9.dp),
        color = topicColor,
        tonalElevation = 0.dp, // Remove shadow
        border = null,
        modifier = Modifier.padding(horizontal = 12.dp , vertical = 4.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        showMenu=true
                    }
                )
            }

    ) { // Allignment of message bubble
        Column( // Message Bubble to align messageContent, additional Content and timestamp
            modifier = Modifier
                .padding(horizontal = 6.dp)
                .padding(vertical = 3.dp), // Padding around everything
        ) {

            if (containsPictures) {
//                if (!showMore) {
                picturesPreview(
                    navController = navController,
                    modifiera = Modifier
                        .padding(vertical = 4.dp, horizontal = 1.dp)
                        .fillMaxWidth(),
                    listOfImages = listOfPictures,
                    iPictureCount = iPictureCount,
                    topicColor = topicColor,
                    topicFontColor = topicFontColor,
                )
//                }
            }

            if (containsAttachments) {
                showAttachments(
                    topicFontColor = topicFontColor,
                    topicColor = topicColor,
                    opacity= opacity,
                    newBubbleWidth = withContentWidth,
                    attachments = listOfAttachments,
                )

            }

//            var iMaxLines: Int by remember {mutableStateOf(10)}
            var isOverFlowing: Boolean by remember {mutableStateOf(false)}
            var bshowMore: Boolean by remember {mutableStateOf(false)}
//var numberOfLines by remember { mutableStateOf(0) }
            var textLayoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }
//            val contentToDisplay = annotatedMessageContent
//            val contentToDisplay = annotatedMessageContent ?: AnnotatedString("")
//            val contentToDisplay = annotatedMessageContent ?: AnnotatedString(messageContent)
            if (containsMessage) {
                Text( // Show Message Content.
                    text = annotatedMessageContent,
//                    text = messagecontent,
                    color = topicFontColor,
                    style = cMessageFont,

                    overflow = TextOverflow.Ellipsis, // Show "..." at the end of the overflowed text
                    maxLines = if(bshowMore) Int.MAX_VALUE else 10,
                    onTextLayout = { textLaoutResult -> isOverFlowing= textLaoutResult.hasVisualOverflow }, // TODO THIS IS NOT EFFECIENT
                    modifier = Modifier
                        .pointerInput(Unit) {
                            detectTapGestures(
                            onLongPress = {

//                                viewModel.setToUnFocusTextbox(true)
//                                //viewModel.setTempMessage(message.content)
//                                //viewModel.setAmEditing(true)
//                                viewModel.setTempMessageId(message.id)
//                                showMenu = false
//                                viewModel.setToFocusTextbox(true)

//                                Log.d("arst","in messagebuble")
                                showMenu = true
//                                onEditClick()
                            }

//                                onLongPress = { Log.d("arst","arst")}
                            )
                        }
                        .padding(vertical = 4.dp)
                )
            }

            if (isOverFlowing || bshowMore) {
                Text(
                    text = if (!bshowMore) "show more" else "show less",
                    modifier = Modifier.clickable(onClick = { bshowMore = !bshowMore }),
                    textAlign = TextAlign.Center,
                    style = cShowMoreFont,
                    color= topicFontColor.copy(alpha = 0.6f),
                )
            }

            Spacer(modifier = Modifier.height(1.dp)) //space between message and date

            Text(
                text = timestamp,
                color= topicFontColor.copy(alpha = 0.8f),
                style = cDateStampFont,
                // color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
//                 modifier = Modifier.align(Alignment.End)
            )

        }
    }

    DropdownMenu(
        expanded = showMenu,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.4f)
            .background(colours.background)
        ,

        onDismissRequest = {
            //viewModel.setToFocusTextbox(false)
            showMenu = false },
    ) {

        DropdownMenuItem(
            text = { Text("Copy", color = colours.onBackground) },
//colors = androidx.compose.material3.DropdownMenuItemDefaults.colors( contentColor = Color.Blue),
            onClick = {
                showMenu = false
                clipboardManager.setText(annotatedString = annotatedMessageContent)
                // ERROR NOTE TODO WHATEVER BELOW LINE IS NEEDED IF WE USE MESSAGE CONTENT AGAIN
                // COPY MIGHT NOT WORK RIGHT ANYMORE
//                clipboardManager.setText(annotatedString = (AnnotatedString(messageContent)))
            }
        )


            DropdownMenuItem(

                text = { Text("Edit", color = colours.onBackground) },
                onClick = {
                    showMenu = false
                    onEditClick()
                }
            )
        DropdownMenuItem(
            text = { Text("View", color = colours.onBackground) },
            onClick = {
                showMenu = false
                onViewMessage()
            }
        )
        if (bDeleteEnabled) {
            DropdownMenuItem(
                text = { Text("Delete Message", color = colours.onBackground) },
                onClick = {
                    showMenu = false
                    onDeleteClick()
                }
            )
        }
    }
}
