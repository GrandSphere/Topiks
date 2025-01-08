package com.example.topics2.unused
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
//import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun testScreen2( // New Message Bubble
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
    var containsPictures: Boolean = true
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

@Composable
fun picturesPreview( // State 1
    topicColor: Color,
    topicFontColor: Color,
    imagePaths: List<String>,
    iPictureCount: Int = 4,
    onShowMore: () -> Unit, // Pass a lambda to update the state
    modifiera: Modifier = Modifier,
) {

    val opacity2: Float = 0.3f
    var iNumberColumns: Int = 2 // Number of columns (2 per row)
    val opacity: Float = 0.2f
    var iPicturesToShow= 0
    val cPadding: Dp = 6.dp
    val cBorder: Dp = 2.dp
    when (iPictureCount) {
        1 -> { iPicturesToShow = 1
            iNumberColumns=1 }
        2 -> { iPicturesToShow = 2
            iNumberColumns=2 }
        3 -> { iPicturesToShow = 3
            iNumberColumns=2 }
        4 -> { iPicturesToShow = 4
            iNumberColumns=2 }
        else -> { iPicturesToShow = 3
            iNumberColumns=2 } }
    Column(
        modifiera
    ) {
        LazyVerticalGrid(
            horizontalArrangement = Arrangement.spacedBy(3.dp), // Horizontal space between items
            verticalArrangement = Arrangement.spacedBy(3.dp),
            columns = GridCells.Fixed(iNumberColumns), // 2 images per row
            modifier = Modifier
        ) {
            items(imagePaths.take(iPicturesToShow)) { imagePath -> // Display the Images
                Image(
                    modifier = Modifier
                        .clip(RoundedCornerShape(cPadding))
                        .background(topicFontColor.copy(opacity))
                        .border(cBorder, topicFontColor.copy(opacity2), RoundedCornerShape(cPadding))
                        .aspectRatio(1f),
                    painter = rememberAsyncImagePainter(imagePath),
                    contentDescription = "Image",
                    contentScale = ContentScale.Crop, // Crops the image if necessary
                )
            }
            item {
                if (iPictureCount>iPicturesToShow) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(cPadding))
                            .background(topicFontColor.copy(opacity)) // 50% transparent blue
                            .border(cBorder, topicFontColor.copy(opacity2), RoundedCornerShape(cPadding)) // Apply a rounded border
                            .aspectRatio(1f)
                            .clickable(onClick = onShowMore) // Trigger the show more action
                    ) {
                        Text(
                            text = "Show More...",
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(Alignment.Center),
                            color = topicFontColor,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }

//        Spacer(modifier = Modifier.height(4.dp)) //space between message and date
//        Divider(color = topicFontColor.copy(opacity2), thickness = cBorder)
        Spacer(modifier = Modifier.height(5.dp)) //space between message and date

    }
}

@Composable
fun showAttachments(
    topicFontColor: Color,
    topicColor: Color,
    opacity: Float = 0.0f,
    newBubbleWidth: Float = 1f,
    attachments: List<String>,
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(topicFontColor.copy(opacity))
            .border(2.dp, topicFontColor, RoundedCornerShape(8.dp))
            .fillMaxWidth(newBubbleWidth)
            .padding(vertical =5.dp)
            .padding(5.dp)
    ) { //Divider(color = Color.Red, thickness = 2.dp)
        attachments.forEach { attachment ->
            Text( // get text name from path
                text = attachment,
                modifier = Modifier
                    .widthIn(min=200.dp)
                    .padding(start=1.dp, top = 5.dp, bottom = 5.dp , end=10.dp),
                style = TextStyle(
                    fontSize = 16.sp,
                    color = topicFontColor,
                    textDecoration = TextDecoration.Underline
                )
            )
        }
    }
}