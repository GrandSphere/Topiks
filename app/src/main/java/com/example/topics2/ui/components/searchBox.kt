package com.example.topics2.ui.components

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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomSearchBox() {

    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    var isSearchFocused by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top=5.dp, bottom=5.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(50),
            color = MaterialTheme.colorScheme.secondary,
            //color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal=12.dp, vertical=0.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(0.dp),
                //.height(40.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = searchText,
                    onValueChange = {
                        searchText = it
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .padding(horizontal = 20.dp, vertical = 0.dp),
                    textStyle = TextStyle(
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSecondary,
                        lineHeight = 20.sp
                    ),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        Box(contentAlignment = Alignment.CenterStart) {
                            if (searchText.text.isEmpty() && !isSearchFocused) {
                                Text(
                                    text = "Search...",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            innerTextField()
                        }
                    },
                    cursorBrush = SolidColor(Color.White) // White cursor
                )

                Spacer(modifier = Modifier.width(5.dp))
                if (isSearchFocused) {

                    IconButton( // ADD BUTTON
                        onClick = {
                            // Handle button click (e.g., show file picker or attachment options)
                        },
                        modifier = Modifier.size(30.dp)
                            .align(Alignment.CenterVertically) // Align button vertically in the center

                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search, // Attach file icon
                            contentDescription = "Attach",
                            tint = Color.White, // Set the icon color to white
                            modifier = Modifier
                                .height(25.dp)
                            //.align(Alignment.Center)
                        )
                    }

                    Spacer(modifier = Modifier.width(5.dp))

                }
            }
        }
    }

    // Update search focus state
    LaunchedEffect(searchText.text) {
        isSearchFocused = searchText.text.isNotEmpty()
    }
}
