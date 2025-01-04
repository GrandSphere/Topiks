package com.example.topics2.ui.components.global

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomTextBox(
    inputText: String,
    onValueChange: (String) -> Unit,
    vMaxLinesSize: Dp = 100.dp,
    vFontSize: TextUnit = 16.sp,
    sPlaceHolder: String = "Enter message",
    isFocused: Boolean = false,
    focusModifier: Modifier = Modifier,
    boxModifier: Modifier = Modifier // Modifier for the Box
) {
    Box(
        modifier = boxModifier // Apply the modifier passed for the Box
    ) {
        BasicTextField(
            value = inputText,
            onValueChange = onValueChange,
            modifier = focusModifier
                .fillMaxWidth() // Ensure textfield takes full width
                .padding(start = 3.dp, top = 3.dp) // Ensure padding inside text field
                .heightIn(max = vMaxLinesSize),
            textStyle = TextStyle(
                fontSize = vFontSize,
                color = Color.White, // Set font color for the input text
                lineHeight = 20.sp,
            ),
            cursorBrush = if (isFocused) SolidColor(Color.White) else SolidColor(Color.Transparent), // Hide cursor if not focused
            decorationBox = @Composable { innerTextField ->
                if (inputText.isEmpty()) {
                    Text(
                        text = sPlaceHolder,
                        style = TextStyle(
                            color = Color.Gray, // Placeholder color
                            fontSize = vFontSize,
                            lineHeight = 20.sp
                        ),
                        modifier = Modifier.padding(start = 0.dp, top = 0.dp) // Adjust placeholder padding
                    )
                }
                innerTextField() // This is where the input text goes
            }
        )
    }
}
