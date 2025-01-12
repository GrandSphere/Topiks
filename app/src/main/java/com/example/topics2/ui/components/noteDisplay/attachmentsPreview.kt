package com.example.topics2.ui.components.noteDisplay

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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