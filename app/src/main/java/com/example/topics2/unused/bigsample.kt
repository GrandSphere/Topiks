package com.example.topics2.unused
import android.util.Log
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel


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
            .background(Color.Black)
            .clip(RoundedCornerShape(4.dp))
    ) {
        BasicTextField(
            textStyle = TextStyle(fontSize = 20.sp, color = Color.White),
            value = query,

            onValueChange = {
                query = it
                viewModel.performUniqueMixedSearch(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                //.background(Color.DarkGray)
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