package com.example.topics2.unused
// this one works better
/*

// Necessary Imports
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.*
import kotlin.random.Random

// Data Classes
//data class TableEntry(
//    val messageID: Int,
//    val messageContent: String,
//    val messageContentLower: String,
//    val topicName: String
//)

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
            //delay(debounceTime)
            if (query.isNotEmpty()) { // blue: Handle empty search query (shows no results)
                results = dataset.filter { entry ->
                    includes.all { it in entry.messageContentLower } &&
                            excludes.none { it in entry.messageContentLower }
                }.take(10000)
            } else {
                results = emptyList() // blue: Ensure no results are shown for empty queries
            }
            onResults(results)
        }
    }
}

// Composable UI for Search Results
@Composable
fun T2SearchUI(dataset: List<TableEntry>, highlightColor: Color = Color.Yellow) {
    var query by remember { mutableStateOf("") }
    var results by remember { mutableStateOf<List<TableEntry>>(emptyList()) }
    val searchHandler = remember { T2SearchHandler(dataset) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        TextField(
            value = query,
            onValueChange = { newQuery ->
                query = newQuery
                searchHandler.search(newQuery) { updatedResults -> results = updatedResults }
            },
            modifier = Modifier.fillMaxWidth().background((Color.DarkGray)),
            placeholder = { Text("Search...", color = Color.Gray) }
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
                            val contentWords = item.messageContent.split(" ")
                            contentWords.forEach { word ->
                                if (query.split(" ").any { it.equals(word, true) }) {
                                    withStyle(style = SpanStyle(color = highlightColor)) {
                                        append("$word ")
                                    }
                                } else {
                                    withStyle(style = SpanStyle(color = Color.White)) {
                                        append("$word ")
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}


// Call this function to run the app
@Composable
fun T2RunApp() {
    //val testDataset = generateTableData(1000)
    //androidx.compose.ui.window.singleWindowApplication {
    T2SearchUI(generateTableData(500000))
}
*/