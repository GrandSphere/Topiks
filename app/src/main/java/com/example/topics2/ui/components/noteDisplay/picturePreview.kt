import android.Manifest
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.asImageBitmap
import java.io.InputStream

import android.content.pm.PackageManager
import android.util.Log

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap

import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import java.io.File






fun calculateHeight(iPicturesToShow: Int, iNumberColumns: Int, cPadding: Dp, cBorder: Dp): Dp {
    val rows = (iPicturesToShow + if (iPicturesToShow < 4) 0 else 1).toDouble() / iNumberColumns
    val itemHeight = 100.dp // Adjust this based on your image size
    val spacing = 3.dp
    val padding = cPadding * 2
    val border = cBorder * 2
    return (itemHeight * rows.toInt() + spacing * (rows.toInt() - 1) + padding + border)
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
            modifier = Modifier.height(calculateHeight(iPicturesToShow, iNumberColumns, cPadding, cBorder))
        ) {

            items(imagePaths.take(iPicturesToShow)) { imagePath ->
                Image(
                    modifier = Modifier
                        .clip(RoundedCornerShape(cPadding))
                        .background(topicFontColor.copy(opacity))
                        .border(cBorder, topicFontColor.copy(opacity2), RoundedCornerShape(cPadding))
                        .aspectRatio(1f),
                    painter = rememberAsyncImagePainter(imagePath),
                    contentDescription = "Image",
                    contentScale = ContentScale.Crop,
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
        Spacer(modifier = Modifier.height(5.dp)) //space between message and date

    }
}