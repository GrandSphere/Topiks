/*
package com.example.topics2.unused.messageScreen


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.topics.ui.themes.cDateStampFont
import com.example.topics.ui.themes.cMessageFont
import com.example.topics2.ui.components.messageScreen.TestshowAttachments
import com.example.topics2.ui.viewmodels.MessageViewModel
import picturesPreview


@Composable
fun TestMessageBubble( // New Message Bubble
    //topicColor: Color = MaterialTheme.colorScheme.tertiary,
    viewModel: MessageViewModel,
    navController: NavController,
    topicColor: Color = Color.Cyan,
    topicFontColor: Color = Color.Black,
    messageContent: String,
    containsPictures: Boolean= false,
    containsAttachments: Boolean = false,
    containsMessage: Boolean = true,
    listOfPictures: List<String> = emptyList<String>(),
    listOfAttachmentsP: List<String> = emptyList<String>(),
    timestamp: String
) {
    var messagecontent = messageContent
    val imagePaths = listOfPictures

    val listOfAttachments: List<String> =  listOfAttachmentsP
    val iPictureCount: Int = imagePaths.size

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
                        listOfImages = imagePaths,
                        iPictureCount = iPictureCount,
                        topicColor = topicColor,
                        topicFontColor = topicFontColor,
                    )
//                }
            }


            if (containsAttachments) {
                TestshowAttachments(
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
            if (containsMessage) {
                Text( // Show Message Content.
                    text = messagecontent,
                    color = topicFontColor,
                    style = cMessageFont,

                    overflow = TextOverflow.Ellipsis, // Show "..." at the end of the overflowed text
                    maxLines = if(bshowMore) Int.MAX_VALUE else 10,
                    onTextLayout = { textLaoutResult -> isOverFlowing= textLaoutResult.hasVisualOverflow }, // TODO THIS IS NOT EFFECIENT
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                )
            }

           if (isOverFlowing || bshowMore) {
               Text(
                   text = if (!bshowMore) "show more" else "show less",
                   modifier = Modifier.clickable(onClick = { bshowMore = !bshowMore }),
                   textAlign = TextAlign.Center,
                   style = cMessageFont,
               )
           }

            Spacer(modifier = Modifier.height(1.dp)) //space between message and date

            Text(
                text = timestamp,
                color=topicFontColor.copy(alpha = 0.8f),
                style = cDateStampFont,
                // color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
//                 modifier = Modifier.align(Alignment.End)
            )

        }
    }
}*/
