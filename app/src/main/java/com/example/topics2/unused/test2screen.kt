package com.example.topics2.unused
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
@Composable
fun testScreen2(
    topicColor: Color = MaterialTheme.colorScheme.tertiary,
    topicFontColor: Color = MaterialTheme.colorScheme.onTertiary,
) { // Main screen
    val colors = MaterialTheme.colorScheme

    val iPictureCount: Int =5
    // Get image paths by calling the separate function
    val imagePaths = getTestImagePaths()

    // State to handle the visibility of additional images
    var showMore by remember { mutableStateOf(false) }
    // Column dimensions to be used
    //val columnWidth = 300.dp
    //val columnHeight = 300.dp

    // Image size for preview
    //val imageSize = 30.dp
    val imageSpacing = 2.dp // Spacing between images

    // Calculate the maximum number of images to show
    val maxImagesVisible = 4 // Initially show only 4 images

var messagecontent="aaaaaaaaaaaaaaaaaaaaaaaa"

    var containsPictures: Boolean = true
    Surface(
        shape = RoundedCornerShape(8.dp),
        //color = topicColor,
        color = topicColor,
        modifier = Modifier.padding(1.dp)
        ,

        tonalElevation = 0.dp, // Remove shadow
        border = null // Remove border
    ) {
        Column(
            modifier = Modifier
                //.fillMaxWidth() //messages take up entire width
                //.wrapContentWidth()
                //.background(topic)
                .padding(6.dp), //space around message
        ) {
            Text(
                text = messagecontent,
                color=topicFontColor,
                style = MaterialTheme.typography.bodyMedium,
                //color = MaterialTheme.colorScheme.onPrimary
            )

            Spacer(modifier = Modifier.height(1.dp)) //space between message and date

if (containsPictures) {
    if (!showMore) {
        DisplayState1(
            modifiera = Modifier
                //.wrapContentWidth()
                //.weight(1f)
                .widthIn(max=cons)
            ,
            imagePaths = imagePaths,
            maxImagesVisible = maxImagesVisible,
            //imageSize = imageSize,
            iPictureCount = iPictureCount,
            imageSpacing = imageSpacing,
            //columnWidth = columnWidth,
            //columnHeight = columnHeight,
            onShowMore = { showMore = true }, // Update state when "Show More" is clicked
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





































    // Main container for switching between display states
//    Column(
//        modifier = Modifier
//            .padding(8.dp)
//            //.background(Color.DarkGray)
//            .fillMaxSize() // Takes up all available space
//    ) {
//        if (!showMore) {
//            DisplayState1(
//                imagePaths = imagePaths,
//                maxImagesVisible = maxImagesVisible,
//                //imageSize = imageSize,
//                imageSpacing = imageSpacing,
//                columnWidth = columnWidth,
//                columnHeight = columnHeight,
//                onShowMore = { showMore = true }, // Update state when "Show More" is clicked
//                topicColor=topicColor,
//                topicFontColor=topicFontColor,
//            )
//        } else {
//            DisplayState2(
//                imagePaths = imagePaths,
//                topicColor=topicColor,
//                topicFontColor=topicFontColor,
//                //imageSize = imageSize,
//                //imageSpacing = imageSpacing,
//                onBack = { showMore = false } // Update state when "Back" is clicked
//            )
//        }
//    }
}


@Composable
fun DisplayState2( // State 2
    topicColor: Color,
    topicFontColor: Color,
    imagePaths: List<String>,
    onBack: () -> Unit,
    //modifier: Modifier
) {
    Box(
        modifier = Modifier
          .fillMaxWidth()  // Make it take the full width of the parent
            //.widthIn(min = 300.dp)    //.fillMaxWidth()
            //.fillMaxHeight()
//            .background(Color.Yellow)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
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
                .align(Alignment.BottomEnd) // Align to the bottom end
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
    maxImagesVisible: Int,
    //imageSize: Dp,
    imageSpacing: Dp,
//    columnWidth: Dp,
//    columnHeight: Dp,
    iPictureCount: Int = 4,
    onShowMore: () -> Unit, // Pass a lambda to update the state
    modifiera: Modifier = Modifier,

) {

    //val iPictureCount: Int = 4 // Number of images to show initially
    var iNumberColumns: Int = 2 // Number of columns (2 per row)
    var iPicturesToShow= 0
//    var iNumberColumns = when (iNumber) { // iNumber -> iNumberColumns
//    1 -> 1
//    2 -> 2
//    //3 -> 2
//    else -> 2 // Default value if none of the conditions are met
//}

    when (iPictureCount) {
        1 -> {
            iPicturesToShow = 1
            iNumberColumns=1
            // Add more actions for x = 1
        }
        2 -> {
            iPicturesToShow = 2
            iNumberColumns=2
        }
        3 -> {
            iPicturesToShow = 3
            iNumberColumns=2
        }
        4 -> {
            iPicturesToShow = 4
            iNumberColumns=2
        }
        else -> {
            iPicturesToShow = 3
            iNumberColumns=2
        }
    }

    Box(
        //modifier = Modifier
        modifiera
            //.wrapContentWidth()
            //.widthIn(max=200.dp)

        //.widthIn(min = 300.dp)    //.fillMaxWidth()
        ///.fillMaxWidth().widthIn(min=200.dp)
            //.wrapContentWidth()
            //.widthIn(max=200.dp)
        //.widthIn(min=200.dp)
            //.width(300.dp)
            //.width(columnWidth)
            //.fillMaxWidth()
            //.clip(RoundedCornerShape(8.dp))
            //.background(Color.Red)

    ) {

//        Surface(
//            shape = RoundedCornerShape(8.dp),
//            //color = topicColor,
//            color = topicColor,
//            modifier = Modifier.padding(1.dp),
//            tonalElevation = 0.dp, // Remove shadow
//            border = null // Remove border
//        ) {
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

                        //.padding(1.dp)
                        .aspectRatio(1f),
                    //.width(imageSize)
                    //.padding(end = imageSpacing, bottom = imageSpacing),
                    painter = rememberAsyncImagePainter(imagePath),
                    contentDescription = "Image",
                    contentScale = ContentScale.Crop, // Crops the image if necessary
                )
            }

            // Show More button in the 4th slot
            item {
                if (iPictureCount>iPicturesToShow) {
                    Box(
                        modifier = Modifier

                            .fillMaxWidth()
                            .aspectRatio(1f)
                            //.height(100.dp)
                            //.padding(1.dp)
                            //.padding(end = imageSpacing, bottom = imageSpacing)
                            //.background(Color.Gray) // Background color for the "Show More" area
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