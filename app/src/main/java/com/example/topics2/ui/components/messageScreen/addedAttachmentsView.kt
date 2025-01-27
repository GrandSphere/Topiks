package com.example.topics2.ui.components.messageScreen

import android.net.Uri
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.example.topics2.unused.old.getFileNameFromString
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun addedAttachmentsView(
    attachments: List<Uri>?,
    iNumToTake: Int = 10,
)
{
    Column(){
        attachments?.take(iNumToTake)?.forEach { attachment ->
            Text( // get text name from path
                text = "\u2022 " + getFileNameFromString(attachment.toString()), // TODO do this somewhere it might be more effecient. Maybe even in database
                modifier = Modifier
                    .widthIn(min=200.dp)
                    .padding(start=1.dp, top = 5.dp, bottom = 5.dp , end=10.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                            },
                            onLongPress = {
                            }
                        )
                    } ,
                style = TextStyle(
                    fontSize = 16.sp,
                    color = Color.Red,
//                    textDecoration = TextDecoration.Underline
                )
            )
        }

    }

}