package com.example.topics2.unused


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.xdrop.fuzzywuzzy.FuzzySearch

import androidx.compose.material3.Text
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.ui.Modifier
/*
fun generateSampleList(): List<String> {
    return listOf(
        "Apple Banana Orange", "Blueberry Strawberry Blackberry", "Beetroot Spinach Carrot",
        "Strawberry Banana Milkshake", "Grapes Pineapple Mango", "Orange Mango Smoothie",
        "Banana Split Dessert", "Apple Pie", "Blackberry Jam", "Carrot Cake", "Beetroot Juice",
        "Strawberry Ice Cream", "Blueberry Pancakes", "Blackberry Tart", "Pineapple Upside Down Cake",
        "Grape Jelly", "Orange Sorbet", "Mango Lassi", "Peach Cobbler", "Cherry Pie",
        "Watermelon Salad", "Coconut Water", "Pear Tart", "Kiwi Smoothie", "Lemonade",
        "Lime Margarita", "Guava Jam", "Papaya Salad", "Plum Sauce", "Raspberry Tart",
        "Cranberry Sauce", "Fig Newtons", "Pomegranate Juice", "Apricot Jam", "Dragonfruit Bowl",
        "Lychee Martini", "Passionfruit Cake", "Tangerine Sorbet", "Starfruit Salad",
        "Durian Pudding", "Avocado Toast", "Jackfruit Chips", "Tomato Soup", "Cucumber Sandwich",
        "Pumpkin Pie", "Carrot Muffin", "Broccoli Salad", "Cauliflower Mash", "Spinach Lasagna",
        "Lettuce Wrap", "Zucchini Bread", "Eggplant Parmesan", "Potato Gratin", "Onion Rings",
        "Garlic Bread", "Radish Pickle", "Beetroot Hummus", "Asparagus Soup", "Artichoke Dip",
        "Celery Sticks", "Turnip Mash", "Parsnip Chips", "Leek Tart", "Okra Stew", "Chilli Sauce",
        "Peas Soup", "Beans Salad", "Cornbread", "Squash Soup", "Brussels Sprouts Casserole",
        "Kale Chips", "Collard Greens Stir Fry", "Swiss Chard Salad", "Arugula Pesto", "Cabbage Slaw"
        // Add more strings as needed to reach 500
    )
}

class FuzzySearchViewModel : ViewModel() {
    private val _searchResults = MutableStateFlow<List<String>>(emptyList())
    val searchResults: StateFlow<List<String>> = _searchResults

    private val dataList = generateSampleList()

    fun performExactSequentialSearch(query: String) {
        viewModelScope.launch {
            val keywords = query.split(" ").filter { it.isNotBlank() }
            val results = dataList.filter { item ->
                var currentIndex = 0
                keywords.all { keyword ->
                    val index = item.indexOf(keyword, currentIndex)
                    if (index >= currentIndex) {
                        currentIndex = index + keyword.length
                        true
                    } else {
                        false
                    }
                }
            }
            _searchResults.value = results
        }
    }
}

@Composable
fun FuzzySearchScreen(viewModel: FuzzySearchViewModel = viewModel()) {
    var query by remember { mutableStateOf("") }
    val searchResults by viewModel.searchResults.collectAsState()

    Column(modifier = Modifier.padding(16.dp).background(Color.White)) {
        BasicTextField(
            value = query,
            onValueChange = {
                query = it
                viewModel.performExactSequentialSearch(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(searchResults.size) { index ->
                Text(text = searchResults[index], modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@Composable
fun App() {
    FuzzySearchScreen()
}

 */

/*

 */
