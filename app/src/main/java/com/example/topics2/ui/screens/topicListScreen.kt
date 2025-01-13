package com.example.topics2.ui.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.topics2.db.enitities.TopicTbl
import com.example.topics2.ui.components.addTopic.argbToColor
import com.example.topics2.ui.components.global.chooseColorBasedOnLuminance
import com.example.topics2.ui.viewmodels.TopicViewModel
import kotlinx.coroutines.launch


@Composable
fun TopicListScreen(navController: NavController, viewModel: TopicViewModel) {
    val topics by viewModel.topics.collectAsState()
    val focusManager = LocalFocusManager.current

    //val inputColor = Color.Gray     // Example input color
    //val outputColor = chooseColorBasedOnLuminance(inputColor)

    Box(
        modifier = Modifier
            .fillMaxSize()

    ) {
        // Topic List Column
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 1.dp)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus() // Clear focus when tapping outside
                    })
                }
        ) {
            // TODO:: Search Box focus
            //CustomSearchBox()
            //TextButton() { }
            Spacer(modifier = Modifier.height(10.dp))
            // Topic List
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(topics.size) { index ->
                    val topic = topics[index]
                    TopicItem(navController, viewModel, topic)
                }
            }
        }
        val localContext = LocalContext.current
            // Button to add new topic, aligned at the bottom end of the screen
            FloatingActionButton(
                onClick = {
                    viewModel.setTempCategory("Topics")
                    viewModel.settemptopicname("")
                    viewModel.setFileURI("")
                    navController.navigate("navaddtopic")
                          },
                shape = CircleShape, // Change the shape to rounded corners
                modifier = Modifier
                    .align(Alignment.BottomEnd)
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
    }

@Composable
fun TopicItem(navController: NavController, viewModel: TopicViewModel,  topic: TopicTbl) {
    var showMenu by remember { mutableStateOf(false) }
    val colors = MaterialTheme.colorScheme
    val coroutineScope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            //.background(Color.Transparent)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { // Go to specific Topic
                        viewModel.cTopicColor=argbToColor(topic.colour)
                        //viewModel.setTopicColor(topic)
                        navController.navigate("navnotescreen/${topic.id}/${topic.name}")
                    },
                    onLongPress = { showMenu = true }
                )
            }
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // TODO check image validity AND compress images
            val imageUrl = topic.iconPath
            if (imageUrl != "") {
                // Load and display the image
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = "Circular Image",
                    contentScale = ContentScale.Crop, // Crop the image to fill the circle
                    modifier = Modifier
                        .size(35.dp)
                        .fillMaxSize()
                        .clip(CircleShape) // Clip the image into a circular shape
                )
            } else {
                // Show an icon as a fallback if no image URL is provided
                Surface(
                    //color = colors.primaryContainer,
                    shape = CircleShape, // Ensures the Surface is circular
                    modifier = Modifier
                        .size(35.dp)
                        .heightIn(max = 35.dp),

                    ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(argbToColor(topic.colour))
                            //.background(argbToColor(topic.topicColour))
                            .heightIn(max = 35.dp),
                    ) {
                        Text(
                            text = topic.name.first().toString(),
                            //color = MaterialTheme.colorScheme.onPrimaryContainer,
                            color = chooseColorBasedOnLuminance(argbToColor(topic.colour)),
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = topic.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colors.onBackground
            )
        }

        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false }
        ) {
            DropdownMenuItem( // Delete Topic Button
                text = { Text("Delete") },
                onClick = {
                    viewModel.deleteTopic(topic.id)
                    coroutineScope.launch { viewModel.deleteMessagesForTopic(topic.id)}
                    showMenu = false
                }
            )
        }
    }
}