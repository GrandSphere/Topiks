package com.example.topics2.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.topics.ui.themes.cSearchTopicFont
//import com.example.topics2.model.allSearchHandler
import com.example.topics2.ui.components.CustomSearchBox
import com.example.topics2.ui.viewmodels.MessageViewModel
import com.example.topics2.ui.viewmodels.searchViewModel

@Composable
fun allSearch( messageViewModel: MessageViewModel, searchViewModel: searchViewModel,
               highlightColor: Color = Color.Yellow) {

    messageViewModel.collectSearchMessages()
    var inputText by remember { mutableStateOf("") }
    val dataset by messageViewModel.searchMessages.collectAsState(emptyList())
    val searchResults by searchViewModel.searchResults.observeAsState(emptyList())

    LaunchedEffect (dataset){
        if (dataset.isNotEmpty())
        {
            searchViewModel.updateAllSearchDataset(dataset)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp, vertical = 1.dp)
            .clip(RoundedCornerShape(4.dp))
    ) {
        CustomSearchBox(
            inputText = inputText,
            onValueChange = { newText ->
                inputText = newText
                searchViewModel.allSearch(newText)
            },
            sPlaceHolder = "Search...",
            isFocused = true,
            focusModifier = Modifier,
            boxModifier = Modifier,
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(searchResults.size) { item ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {

                    Text( // Topic
                        text=searchResults[item].topicName.toString(),
                        style = cSearchTopicFont,
                    )
                    Text( // result string
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = Color.Gray)) {
                                append(searchResults[item].content.take(8) + " ")
                            }

                            val normalizedQuery = inputText.split(" ").map { it.trim() }.filter { it.isNotEmpty() }
                            val contentWords = searchResults[item].content.split(" ")

                            contentWords.forEach { word ->
                                var currentIndex = 0 // Track the current position in the word

                                normalizedQuery.forEach { substring ->
                                    while (true) {
                                        val matchIndex = word.indexOf(substring, currentIndex, ignoreCase = true)
                                        if (matchIndex == -1) break

                                        // Append the part before the match
                                        withStyle(style = SpanStyle(color = Color.White)) {
                                            append(word.substring(currentIndex, matchIndex))
                                        }

                                        // Append the matched part
                                        withStyle(style = SpanStyle(color = highlightColor)) {
                                            append(word.substring(matchIndex, matchIndex + substring.length))
                                        }

                                        // Move the index forward
                                        currentIndex = matchIndex + substring.length
                                    }
                                }

                                // Append the remaining part of the word
                                if (currentIndex < word.length) {
                                    withStyle(style = SpanStyle(color = Color.White)) {
                                        append(word.substring(currentIndex))
                                    }
                                }

                                append(" ") // Add space between words
                            }
                        }
                    )
                }
            }
        }
    }
}