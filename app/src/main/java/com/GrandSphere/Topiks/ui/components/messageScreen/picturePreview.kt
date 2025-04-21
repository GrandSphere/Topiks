/*******************************************************************************
 * Copyright (C) <2025> GrandSphere <GrandSphereStudios@protonmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.GrandSphere.Topiks.db.entities.FileInfoWithIcon
import com.GrandSphere.Topiks.utilities.helper.TemporaryDataHolder
import com.GrandSphere.Topiks.utilities.openFile

@Composable
fun picturesPreview( // State 1
    modifiera: Modifier = Modifier,
    navController: NavController,
    topicFontColor: Color,
    listOfImages: List<FileInfoWithIcon>,
    iPictureCount: Int = 4,
) {
    val imagePaths: List<String> = listOfImages.map { it.filePath }
    TemporaryDataHolder.setImagePaths(imagePaths)

    var iNumberColumns: Int = 2 // Number of columns (2 per row)
    val opacity: Float = 0.2f
    var iPicturesToShow= 0
    val cPadding: Dp = 8.dp
    val cBorder: Dp = 2.dp
    val cCrop: Dp = cPadding-cBorder
    val context: Context = LocalContext.current
    when (iPictureCount) {
        1 -> { iPicturesToShow = 1
            iNumberColumns=1 }
        2 -> { iPicturesToShow = 2
            iNumberColumns=2 }
        3 -> { iPicturesToShow = 1
            iNumberColumns=2 }
        4 -> { iPicturesToShow = 4
            iNumberColumns=2 }
        else -> { iPicturesToShow = 3
            iNumberColumns=2 } }
    Column(
        modifiera
    ) {
        LazyVerticalGrid(
            horizontalArrangement = Arrangement.spacedBy(4.dp), // Horizontal space between items
            verticalArrangement = Arrangement.spacedBy(4.dp),
            columns = GridCells.Fixed(iNumberColumns), // 2 images per row
         //   modifier = Modifier.height(calculateHeight(iPicturesToShow, iNumberColumns, cPadding, cBorder))
            modifier = Modifier.heightIn(max=800.dp) // TODO this is a temp fix
        ) {

            items(listOfImages.take(iPicturesToShow)) { imagePath ->
                Image(
                    modifier = Modifier
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap= {
                                    openFile(context,imagePath.filePath)
                                },
                                onLongPress = {
                                }
                            )
                        }

                        .border(cBorder, topicFontColor.copy(1f), RoundedCornerShape(cPadding))
//                        .border(cBorder, topicFontColor.copy(opacity2), RoundedCornerShape(cPadding))
                        .padding(cBorder)
                        .clip(RoundedCornerShape(cCrop))
                        .background(topicFontColor.copy(opacity))
                        .aspectRatio(1f),
                    painter = rememberAsyncImagePainter(imagePath.iconPath),
                    contentDescription = "Image",
                    contentScale = ContentScale.Crop,
                )
            }
            item {
                if (iPictureCount>iPicturesToShow) {
                    Box(
                        modifier = Modifier

                            .border(cBorder, topicFontColor.copy(1f), RoundedCornerShape(cPadding))
//                            .border(cBorder, topicFontColor.copy(opacity2), RoundedCornerShape(cPadding))
                            .padding(cBorder)
                            .clip(RoundedCornerShape(cCrop))

//                            .clip(RoundedCornerShape(cPadding))
                            .background(topicFontColor.copy(opacity)) // 50% transparent blue
//                            .border(cBorder, topicFontColor.copy(opacity2), RoundedCornerShape(cPadding)) // Apply a rounded border
                            .aspectRatio(1f)
                            .clickable(onClick = {navController.navigate("navShowMorePictures")}) // Trigger the show more action
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