package com.GrandSphere.Topiks.ui.components.messageScreen
// Moved to viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.GrandSphere.Topiks.db.entities.FileInfoWithIcon
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.GrandSphere.Topiks.ui.themes.cDateStampFont
import com.GrandSphere.Topiks.ui.themes.cMessageFont
import com.GrandSphere.Topiks.ui.themes.cShowMoreFont
import picturesPreview

@Composable
fun MessageBubble(
    onFocusClear: () -> Unit = {},
    navController: NavController,
    topicColor: Color = Color.Cyan,
    topicFontColor: Color = Color.Black,
    annotatedMessageContent: AnnotatedString,
    containsPictures: Boolean = false,
    containsAttachments: Boolean = false,
    listOfPictures: List<FileInfoWithIcon> = emptyList(),
    listOfAttachmentsP: List<String> = emptyList(),
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onViewMessage: () -> Unit = {},
    onSelectedClick: () -> Unit = {},
    onExportClick: () -> Unit = {},
    bDeleteEnabled: Boolean = false,
    messageSelected: Boolean = false,
    timestamp: String
) {
    val colours = MaterialTheme.colorScheme
    var showMenu by remember { mutableStateOf(false) }
    val clipboardManager = LocalClipboardManager.current
    val listOfAttachments: List<String> = listOfAttachmentsP
    val iPictureCount: Int = listOfPictures.size
    val withContentWidth: Float = 0.8f

    Surface(
        shape = RoundedCornerShape(9.dp),
        color = topicColor,
        tonalElevation = 0.dp,
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { showMenu = true },
                    onPress = { onFocusClear() }
                )
            }
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 6.dp)
                .padding(vertical = 3.dp)
        ) {
            if (containsPictures) {
                picturesPreview(
                    navController = navController,
                    modifiera = Modifier
                        .padding(vertical = 4.dp, horizontal = 1.dp)
                        .fillMaxWidth(),
                    listOfImages = listOfPictures,
                    iPictureCount = iPictureCount,
                    topicColor = topicColor,
                    topicFontColor = topicFontColor
                )
            }

            if (containsAttachments) {
                showAttachments(
                    topicFontColor = topicFontColor,
                    newBubbleWidth = withContentWidth,
                    attachments = listOfAttachments
                )
            }

            var isOverFlowing by remember { mutableStateOf(false) }
            var bShowMore by remember { mutableStateOf(false) }

            if (annotatedMessageContent.isNotEmpty()) {
                Text(
                    text = annotatedMessageContent,
                    color = topicFontColor,
                    style = cMessageFont,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = if (bShowMore) Int.MAX_VALUE else 10,
                    onTextLayout = { textLayoutResult -> isOverFlowing = textLayoutResult.hasVisualOverflow },
                    modifier = Modifier
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onLongPress = { showMenu = true },
                                onPress = { onFocusClear() }
                            )
                        }
                        .padding(vertical = 4.dp)
                )
            }

            if (isOverFlowing || bShowMore) {
                Text(
                    text = if (!bShowMore) "show more" else "show less",
                    modifier = Modifier.clickable(onClick = { bShowMore = !bShowMore }),
                    textAlign = TextAlign.Center,
                    style = cShowMoreFont,
                    color = topicFontColor.copy(alpha = 0.6f)
                )
            }

            Spacer(modifier = Modifier.height(1.dp))

            Text(
                text = timestamp,
                color = topicFontColor.copy(alpha = 0.8f),
                style = cDateStampFont
            )
        }
    }

    DropdownMenu(
        expanded = showMenu,
        modifier = Modifier.background(colours.surface),
        onDismissRequest = { showMenu = false }
    ) {
        val textColour: Color = colours.onSurface
        DropdownMenuItem(
            text = { Text("Copy", color = textColour) },
            onClick = {
                showMenu = false
                clipboardManager.setText(annotatedString = annotatedMessageContent)
            }
        )
        DropdownMenuItem(
            text = { Text("Edit", color = textColour) },
            onClick = {
                showMenu = false
                onEditClick()
            }
        )
        DropdownMenuItem(
            text = { Text("View", color = textColour) },
            onClick = {
                showMenu = false
                onViewMessage()
            }
        )
        DropdownMenuItem(
            text = { Text(if (messageSelected) "Deselect" else "Select", color = textColour) },
            onClick = {
                Log.d("qqwwee", "$messageSelected")
                showMenu = false
                onSelectedClick()
            }
        )
        DropdownMenuItem(
            text = { Text("Export Message", color = textColour) },
            onClick = {
                showMenu = false
                onExportClick()
            }
        )
        if (bDeleteEnabled) {
            DropdownMenuItem(
                text = { Text("Delete Message", color = textColour) },
                onClick = {
                    showMenu = false
                    onDeleteClick()
                }
            )
        }
    }
}