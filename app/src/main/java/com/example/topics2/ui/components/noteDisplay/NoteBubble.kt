package com.example.topics2.ui.components.noteDisplay

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.topics2.model.Message

@Composable
fun ChatBubble(message: Message) {
val colors = MaterialTheme.colorScheme
    Row(
        modifier = Modifier
            //.background(Color.Red)
            .fillMaxWidth()
            .padding(4.dp),
    ) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            //color = if (message.isUser) Color(0xFFDCF8C6) else Color(0xFFEDEDED)
            //color = Color.Blue
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(8.dp),
                fontSize = 16.sp,
                color = colors.onTertiary
            )
        }
    }
}
