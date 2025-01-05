package com.example.topics2.ui.components.noteDisplay

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.example.topics2.db.enitities.MessageTbl
import com.example.topics2.ui.viewmodels.MessageViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun MessageBubble(
    message: MessageTbl,
    topicColor: Color = MaterialTheme.colorScheme.secondary,  // Default to secondary color from theme
    topicFontColor: Color = MaterialTheme.colorScheme.onSecondary,  // Default to secondary color from theme
    topicId: Int?,
    viewModel: MessageViewModel
) {
    var showMenu by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    // Format timestamp (you can format this as needed)


    //var inputText by remember { mutableStateOf(viewModel.tempMessage.value ) }

    val clipboardManager = LocalClipboardManager.current
    val formattedTimestamp =
        SimpleDateFormat("HH:mm dd/MM/yy", Locale.getDefault()).format(message.messageTimestamp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            //.background(Color.Red)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { },
                    onLongPress = { showMenu = true }
                )
            }
            .padding(3.dp),  // Reduced padding between messages

        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Message bubble with some padding and rounded corners
        Surface(
            shape = RoundedCornerShape(8.dp),
            //color = topicColor,
            color = topicColor,
            modifier = Modifier.padding(1.dp),
            tonalElevation = 0.dp, // Remove shadow
            border = null // Remove border
        ) {
            Column(
                modifier = Modifier
                    //.fillMaxWidth() //messages take up entire width
                    //.background(topic)
                    .padding(6.dp), //space around message
            ) {
                Text(
                    text = message.messageContent,
                    color=topicFontColor,
                    style = MaterialTheme.typography.bodyMedium,
                    //color = MaterialTheme.colorScheme.onPrimary
                )

                Spacer(modifier = Modifier.height(1.dp)) //space between message and date
                Text(
                    text = formattedTimestamp,
                    color=topicFontColor,
                    style = MaterialTheme.typography.bodySmall,
                    // color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
                )
            }
        }
        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false }
        ) {

            DropdownMenuItem(
                text = { Text("Copy") },
                onClick = {

                    clipboardManager.setText(annotatedString = (AnnotatedString(message.messageContent)))
                   showMenu = false
                }
            )

            DropdownMenuItem(
                text = { Text("Edit") },
                onClick = {
                    viewModel.setTempMessage(message.messageContent)
                    viewModel.setShouldUpdate(true)
                    viewModel.setTempMessageId(message.id)
                   // coroutineScope.launch {
                   //     viewModel.editMessage(
                   //         message.id,
                   //         topicId,
                   //         "I was edited",
                   //         message.messagePriority
                   //     )
                   // }
                    showMenu = false
                }
            )

            DropdownMenuItem(
                text = { Text("Delete Message") },
                onClick = {
                    coroutineScope.launch {
                        viewModel.deleteMessage(message.id, topicId,)
                        showMenu = false
                    }
                }
            )
        }
    }

}
