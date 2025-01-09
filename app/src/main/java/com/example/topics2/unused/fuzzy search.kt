package com.example.topics2.unused

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.topics2.ui.components.CustomSearchBox

@Composable
fun NewSearchScreen(viewModel: UniqueFuzzySearchViewModel = viewModel()) {
    var query by remember { mutableStateOf("") }
    val searchResults by viewModel.uniqueSearchResults.collectAsState()

    var isDialogOpen by remember { mutableStateOf(false) }
    var selectedItem: TableEntry? by remember { mutableStateOf(null) }
    var inputText by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(Color.Black)
            .clip(RoundedCornerShape(4.dp))
    ) {
        CustomSearchBox(
            inputText = query,
            onValueChange = { newtext ->
                query = newtext  // Update the inputText as the user types
                viewModel.performUniqueMixedSearch(query)  // Trigger search with updated query
            },
            sPlaceHolder="Search...",
            isFocused=true,
            focusModifier= Modifier,
            boxModifier=Modifier,
        )



/*
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
                color = colors.onSecondary,
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
            cursorBrush = SolidColor(colors.tertiary) // White cursor
        )
*/


//        BasicTextField(
//            //textStyle = TextStyle(fontSize = 20.sp, color = Color.White),
//            value = query,
//
//            onValueChange = {
//                query = it
//                viewModel.performUniqueMixedSearch(it)
//            },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(4.dp)
//                .background(Color.DarkGray)
//            //.background(Color.DarkGray)
//        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(searchResults) { item ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            selectedItem = item
                            Log.d(
                                "TableEntry",
                                "ID: ${item.messageID}, Content: ${item.messageContent}, Topic: ${item.topicName}"
                            )
                            isDialogOpen = true
                        }
                ) {
                    Text(
                        text = item.topicName,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = item.messageContent,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
        }

        if (isDialogOpen && selectedItem != null) {
            Dialog(onDismissRequest = { isDialogOpen = false }) {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "ID: ${selectedItem!!.messageID}", fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Content: ${selectedItem!!.messageContent}", fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Topic: ${selectedItem!!.topicName}", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}