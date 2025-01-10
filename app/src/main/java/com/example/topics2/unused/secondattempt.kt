package com.example.topics2.unused
// Necessary Imports
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.topics2.ui.components.CustomSearchBox
import kotlinx.coroutines.*
import kotlin.random.Random

// Data Classes

// ViewModel-like functionality using a single class
class T2SearchHandler(private val dataset: List<TableEntry>) {
    private var results: List<TableEntry> = listOf()
    private var currentJob: Job? = null // blue: Store current search job

    fun search(query: String, debounceTime: Long = 150L, onResults: (List<TableEntry>) -> Unit) {
        // blue: Cancel the previous job if there is one before starting a new one
        currentJob?.cancel()

        val includes = mutableListOf<String>()
        val excludes = mutableListOf<String>()
        query.split(" ").forEach { part ->
            if (part.startsWith("!")) excludes.add(part.substring(1).lowercase())
            else includes.add(part.lowercase())
        }

        // blue: Perform debounced search in a coroutine
        currentJob = GlobalScope.launch {
            delay(debounceTime) // blue: Ensure debounce before starting the search

            if (query.isNotEmpty()) { // blue: Handle empty search query (shows no results)
                results = dataset.filter { entry ->
                    includes.all { word -> word in entry.messageContentLower } && // blue: Checking for substrings
                            excludes.none { word -> word in entry.messageContentLower }
                }.take(20)
            } else {
                results = emptyList() // blue: Ensure no results are shown for empty queries
            }
            onResults(results)
        }
    }
}

@Composable
fun T2SearchUI(dataset: List<TableEntry>, highlightColor: Color = Color.Yellow) {
    var query by remember { mutableStateOf("") }
    var results by remember { mutableStateOf<List<TableEntry>>(emptyList()) }
    val searchHandler = remember { T2SearchHandler(dataset) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(4.dp))
    ) {
        CustomSearchBox(
            inputText = query,
            onValueChange = { newQuery ->
                query = newQuery
                searchHandler.search(newQuery) { updatedResults -> results = updatedResults }
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
            items(results) { item ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = Color.Gray)) {
                                append(item.topicName.take(8) + " ")
                            }

                            val normalizedQuery = query.split(" ").map { it.trim() }.filter { it.isNotEmpty() }
                            val contentWords = item.messageContent.split(" ")

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


@Composable
fun T2RunApp() {
    T2SearchUI(generateTableData(2000000)) // blue: Updated dataset size for testing
}