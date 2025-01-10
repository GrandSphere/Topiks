package com.example.topics2.unused


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

    fun search(query: String, debounceTime: Long = 200L, onResults: (List<TableEntry>) -> Unit) {
        val includes = mutableListOf<String>()
        val excludes = mutableListOf<String>()
        query.split(" ").forEach { part ->
            if (part.startsWith("!")) excludes.add(part.substring(1).lowercase())
            else includes.add(part.lowercase())
        }

        // Perform debounced search
        GlobalScope.launch {
            delay(debounceTime)
            results = dataset.filter { entry ->
                includes.all { it in entry.messageContentLower } &&
                        excludes.none { it in entry.messageContentLower }
            }.take(100)
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

// Example Test Dataset and Main Function
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

    fun generateRandomSentence(index: Int): String {
        val structure = Random.nextInt(1, 4)
        return when (structure) {
            1 -> "${adjectives.random()} ${nouns.random()} ${verbs.random()} ${adverbs.random()} $index"
            2 -> "${nouns.random()} ${verbs.random()} ${nouns.random()} ${verbs.random()} $index"
            3 -> "${adjectives.random()} ${nouns.random()} ${verbs.random()} $index"
            else -> "${verbs.random()} ${nouns.random()} ${nouns.random()} $index"
        }
    }

    val topics = List(100) { "Topic ${it + 1}" }

    return (1..numberOfEntries).map { index ->
        val content = generateRandomSentence(index)
        TableEntry(
            messageID = index,
            messageContent = content,
            messageContentLower = content.lowercase(),
            topicName = topics.random()
        )
    }
}

// Call this function to run the app
@Composable
fun T2RunApp() {

    //val testDataset = generateTableData(1000)
    //androidx.compose.ui.window.singleWindowApplication {
        T2SearchUI(generateTableData(2000000))
    }