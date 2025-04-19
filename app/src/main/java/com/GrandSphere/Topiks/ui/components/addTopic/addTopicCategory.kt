package com.GrandSphere.Topiks.ui.components.addTopic

// Moved to viewmodel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
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
import com.GrandSphere.Topiks.ui.viewmodels.TopicViewModel

@Composable
fun TopicCategory(viewModel: TopicViewModel) {
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }
    val colours = MaterialTheme.colorScheme
    // Focus change listener to update isFocused state
    val focusModifier = Modifier
        .focusRequester(focusRequester)
        .onFocusChanged { focusState ->
            isFocused = focusState.isFocused
        }

    // Initial text state, set to "Topics"
    var inputText by remember { mutableStateOf(viewModel.tempcategory.value) }
    viewModel.setTempCategory(inputText)
    Row(
        modifier = Modifier
            .padding(top = 3.dp, start = 5.dp, end = 0.dp, bottom = 4.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) { // Label "Category"
        Text(
            text = "Category:",
            modifier = Modifier
                .padding(start = 3.dp, top = 3.dp, end = 8.dp),
            style = TextStyle(
                fontSize = 18.sp,
                color = colours.onSecondary,
            )
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .padding(0.dp)
                .align(Alignment.CenterVertically)
        ) {
            BasicTextField(
                value = inputText,
                onValueChange = { newText ->
                    inputText = newText
                },
                modifier = focusModifier
                    .fillMaxWidth()
                    .padding(start = 3.dp, top = 3.dp),
                textStyle = TextStyle(
                    fontSize = 18.sp,
                    color = colours.onBackground,
                    lineHeight = 20.sp,
                ),
                cursorBrush = if (isFocused) SolidColor(colours.tertiary) else SolidColor(Color.Transparent), // Show cursor only when focused
                decorationBox = @Composable { innerTextField ->
                    if (inputText.isEmpty()) {
                        Text(
                            text = "Category...",
                            style = TextStyle(
                                color = colours.secondary,
                                fontSize = 18.sp,
                                lineHeight = 20.sp
                            ),
                            modifier = Modifier.padding(start = 0.dp, top = 0.dp)
                        )
                    }
                    innerTextField()
                }
            )
        }
    }
}