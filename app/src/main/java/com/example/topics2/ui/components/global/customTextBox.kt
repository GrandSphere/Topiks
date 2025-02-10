package com.example.topics2.ui.components.global

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
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
    onFocus: () -> Unit = {},
    boxModifier: Modifier = Modifier // Modifier for the Box
) {
    val colors = MaterialTheme.colorScheme
    Box(
        modifier = boxModifier // Apply the modifier passed for the Box
    ) {
        BasicTextField(
            value = inputText,
            onValueChange = onValueChange,
            modifier = focusModifier
                .fillMaxWidth() // Ensure textfield takes full width
                .padding(start = 3.dp, top = 3.dp) // Ensure padding inside text field
//                .heightIn(max = vMaxLinesSize),
            ,
            textStyle = TextStyle(
                fontSize = vFontSize,
                color = colors.onBackground, // Set font color for the input text
                lineHeight = 20.sp,

//        softWrap = true // Allow the text to wrap within the available space

            ),
//                            maxLines = 6,

            cursorBrush = if (isFocused) SolidColor(colors.onPrimary) else SolidColor(Color.Transparent), // Hide cursor if not focused
            decorationBox = @Composable { innerTextField ->
                if (inputText.isEmpty()) {
                    Text(
                        text = sPlaceHolder,
                        style = TextStyle(
                            color = colors.secondary, // Placeholder color
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

fun chooseColorBasedOnLuminance(inputColor: Color): Color {
    // Extract normalized RGB values
    val red = inputColor.red
    val green = inputColor.green
    val blue = inputColor.blue
    // Compute luminance using the standard formula
    var luminance = 0.2126 * red + 0.7152 * green + 0.0722 * blue
    return when { // Determine the output color based on luminance
//        luminance < 0.5 -> Color.White
        luminance < 0.5 -> Color(0xf0f0f0FF)
//        luminance < 0.5 -> Color(0xFFFFF0FF)
        //luminance < 0.25 -> Color.White
        //luminance < 0.5 -> Color.Gray
        //luminance < 0.75 -> Color.DarkGray
        else -> Color.Black
    }
}

//fun hsvToHex(hsv: FloatArray): String {
//    // Convert HSV to RGB
//    val rgb = android.graphics.Color.HSVToColor(hsv)
//
//    // Format RGB as a hex string
//    return String.format("#%06X", (0xFFFFFF and rgb))
//}