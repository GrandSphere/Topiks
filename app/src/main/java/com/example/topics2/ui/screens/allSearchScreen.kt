package com.example.topics2.ui.screens

//import com.example.topics2.model.allSearchHandler
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.topics.ui.themes.cSearchTopicFont
import com.example.topics2.ui.components.CustomSearchBox
import com.example.topics2.ui.viewmodels.GlobalViewModelHolder
import com.example.topics2.ui.viewmodels.MessageViewModel
import com.example.topics2.ui.viewmodels.searchViewModel

@Composable
fun allSearch( messageViewModel: MessageViewModel, searchViewModel: searchViewModel, navController: NavController,
               highlightColor: Color = MaterialTheme.colorScheme.surfaceVariant) {

    messageViewModel.collectSearchMessages()
    var inputText by remember { mutableStateOf("") }
    val dataset by messageViewModel.searchMessages.collectAsState(emptyList())
    val searchResults by searchViewModel.searchResults.observeAsState(emptyList())

    val colours = MaterialTheme.colorScheme
    val topBarViewModel = GlobalViewModelHolder.getTopBarViewModel()

    val focusManager = LocalFocusManager.current // For clearing focus
    val focusRequester = remember { FocusRequester() }

    val focusModifier = Modifier // Used to set edit cursor
        .focusRequester(focusRequester)
       // .onFocusChanged { focusState ->
       //     isFocused = focusState.isFocused
       // }

    var toFocusSearchBox by  remember { mutableStateOf(false) }
    LaunchedEffect(toFocusSearchBox) {
        if (toFocusSearchBox) {
            focusManager.clearFocus()
            focusRequester.requestFocus()
            toFocusSearchBox = false
        }
    }
    LaunchedEffect(Unit) {
        toFocusSearchBox = true
        topBarViewModel.setMenuItems(
            listOf(
            )
        )
    }
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
            focusModifier = focusModifier.focusRequester(focusRequester),
            onClick = { toFocusSearchBox = true},
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
                    .clickable {
                        val topicId = searchResults[item].topicId
                        val name = searchResults[item].topicName
                        val messageId = searchResults[item].id
                        //messageViewModel.setSelectedSearchMessageID(messageId)
                        Log.d("QQWWEE", "THIS IS THE MESAGEID: ${messageId}")
                        Log.d("QQWWEE", "Navigating to navnotescreen with TopicId: $topicId and name: $name and messageID:$messageId")
                        navController.navigate("navnotescreen/${topicId}/${name}/${messageId}")
                    }
                ) {
                    Text( // Topic
                        text=searchResults[item].topicName.toString(),
                        style = cSearchTopicFont,

                        )
                    Text( // result string
                        text = buildAnnotatedString {
                           // withStyle(style = SpanStyle(color = colours.onSecondary)) {
                           //     append(searchResults[item].content.take(8) + " ")
                           // }

                            val normalizedQuery = inputText.split(" ").map { it.trim() }.filter { it.isNotEmpty() }
                            val contentWords = searchResults[item].content.split(" ")

                            contentWords.forEach { word ->
                                var currentIndex = 0 // Track the current position in the word

                                normalizedQuery.forEach { substring ->
                                    while (true) {
                                        val matchIndex = word.indexOf(substring, currentIndex, ignoreCase = true)
                                        if (matchIndex == -1) break

                                        // Append the part before the match
                                        withStyle(style = SpanStyle(color = colours.onBackground)) {
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
                                    withStyle(style = SpanStyle(color = colours.onBackground)) {
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
        BackHandler(onBack = {
            searchViewModel.setSearchResultsEmpty()
            navController.popBackStack()
        })
    }
}