fun generateSampleList(): List<String> {
    return listOf(
        "Apple Banana Orange", "Blueberry Strawberry Blackberry", "Beetroot Spinach Carrot",
        "Strawberry Grapes", "Grapes Pineapple Mango", "Orange Mango Smoothie",
        "Banana Split Dessert", "Apple Pie", "Blackberry Jam", "Carrot Cake", "Beetroot Juice",
        "Strawberry Ice Cream", "Blueberry Pancakes", "Blackberry Tart", "Pineapple Upside Down Cake",
        "Grape Jelly", "Orange Sorbet", "Mango Lassi", "Peach Cobbler", "Cherry Pie",
        "Watermelon Salad", "Coconut Water", "Pear Tart", "Kiwi Smoothie", "Lemonade",
        "Lime Margarita", "Guava Jam", "Papaya Salad", "Plum Sauce", "Raspberry Tart",
        "Cranberry Sauce", "Fig Newtons", "Pomegranate Juice", "Apricot Jam", "Dragonfruit Bowl",
        "Lychee Martini", "Passionfruit Cake", "Tangerine Sorbet", "Starfruit Salad",
        "Durian Pudding", "Avocado Toast", "Jackfruit Chips", "Tomato Soup", "Cucumber Sandwich",
        "Pumpkin Pie", "Carrot Muffin", "Broccoli Salad", "Cauliflower Mash", "Spinach Lasagna",
        "Lettuce Wrap", "Zucchini Bread", "Eggplant Parmesan", "Potato Gratin", "Onion Rings",
        "Garlic Bread", "Radish Pickle", "Beetroot Hummus", "Asparagus Soup", "Artichoke Dip",
        "Celery Sticks", "Turnip Mash", "Parsnip Chips", "Leek Tart", "Okra Stew", "Chilli Sauce",
        "Peas Soup", "Beans Salad", "Cornbread", "Squash Soup", "Brussels Sprouts Casserole",
        "Kale Chips", "Collard Greens Stir Fry", "Swiss Chard Salad", "Arugula Pesto", "Cabbage Slaw"
        // Add more strings as needed to reach 500
    )
}
/*
class FuzzySearchViewModel : ViewModel() {
    private val _searchResults = MutableStateFlow<List<String>>(emptyList())
    val searchResults: StateFlow<List<String>> = _searchResults

    private val dataList = generateSampleList()

    fun performMixedSearch(query: String) {
        viewModelScope.launch {
            val keywords = query.lowercase().split(" ").filter { it.isNotBlank() }

            val results = dataList.filter { item ->
                val lowercasedItem = item.lowercase()

                // Check for ordered matches (substrings without spaces must appear in order)
                val orderedKeywords = keywords.filter { !it.contains(" ") }
                var currentIndex = 0
                val orderedMatch = orderedKeywords.all { keyword ->
                    val index = lowercasedItem.indexOf(keyword, currentIndex)
                    if (index >= currentIndex) {
                        currentIndex = index + keyword.length
                        true
                    } else {
                        false
                    }
                }

                // Check for unordered matches (substrings with spaces can appear in any order)
                val unorderedKeywords = keywords.filter { it.contains(" ") }
                val unorderedMatch = unorderedKeywords.all { keyword ->
                    lowercasedItem.contains(keyword)
                }

                // Combine results
                orderedMatch && unorderedMatch
            }

            _searchResults.value = results
        }
    }
}

@Composable
fun FuzzySearchScreen(viewModel: FuzzySearchViewModel = viewModel()) {
    var query by remember { mutableStateOf("") }
    val searchResults by viewModel.searchResults.collectAsState()

    Column(modifier = Modifier.padding(16.dp).background(Color.Red)) {
        BasicTextField(
            value = query,
            onValueChange = {
                query = it
                viewModel.performMixedSearch(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(searchResults.size) { index ->
                Text(text = searchResults[index], modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@Composable
fun App() {
    FuzzySearchScreen()
}*/

@Composable
fun FuzzySearchScreen(viewModel: FuzzySearchViewModel = viewModel()) {
    var query by remember { mutableStateOf("") }
    val searchResults by viewModel.searchResults.collectAsState()

    Column(modifier = Modifier.padding(16.dp).background(Color.Red)) {
        BasicTextField(
            value = query,
            onValueChange = {
                query = it
                viewModel.performMixedSearch(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(searchResults.size) { index ->
                Text(text = searchResults[index], modifier = Modifier.padding(8.dp))
            }
        }
    }
}

class FuzzySearchViewModel : ViewModel() {
    private val _searchResults = MutableStateFlow<List<String>>(emptyList())
    val searchResults: StateFlow<List<String>> = _searchResults

    private val dataList = generateSampleList()

    fun performMixedSearch(query: String) {
        viewModelScope.launch {
            // Split the query into substrings (case-insensitive)
            val keywords = query.lowercase().split(" ").filter { it.isNotBlank() }

            val results = dataList.filter { item ->
                val lowercasedItem = item.lowercase()

                // Ensure all substrings (keywords) are found in the item in any order
                keywords.all { keyword ->
                    lowercasedItem.contains(keyword)
                }
            }

            _searchResults.value = results
        }
    }
}