package com.example.topics2.ui.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomSearchBox(
    inputText: String,
    onValueChange: (String) -> Unit,
    oncHold: () -> Unit = {},
    sPlaceHolder: String = "Enter message",
    bShowSearchNav: Boolean = false,
    isFocused: Boolean = false,
    focusModifier: Modifier = Modifier,
    boxModifier: Modifier = Modifier, // Modifier for the Box
    onNextClick: () -> Unit = {},
    onPreviousClick: () -> Unit = {},
    iSearchCount: Int = 0,
    iCurrentSearch: Int = 0,
) {
    val colors = MaterialTheme.colorScheme
    var isSearchFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
//            .pointerInput(Unit) { detectTapGestures( onLongPress = { oncHold() } ) }
            .padding(top = 5.dp, bottom = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(50),
            color = colors.secondary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(0.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = inputText,
                    onValueChange = {
                        onValueChange(it)
                        isSearchFocused = it.isNotEmpty()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .padding(horizontal = 20.dp, vertical = 0.dp)
                        .focusRequester(focusRequester),
                    textStyle = TextStyle(
                        fontSize = 18.sp,
                        color = colors.onSecondary,
                        lineHeight = 20.sp
                    ),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier.pointerInput(Unit) {
                                detectTapGestures(
                                    onLongPress = { oncHold() },
                                    onTap = {
                                        focusRequester.requestFocus()
                                    }
                                )
                            },
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (inputText.isEmpty() && !isSearchFocused) {
                                Text(
                                    text = sPlaceHolder,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            innerTextField()
                        }
                    },
                    cursorBrush = SolidColor(colors.tertiary) // Cursor color
                )

                Spacer(modifier = Modifier.width(5.dp))

                if (bShowSearchNav) {
                    Text(text = "${iCurrentSearch}/${iSearchCount}", fontSize = 10.sp)
                    IconButton( // Previous
                        onClick = { onPreviousClick() },
                        modifier = Modifier.size(30.dp)
                            .align(Alignment.CenterVertically)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowUp,
                            contentDescription = "Search",
                            tint = colors.onBackground,
                            modifier = Modifier.height(25.dp)
                        )
                    }
                        IconButton( // next
                            onClick = { onNextClick() },
                            modifier = Modifier.size(30.dp)
                                .align(Alignment.CenterVertically)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowDown,
                                contentDescription = "Search",
                                tint = colors.onBackground,
                                modifier = Modifier.height(25.dp)
                            )
                        }

                }

                Spacer(modifier = Modifier.width(5.dp))
            }
        }
    }
}
