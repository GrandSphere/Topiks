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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.random.Random


// Data class representing the table with a cached lowercase version of the message content
data class TableEntry(
    val messageID: Int,
    val messageContent: String,
    val messageContentLower: String, // Cached lowercase content
    val topicName: String
)

// Composable for the unique fuzzy search screen
@Composable
fun UniqueFuzzySearchScreen(viewModel: UniqueFuzzySearchViewModel = viewModel()) {
    var query by remember { mutableStateOf("") }
    val searchResults by viewModel.uniqueSearchResults.collectAsState()

    var isDialogOpen by remember { mutableStateOf(false) }
    var selectedItem: TableEntry? by remember { mutableStateOf(null) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(Color.DarkGray)
            .clip(RoundedCornerShape(4.dp))
    ) {
        BasicTextField(
            textStyle = TextStyle(fontSize = 20.sp),
            value = query,
            onValueChange = {
                query = it
                viewModel.performUniqueMixedSearch(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        )

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

// ViewModel for the fuzzy search
class UniqueFuzzySearchViewModel : ViewModel() {
    private val _uniqueSearchResults = MutableStateFlow<List<TableEntry>>(emptyList())
    val uniqueSearchResults: StateFlow<List<TableEntry>> = _uniqueSearchResults

    // This is the large dataset we are working with
    private val dataList = generateTableData(2000)

    // Debounce variable to handle fast search input
    private var debounceJob: Job? = null

    // Search function with debounce and fuzzy search logic
    fun performUniqueMixedSearch(query: String) {
        debounceJob?.cancel() // Cancel any ongoing debounce task

        // Start a new debounce task
        debounceJob = viewModelScope.launch {
            delay(300) // Delay for debouncing user input
            val keywords = query.lowercase().split(" ").filter { it.isNotBlank() }

            val results = dataList.filter { entry ->
                keywords.all { keyword ->
                    if (keyword.startsWith("!")) {
                        // Exclude results containing the substring
                        !entry.messageContentLower.contains(keyword.substring(1))
                    } else {
                        // Include results containing the substring
                        entry.messageContentLower.contains(keyword)
                    }
                }
            }.take(30) // Limit to top 30 results

            _uniqueSearchResults.value = results
        }
    }
}

// Function to generate the table data
fun generateTableData(numberOfEntries: Int): List<TableEntry> {
    val adjectives = listOf(
        "beautiful", "fast", "slow", "ancient", "bright", "dark",
        "tall", "small", "grumpy", "peaceful", "shiny", "dirty",
        "quiet", "loud", "colorful"
    )
    val nouns = listOf(
        "cat", "dog", "bird", "tree", "car", "mountain",
        "river", "city", "forest", "lake", "house", "sky",
        "sun", "moon", "star"
    )
    val verbs = listOf(
        "runs", "flies", "sits", "jumps", "swims", "walks",
        "climbs", "dives", "sings", "barks", "howls", "shines",
        "builds", "paints", "explores"
    )
    val adverbs = listOf(
        "quickly", "loudly", "gracefully", "happily", "silently",
        "randomly", "brightly", "gently", "strongly", "awkwardly",
        "suddenly", "playfully"
    )

    fun generateRandomSentence(): String {
        val structure = Random.nextInt(1, 4)
        return when (structure) {
            1 -> "${adjectives.random()} ${nouns.random()} ${verbs.random()} ${adverbs.random()}"
            2 -> "${nouns.random()} ${verbs.random()} ${nouns.random()} ${verbs.random()}"
            3 -> "${adjectives.random()} ${nouns.random()} ${verbs.random()}"
            else -> "${verbs.random()} ${nouns.random()} ${nouns.random()}"
        }
    }

    val topics = List(100) { "Topic ${it + 1}" }

    return (1..numberOfEntries).map {
        val content = generateRandomSentence()
        TableEntry(
            messageID = it,
            messageContent = content,
            messageContentLower = content.lowercase(),
            topicName = topics.random()
        )
    }
}