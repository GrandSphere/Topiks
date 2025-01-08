package com.example.topics2.unused
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
        modifier = Modifier
            .padding(1.dp),
        tonalElevation = 0.dp, // Remove shadow
        border = null // Remove border
    ) {
        Column( // Message Bubble to allign messageContent, additional Content and timpstamp
            modifier = Modifier
                .padding(6.dp), //space around message
        ) {
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
            if (containsPictures) {
                if (!showMore) {
                    picturesPreview(
                        modifiera = Modifier
                            //.widthIn(min=200.dp)
                            //.width(componentWidth.dp),
                            .fillMaxWidth(0.7f),
                        imagePaths = imagePaths,
                        iPictureCount = iPictureCount,
                        onShowMore = { navController.navigate("navState2") },
                        topicColor = topicColor,
                        topicFontColor = topicFontColor,
                    )
                }
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

    var iNumberColumns: Int = 2 // Number of columns (2 per row)
    val opacity: Float = 0.2f
    var iPicturesToShow= 0
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

        //Spacer(modifier = Modifier.height(8.dp)) //space between message and date
        //Divider(color = topicFontColor, thickness = 3.dp)
        //Spacer(modifier = Modifier.height(5.dp)) //space between message and date

        LazyVerticalGrid(

            horizontalArrangement = Arrangement.spacedBy(1.dp), // Horizontal space between items
            verticalArrangement = Arrangement.spacedBy(1.dp),

            columns = GridCells.Fixed(iNumberColumns), // 2 images per row
            modifier = Modifier
                .padding(1.dp)
            //.width(columnWidth)
            //.background(Color.Red)
        ) {
            // Display the images
            items(imagePaths.take(iPicturesToShow)) { imagePath ->
                Image(
                    //clipu = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
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
                            //.fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(topicFontColor.copy(opacity)) // 50% transparent blue
                            .border(2.dp, topicFontColor, RoundedCornerShape(8.dp)) // Apply a rounded border
                            .aspectRatio(1f)
                            .clickable(onClick = onShowMore) // Trigger the show more action
                            //.align(Alignment.Center) // Center the content inside the Box
                    ) {
                        Text(
                            text = "Show More...",
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(Alignment.Center), // Center the text
                            color = topicFontColor,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
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
            //.border(2.dp, Color.Red)
            .background(topicFontColor.copy(opacity)) // 50% transparent blue
            .border(2.dp, topicFontColor, RoundedCornerShape(8.dp)) // Apply a rounded border
            .fillMaxWidth(newBubbleWidth)
            .padding(vertical =5.dp)
            .padding(5.dp)
        //.padding(20.dp),
    ) { //Divider(color = Color.Red, thickness = 2.dp)
        attachments.forEach { attachment ->
            Text( // get text name from path
                text = attachment,
                modifier = Modifier
                    .widthIn(min=200.dp)
                    .padding(start=1.dp, top = 5.dp, bottom = 5.dp , end=10.dp),
                style = TextStyle(
                    fontSize = 16.sp, // Set font size as needed
                    color = topicFontColor, // Set text color
                    textDecoration = TextDecoration.Underline // Underline text
                )
            )

        }
    }
}