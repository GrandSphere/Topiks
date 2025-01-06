package com.example.topics2.ui.components.addTopic

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.topics.utilities.copyIconToAppFolder
import com.example.topics2.ui.components.global.CustomTextBox
import com.example.topics2.ui.viewmodels.TopicViewModel

@Composable
fun TopicName(navController: NavController, viewModel: TopicViewModel) {
    //val category: String by viewModel.category.collectAsState()
    val sPlaceHolder = "Topic Name..."
//val iMaxLines = 5
    val vFontSize: TextUnit = 18.sp // You can change this value as needed
    val vButtonSize: Dp = 30.dp // You can change this value as needed
    val vIconSize: Dp = 30.dp // You can change this value as needed
    val vMaxLinesSize: Dp = 80.dp
    val vLineHeight: TextUnit = 20.sp // You can change this value as needed
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }
    var inputText by remember { mutableStateOf(viewModel.temptopicname.value) }
    viewModel.settemptopicname(inputText)


    val colors = MaterialTheme.colorScheme
// Focus change listener to update isFocused state
    val focusModifier = Modifier
        .focusRequester(focusRequester)
        .onFocusChanged { focusState ->
            isFocused = focusState.isFocused
        }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 3.dp, start = 5.dp, end = 0.dp, bottom = 4.dp),
        verticalAlignment = Alignment.Bottom // Align the contents vertically centered
    ) {
        CustomTextBox(
            inputText = inputText,
            onValueChange = { newText -> inputText = newText },
            vMaxLinesSize = vMaxLinesSize,  // Adjust this as needed
            vFontSize = vFontSize,          // Adjust this as needed
            sPlaceHolder = sPlaceHolder,    // Adjust this as needed
            isFocused = isFocused,         // Adjust this as needed
            focusModifier = focusModifier, // Pass the focus modifier here
            boxModifier = Modifier
                .weight(1f)
                .padding(bottom=5.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton( // CANCEL BUTTON
            onClick = {
                navController.popBackStack()
            },
            modifier = Modifier.size(vButtonSize)
                .align(Alignment.Bottom) // Align button vertically in the center

        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Attach",
                tint = colors.onBackground,
                modifier = Modifier
                    .height(vIconSize)
            )
        }
        val context = LocalContext.current
        Spacer(modifier = Modifier.width(5.dp))
        IconButton( // CONFIRM BUTTON
            onClick = {
                val nColor : Color = viewModel.colour.value
                val iColor : Int = colorToArgb(nColor)
                if (inputText.isNotBlank()) {
                    if (viewModel.fileURI.value.length > 4) {
                        copyIconToAppFolder(context, viewModel)
                    }

                    viewModel.addTopic(
                        //topicName = inputText,
                        topicName = viewModel.temptopicname.value,
                        topicColour = iColor,
                        topicCategory = viewModel.tempcategory.value,
                        topicIcon = viewModel.fileURI.value,
                        topicPriority = 0,
                    )

                    viewModel.setURI("")
                    inputText = ""
                    navController.popBackStack()
                }
            },
            modifier = Modifier
                .size(vButtonSize)
                .fillMaxWidth(1f)
                .align(Alignment.Bottom) // Align button vertically in the center

        ) {
            Icon(
                imageVector = Icons.Filled.Check, // Attach file icon
                contentDescription = "Attach",
                tint = colors.onBackground,
                modifier = Modifier
                    .size(vIconSize)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
    }

// Request focus initially
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}