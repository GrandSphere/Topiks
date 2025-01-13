package com.example.topics2.unused

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.topics2.ui.components.CustomSearchBox
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Composable
fun NewSearchScreen(viewModel: UniqueFuzzySearchViewModel = viewModel()) {
    var query by remember { mutableStateOf("") }
    val searchResults by viewModel.uniqueSearchResults.collectAsState()
    val numResultsRender by viewModel.numResultsRender.collectAsState()
    var isDialogOpen by remember { mutableStateOf(false) }
    var selectedItem: TableEntry? by remember { mutableStateOf(null) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(Color.Black)
            .clip(RoundedCornerShape(4.dp))
    ) {
        CustomSearchBox(
            inputText = query,
            onValueChange = { newtext ->
                query = newtext
                viewModel.performUniqueMixedSearch(query)
            },
            sPlaceHolder="Search...",
            isFocused=true,
            focusModifier= Modifier,
            boxModifier=Modifier,
        )
        Spacer(modifier = Modifier.height(16.dp))
        // Use LazySearchResults component and pass the ViewModel
        DisplaySearchResultUx(
            searchResults = searchResults,
            numResultsRender = numResultsRender,
            onItemClick = {
                    item ->
                selectedItem = item
                isDialogOpen = true
            },
            onClick = { viewModel.setnumResultsRender(numResultsRender + 200)},
            onLongClick = { viewModel.setnumResultsRender(searchResults.size)}
        )

        ItemDialogUx( // Dialog for item details, mostly for debuggin
            isDialogOpen = isDialogOpen,
            selectedItem = selectedItem,
            onDismissRequest = { isDialogOpen = false } // Dismiss by setting isDialogOpen to false
        )

    }
}

class UniqueFuzzySearchViewModel : ViewModel() {
    private val _uniqueSearchResults = MutableStateFlow<List<TableEntry>>(emptyList())
    val uniqueSearchResults: StateFlow<List<TableEntry>> = _uniqueSearchResults

    // Adjust for how many results to show
    private val defaultNumResultsRendered: Int = 30
    private val _numResultsRender = MutableStateFlow(defaultNumResultsRendered)
    val numResultsRender: StateFlow<Int> = _numResultsRender
    fun setnumResultsRender(newValue: Int) {
        _numResultsRender.value = newValue
    }

    // This is the large dataset we are working with
    private val dataList = generateTableData(2000)

    // Debounce variable to handle fast search input
    private var debounceJob: Job? = null

    // Search function with debounce and fuzzy search logic
    fun performUniqueMixedSearch(query: String) {
        // Reset the number of results back to 30 whenever a new query is started

        debounceJob?.cancel() // Cancel any ongoing debounce task
        // Start a new debounce task
        debounceJob = viewModelScope.launch {
            delay(200) // Delay for debouncing user input
            _numResultsRender.value = defaultNumResultsRendered
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
            }

            // Update the search results with the filtered data
            _uniqueSearchResults.value = results
        }
    }
}

@Composable
fun ItemDialogUx(
    isDialogOpen: Boolean,
    selectedItem: TableEntry?,
    onDismissRequest: () -> Unit
) {
    if (isDialogOpen && selectedItem != null) {
        Dialog(onDismissRequest = onDismissRequest) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.background,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "ID: ${selectedItem.messageID}", fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Content: ${selectedItem.messageContent}", fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Topic: ${selectedItem.topicName}", fontSize = 16.sp)
                }
            }
        }
    }
}



@Composable
fun DisplaySearchResultUx(
    searchResults: List<TableEntry>,
    numResultsRender: Int,
    onItemClick: (TableEntry) -> Unit,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(searchResults.take(numResultsRender)) { item ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { onItemClick(item) }
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
 if (searchResults.size > numResultsRender) {
            item {
                Text(
                    text = "Show More",
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                        .padding(16.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = { onClick() },
                                onLongPress = { onLongClick() }
                            )
                        }
                )
            }
 }

    }
}