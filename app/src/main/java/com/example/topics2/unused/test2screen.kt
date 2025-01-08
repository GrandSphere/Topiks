package com.example.topics2.unused
import android.util.Log
import androidx.compose.animation.expandVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp

@Composable
fun testScreen2(
    //topicColor: Color = MaterialTheme.colorScheme.tertiary,
    topicColor: Color = Color.Cyan ,
    topicFontColor: Color = Color.Black
) { // Main screen
    val iPictureCount: Int =5
        val listOfAttachments = listOf("Attachment 1", "Attachment 2", "Attachment 3") // Example list
    val imagePaths = getTestImagePaths()
    var showMore by remember { mutableStateOf(false) }
    var messagecontent="aaaaaa\naaaaaaaaaaaaaaaaa\naaa\nnnnnnnnnnnnnnna"
    var containsPictures: Boolean = false
    var containsAttachments: Boolean = true
    val withContentWidth: Float = 0.8f
    val opacity: Float = 0.2f
    var componentWidth by remember { mutableStateOf(0) }
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
                //.width(200.dp)
//                    .onSizeChanged { size ->
//                        componentWidth = size.width
//                        Log.d("aabbcc width changed", componentWidth.toString())
//                    }
            )

            if (containsAttachments) {
                Column(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(topicFontColor.copy(opacity)) // 50% transparent blue
                                //.border(2.dp, Color.Red)
                                .border(2.dp, topicFontColor, RoundedCornerShape(8.dp)) // Apply a rounded border
                                .fillMaxWidth(withContentWidth)
                                .padding(vertical =5.dp)
                                .padding(5.dp)
                    //.padding(20.dp),
                ) { //Divider(color = Color.Red, thickness = 2.dp)
                    listOfAttachments.forEach { attachment ->
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


            Spacer(modifier = Modifier.height(1.dp)) //space between message and date



            if (containsPictures) {
                if (!showMore) {
                    DisplayState1(
                        modifiera = Modifier
                            //.widthIn(min=200.dp)
                            //.width(componentWidth.dp),
                            .fillMaxWidth(0.7f),
                        imagePaths = imagePaths,
                        iPictureCount = iPictureCount,
                        onShowMore = { showMore = true },
                        topicColor = topicColor,
                        topicFontColor = topicFontColor,
                    )
                } else {
                    DisplayState2(
                        imagePaths = imagePaths,
                        topicColor = topicColor,
                        topicFontColor = topicFontColor,
                        //imageSize = imageSize,
                        //imageSpacing = imageSpacing,
                        onBack = { showMore = false } // Update state when "Back" is clicked
                    )
                }
            }


            Spacer(modifier = Modifier.height(1.dp)) //space between message and date

            Text(
                text = "my timestamp",
                color=topicFontColor,
                style = MaterialTheme.typography.bodySmall,
                // color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
            )
        }
    }






}


@Composable
fun DisplayState2( // State 2
    topicColor: Color,
    topicFontColor: Color,
    imagePaths: List<String>,
    onBack: () -> Unit,
    //modifier: Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()  // Make it take the full width of the parent
        //.widthIn(min = 300.dp)    //.fillMaxWidth()
        //.fillMaxHeight()
//            .background(Color.Yellow)
    ) {
        LazyColumn(
            modifier = Modifier

            //.fillMaxWidth()
            //.background(Color.Red)
        ) {
            items(imagePaths) { imagePath ->
                SubcomposeAsyncImage(
                    model = imagePath,
                    contentDescription = getFileNameFromString(imagePath),
                    contentScale = ContentScale.Fit, // Maintains aspect ratio
                    loading = { // Show a placeholder while the image is loading
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth() // Take full width
                        .padding(bottom = 2.dp)
                    //.border(2.dp, Color.Black) // Add border around the image
                )
            }
        }

        FloatingActionButton(
            onClick = onBack,
            modifier = Modifier
                //.align(Alignment.BottomEnd) // Align to the bottom end
                .padding(16.dp), // Add padding to the edge
            //containerColor = topicColor,
            //shape = RoundedCornerShape(16.dp), // Change the shape to rounded corners
            shape = CircleShape, // Change the shape to rounded corners

        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack, // Example icon
                contentDescription = "Add",
                tint = topicFontColor
            )
        }
    }
}


@Composable
fun DisplayState1( // State 1
    topicColor: Color,
    topicFontColor: Color,
    imagePaths: List<String>,
    iPictureCount: Int = 4,
    onShowMore: () -> Unit, // Pass a lambda to update the state
    modifiera: Modifier = Modifier,
) {

    var iNumberColumns: Int = 2 // Number of columns (2 per row)
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
    Box(
        modifiera
    ) {
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
                            .aspectRatio(1f)
                            .clickable(onClick = onShowMore) // Trigger the show more action
                            .align(Alignment.Center) // Center the content inside the Box
                    ) {
                        Text(
                            text = "Show More...",
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(Alignment.Center), // Center the text
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }}
//}}