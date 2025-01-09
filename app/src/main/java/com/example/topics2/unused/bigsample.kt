package com.example.topics2.unused

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.random.Random

// Composable for the unique fuzzy search screen
@Composable
fun UniqueFuzzySearchScreen(viewModel: UniqueFuzzySearchViewModel = viewModel()) {
    var query by remember { mutableStateOf("") }
    val searchResults by viewModel.uniqueSearchResults.collectAsState()

    var isDialogOpen by remember { mutableStateOf(false) }
    var selectedItemText by remember { mutableStateOf("") }

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

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(searchResults) { item ->
                Text(
                    text = item,
                    fontSize = 16.sp,
                    color = Color.White,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            selectedItemText = item
                            isDialogOpen = true
                        }
                )
            }
        }

        if (isDialogOpen) {
            Dialog(onDismissRequest = { isDialogOpen = false }) {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Text(text = selectedItemText)
                }
            }
        }
    }
}

// ViewModel for the fuzzy search
class UniqueFuzzySearchViewModel : ViewModel() {
    private val _uniqueSearchResults = MutableStateFlow<List<String>>(emptyList())
    val uniqueSearchResults: StateFlow<List<String>> = _uniqueSearchResults

    // This is the large dataset we are working with
    private val dataList = generateMyLista(2000) + generateSampleList()

    // Debounce variable to handle fast search input
    private var debounceJob: Job? = null

    // Search function with debounce and fuzzy search logic
    fun performUniqueMixedSearch(query: String) {
        debounceJob?.cancel() // Cancel any ongoing debounce task

        // Start a new debounce task
        debounceJob = viewModelScope.launch {
            delay(300) // Delay for debouncing user input
            val keywords = query.lowercase().split(" ").filter { it.isNotBlank() }

            val results = dataList.filter { item ->
                val lowercasedItem = item.lowercase()

                // Process each keyword to check for exclusion
                keywords.all { keyword ->
                    if (keyword.startsWith("!")) {
                        // If keyword starts with "!", exclude results containing this substring
                        !lowercasedItem.contains(keyword.substring(1)) // Remove the "!"
                    } else {
                        // Otherwise, include results that contain the substring
                        lowercasedItem.contains(keyword)
                    }
                }
            }

            _uniqueSearchResults.value = results
        }
    }
}

// Function to generate the list of strings (50,000 entries)
fun generateMyLista(numberOfEntries: Int): List<String> {
    // Define vocabulary categories
    val adjectives = listOf("beautiful", "fast", "slow", "ancient", "bright", "dark", "tall", "small", "grumpy", "peaceful", "shiny", "dirty", "quiet", "loud", "colorful")
    val nouns = listOf("cat", "dog", "bird", "tree", "car", "mountain", "river", "city", "forest", "lake", "house", "sky", "sun", "moon", "star")
    val verbs = listOf("runs", "flies", "sits", "jumps", "swims", "walks", "climbs", "dives", "sings", "barks", "howls", "shines", "builds", "paints", "explores")
    val adverbs = listOf("quickly", "loudly", "gracefully", "happily", "silently", "randomly", "brightly", "gently", "strongly", "awkwardly", "suddenly", "playfully")

    // Function to generate a random sentence
    fun generateRandomSentence(): String {
        // Choose a random structure for the sentence
        val structure = Random.nextInt(1, 4)

        return when (structure) {
            1 -> {
                // Adjective + Noun + Verb + Adverb
                "${adjectives.random()} ${nouns.random()} ${verbs.random()} ${adverbs.random()}"
            }
            2 -> {
                // Noun + Verb + Noun + Verb
                "${nouns.random()} ${verbs.random()} ${nouns.random()} ${verbs.random()}"
            }
            3 -> {
                // Adjective + Noun + Verb
                "${adjectives.random()} ${nouns.random()} ${verbs.random()}"
            }
            else -> {
                // Fallback to a simple combination of a verb and two nouns
                "${verbs.random()} ${nouns.random()} ${nouns.random()}"
            }
        }
    }

    // Generate the list of sentences
    return (1..numberOfEntries).map { generateRandomSentence() }
}