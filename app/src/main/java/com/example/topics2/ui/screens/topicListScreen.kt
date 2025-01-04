package com.example.topics2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.topics2.ui.viewmodels.topicViewModel

@Composable
fun TopicListScreen(navController: NavController, viewModel: topicViewModel = viewModel()) {

    val focusManager = LocalFocusManager.current // For managing focus
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus() // Clear focus when tapping outside
                })
            }
    ) {
        // Topic List Column
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 1.dp)
        ) {
            // Search Box
            //fSearchBox() // No changes required for the SearchBox

            // Topic List
            LazyColumn(
                modifier = Modifier.weight(1f) // Ensure LazyColumn takes up all available space
            ) {
               // items(topics.size) { index ->
               //     val topic = topics[index]
               //     TopicItem(
               //         topic = topic,
               //         onClick = { onTopicClick(topic) },
               //         onDelete = { onTopicDelete(topic.topicId) }
                    //)
                }
            }
        }

        // Button to add new topic, aligned at the bottom end of the screen
        FloatingActionButton(
            onClick = {  },
            modifier = Modifier
                //.align(Alignment.BottomEnd) // Align it to bottom end of the Box
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Topic",
                modifier = Modifier.size(24.dp)
            )
        }
    }


@Composable
fun TopicItem(navController: NavController, viewModel: topicViewModel = viewModel()) {
    var showMenu by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .pointerInput(Unit) {
                detectTapGestures(
                    //onTap = { onClick() },
                    onTap = {  },
                    //onLongPress = { showMenu = true }
                    onLongPress = {  }
                )
            }
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = CircleShape, // Ensures the Surface is circular
                modifier = Modifier
                    .size(35.dp)
                    .heightIn(max = 35.dp),

                ) {
                Box(
                    contentAlignment = Alignment.Center, // Center the text inside the circle
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent)
                        .heightIn(max = 35.dp),
                ) {
                    Text(
                        //text = topic.topicName.first().toString(),
                        text = "",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                //text = topic.topicName,
                text = "",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false }
        ) {
            DropdownMenuItem(
                text = { Text("Delete") },
                onClick = {
                    //showMenu = false
                    //onDelete()
                }
            )
        }
    }


}

