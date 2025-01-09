package com.example.topics2.ui.components.noteDisplay

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.topics2.unused.getTestImagePaths


@Composable
fun MessageBubble( // New Message Bubble
    //topicColor: Color = MaterialTheme.colorScheme.tertiary,
    navController: NavController,
    topicColor: Color = Color.Cyan,
    topicFontColor: Color = Color.Black
) { // Main screen
    val iPictureCount: Int =5
    val listOfAttachments = listOf("Attachment 1", "Attachment 2", "Attachment 3") // Example list
    val imagePaths = getTestImagePaths()
    var showMore by remember { mutableStateOf(false) }
    var messagecontent="This is my first sentence\nThis is my second sentence\n\nthis nothing"
    var containsPictures: Boolean = false
    var containsAttachments: Boolean = false
    val withContentWidth: Float = 0.8f
    val opacity: Float = 0.2f
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = topicColor,
        tonalElevation = 0.dp, // Remove shadow
        border = null
    ) {
        Column( // Message Bubble to allign messageContent, additional Content and timpstamp
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .padding(vertical = 3.dp), // Padding around everything
        ) {

            Spacer(modifier = Modifier.height(1.dp)) //space between message and date
            if (containsPictures) {
                if (!showMore) {
                    picturesPreview(
                        modifiera = Modifier
                            .fillMaxWidth(0.7f),
                        imagePaths = imagePaths,
                        iPictureCount = iPictureCount,
                        onShowMore = { navController.navigate("navState2") },
                        topicColor = topicColor,
                        topicFontColor = topicFontColor,
                    )
                }
            }

            Text( // Show Message Content.
                text = messagecontent,
                color = topicFontColor,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(0.dp)
            )

            if (containsAttachments) {
                showAttachments(
                    topicFontColor = topicFontColor,
                    topicColor = topicColor,
                    opacity= opacity,
                    newBubbleWidth = withContentWidth,
                    attachments = listOfAttachments,
                )

            }
            Spacer(modifier = Modifier.height(1.dp)) //space between message and date
            Text(
                text = "02:07 08/01/25",
                color=topicFontColor,
                style = MaterialTheme.typography.bodySmall,
                // color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
            )
        }
    }
}