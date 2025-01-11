package com.example.topics2.ui.components.noteDisplay


import android.R.attr.text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.style.TextAlign
import com.example.topics2.unused.getFileNameFromString

@Composable
fun TestshowAttachments(
    topicFontColor: Color,
    topicColor: Color,
    opacity: Float = 0.0f,
    newBubbleWidth: Float = 1f,
    attachments: List<String>,
) {

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
            )
        }
    }
}