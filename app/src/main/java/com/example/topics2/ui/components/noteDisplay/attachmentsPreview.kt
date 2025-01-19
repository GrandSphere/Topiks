package com.example.topics2.ui.components.noteDisplay

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.example.topics2.unused.getFileNameFromString
import com.example.topics2.utilities.openFile
import java.io.File


@Composable
fun showAttachments(
    topicFontColor: Color,
    topicColor: Color,
    opacity: Float = 0.0f,
    newBubbleWidth: Float = 1f,
    attachments: List<String>,
) {

    val context: Context = LocalContext.current
    var bshowMore: Boolean by remember { mutableStateOf(false) }
    var iNumToTake: Int by remember { mutableStateOf(4) }
//   var bShowMore by remember { mutableStateOf(false)}
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
//            .background(topicFontColor.copy(opacity))
//            .border(2.dp, topicFontColor, RoundedCornerShape(8.dp))
            .fillMaxWidth(newBubbleWidth)
            .padding(vertical =5.dp)
            .padding(5.dp)
    ) { //Divider(color = Color.Red, thickness = 2.dp)

        val iDefaultNumToTake: Int=4
        Text( // get text name from path
            text = "Attachments:",
            modifier = Modifier
                .widthIn(min=200.dp)
                .padding(start=1.dp, top = 5.dp, bottom = 5.dp , end=10.dp),
            style = TextStyle(
                fontSize = 16.sp,
                color = topicFontColor,
//                textDecoration = TextDecoration.Underline
            )
        )

        attachments.take(iNumToTake).forEach { attachment ->
            Text( // get text name from path
                text = "\u2022 " + getFileNameFromString(attachment), // TODO do this somewhere it might be more effecient. Maybe even in database
                modifier = Modifier
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                openFile(context, attachment)
                            },
                            onLongPress = {
                            }
                        )
                    }
                    .widthIn(min=200.dp)
                    .padding(start=1.dp, top = 5.dp, bottom = 5.dp , end=10.dp),
                style = TextStyle(
                    fontSize = 16.sp,
                    color = topicFontColor.copy(alpha = 0.8f),
//                    textDecoration = TextDecoration.Underline
                )
            )
        }

        if (attachments.size> iDefaultNumToTake) {
            if (bshowMore) {
                //iNumToTake = attachments.size
                iNumToTake = attachments.size
            } else {
                iNumToTake = iDefaultNumToTake
            }
            Text(
                text = if (!bshowMore) "show more" else "show less",
                modifier = Modifier.clickable(onClick = { bshowMore = !bshowMore }),
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 16.sp,
                    color = topicFontColor.copy(alpha = 0.5f),
//                    textDecoration = TextDecoration.Underline
                )
            )
        }
    }
}
