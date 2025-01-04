package com.example.topics2.ui.components.addTopic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.topics2.ui.viewmodels.topicViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TopicCategory() {
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }

    // Focus change listener to update isFocused state
    val focusModifier = Modifier
        .focusRequester(focusRequester)
        .onFocusChanged { focusState ->
            isFocused = focusState.isFocused
        }

    // Initial text state, set to "Topics"
    var inputText by remember { mutableStateOf("Topics") }

    Row(
        modifier = Modifier
            .padding(top = 3.dp, start = 5.dp, end = 0.dp, bottom = 4.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start, // Align items to the start of the row
        verticalAlignment = Alignment.CenterVertically // Vertically center the text and TextField
    ) {
        // Label "Category"
        Text(
            text = "Category:",
            modifier = Modifier
                .padding(start = 3.dp, top = 3.dp, end = 8.dp),
            style = TextStyle(
                fontSize = 18.sp, // Set the font size
                color = Color.Gray, // Set the text color
            )
        )

        // BasicTextField with the "Topics" initial value
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(0.dp)
                .align(Alignment.CenterVertically) // Align the text field vertically in the center
        ) {
            BasicTextField(
                value = inputText, // Bind value to inputText
                onValueChange = { newText ->
                    inputText = newText // Update inputText when the user types
                },
                modifier = focusModifier
                    .fillMaxWidth() // Ensure the text field takes full width
                    .padding(start = 3.dp, top = 3.dp), // Padding inside text field
                textStyle = TextStyle(
                    fontSize = 18.sp,
                    color = Color.White, // Set font color for the input text
                    lineHeight = 20.sp,
                ),
                cursorBrush = if (isFocused) SolidColor(Color.White) else SolidColor(Color.Transparent), // Show cursor only when focused
                decorationBox = @Composable { innerTextField ->
                    if (inputText.isEmpty()) {
                        // Show "Category..." text only when the input is empty
                        Text(
                            text = "Category...", // Placeholder text
                            style = TextStyle(
                                color = Color.Gray, // Placeholder color
                                fontSize = 18.sp,
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
}
