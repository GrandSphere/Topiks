package com.example.topics2.ui.components.noteDisplay

import android.util.Log
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
    topicFontColor: Color = Color.Black,
    messageContent: String,
    containPictures: Boolean,
    containAttachments: Boolean,
    listOfPictures: List<String>,
    listOfAttachments: List<String>
) { // Main screen


    var messagecontent = messageContent
    val imagePaths: List<String> = listOfPictures
    val listOfAttachments: List<String> =  listOfAttachments
    val iPictureCount: Int = imagePaths.size
    var containsPictures: Boolean = containPictures
    var containsAttachments: Boolean = containAttachments

    Log.d("DEBUG_LOG", "Message Content: $messagecontent")
    Log.d("DEBUG_LOG", "Image Paths: $imagePaths")
    Log.d("DEBUG_LOG", "List of Attachments: $listOfAttachments")
    Log.d("DEBUG_LOG", "Picture Count: $iPictureCount")
    Log.d("DEBUG_LOG", "Contains Pictures: $containsPictures")
    Log.d("DEBUG_LOG", "Contains Attachments: $containsAttachments")



    var showMore by remember { mutableStateOf(false) }
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