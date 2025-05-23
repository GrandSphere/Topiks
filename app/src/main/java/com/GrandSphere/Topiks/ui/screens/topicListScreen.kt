/*******************************************************************************
 * Copyright (C) <2025> GrandSphere <GrandSphereStudios@protonmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.GrandSphere.Topiks.ui.screens

import ExportDatabaseWithPicker
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.GrandSphere.Topiks.db.enitities.TopicTbl
import com.GrandSphere.Topiks.ui.components.CustomSearchBox
import com.GrandSphere.Topiks.ui.components.addTopic.argbToColor
import com.GrandSphere.Topiks.ui.components.addTopic.chooseColorBasedOnLuminance
import com.GrandSphere.Topiks.ui.focusClear
import com.GrandSphere.Topiks.ui.viewmodels.GlobalViewModelHolder
import com.GrandSphere.Topiks.ui.viewmodels.MenuItem
import com.GrandSphere.Topiks.ui.viewmodels.TopicViewModel
import com.GrandSphere.Topiks.utilities.helper.restartMainActivity
import com.GrandSphere.Topiks.utilities.importDatabaseFromUri
import kotlinx.coroutines.launch

@Composable
fun TopicListScreen(navController: NavController, viewModel: TopicViewModel) {
    val topics by viewModel.topics.collectAsState()
    val searchResults by viewModel.searchResults.observeAsState(emptyList())
    var inputText by remember{ mutableStateOf("")}
    val topBarViewModel = GlobalViewModelHolder.getTopBarViewModel()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    val selectedFileUri: MutableState<Uri> = remember { mutableStateOf(Uri.EMPTY) }
    val openFileLauncher = filePickerScreen { uri -> selectedFileUri.value = uri?: Uri.parse("")}

    val focusModifier = Modifier
        .focusRequester(focusRequester)
        .onFocusChanged { focusState ->
        }

    var toFocusSearchBox by  remember { mutableStateOf(false) }
    LaunchedEffect(toFocusSearchBox) {
        if (toFocusSearchBox) {
            focusManager.clearFocus()
            focusRequester.requestFocus()
            toFocusSearchBox = false
        }
    }

    LaunchedEffect(Unit) {

        topBarViewModel.setMenuItems(
            listOf(
                MenuItem("Export Database") {
                    coroutineScope.launch { ExportDatabaseWithPicker(context) }
                },
                MenuItem("Import Database") {
                    openFileLauncher.launch(arrayOf("*/*"))
                },
                MenuItem("Search All Topics") {
                    navController.navigate("newSearch")
                },
                MenuItem("About") {
                    navController.navigate("navAboutScreen")
                },
                MenuItem("Close") {
                    System.exit(0)
                }
            )
        )
    }
    Box(
        modifier = Modifier
            .clickable(onClick = {focusManager.clearFocus()})
            .fillMaxSize()

    ) {
        Column( // Topic List
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 1.dp)
                .focusClear()
        ) {
            // TODO:: Search Box focus

            CustomSearchBox(
                focusModifier = focusModifier.focusRequester(focusRequester),
                inputText = inputText,
                sPlaceHolder = "Search Topics...",
                onValueChange = { newText ->
                    inputText = newText
                    viewModel.search(newText)
                },
                oncHold = { navController.navigate("newSearch") },
                onClick = { toFocusSearchBox = true},
            )
            LazyColumn(
                modifier = Modifier.fillMaxWidth()

            ) {
                if (inputText.length > 0) {
                    viewModel.search(inputText)
                    items(searchResults.size){
                            index: Int ->
                        val topicID: Int =  searchResults[index].id
                        val topic = viewModel.getTopicObjectById(topicID)
                        if(topic != null) {
                            TopicItem(navController, viewModel, topic)
                        }
                    }
                } else {
                    items(topics.size) { index ->
                        val topic = topics[index]
                        TopicItem(navController, viewModel, topic)
                    }
                }
            }
        }

        FloatingActionButton( // Add new Topic
            onClick = {
                viewModel.setTempCategory("Topics")
                viewModel.setTempTopicName("")
                viewModel.setFileURI("")
                navController.navigate("navaddtopic/-1")
            },
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
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
    var showDialog by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { // Go to specific Topic
                        viewModel.cTopicColor=argbToColor(topic.colour)
                        navController.navigate("navnotescreen/${topic.id}/${topic.name}/-1")
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
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(35.dp)
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            } else {
                Surface( // Show an icon as a fallback if no image URL is provided
                    shape = CircleShape,
                    modifier = Modifier
                        .size(35.dp)
                        .heightIn(max = 35.dp),

                    ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(argbToColor(topic.colour))
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

        if (showDialog) {

            val styledText = buildAnnotatedString {
                append("Delete topic: ")
                withStyle(style = SpanStyle(color = colors.error)) {
                    append(topic.name)
                }
                append(" ?")
            }
            AlertDialog(

                modifier = Modifier
                    .background(Color.Transparent, shape = RoundedCornerShape(8.dp)),
                containerColor = colors.surface,
                tonalElevation = 0.dp,
                onDismissRequest = { showDialog = false },
                title = { Text("Confirm Delete", color = colors.onSurface) },
                text = {
                    Text(
                        styledText,
                        color = colors.onSurface
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        showDialog = false
                        viewModel.deleteTopic(topic.id)
                        coroutineScope.launch { viewModel.deleteMessagesForTopic(topic.id)}
                    }) {
                        Text("Delete", color = colors.onSurface)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancel", color = colors.onSurface)
                    }
                }
            )
        }
        DropdownMenu(
            modifier = Modifier
                .background(colors.surface)
            ,
            expanded = showMenu,
            onDismissRequest = { showMenu = false }
        ) {
            val textColour: Color = colors.onSurface
            DropdownMenuItem( // Edit Topic Button
                text = { Text("Edit", color = textColour) },
                onClick = {
                    viewModel.setEditMode(true)
                    navController.navigate("navaddtopic/${topic.id}")
                    showMenu = false
                }
            )
            DropdownMenuItem( // Delete Topic Button
                text = { Text("Delete", color = textColour) },
                onClick = {
                    showDialog = true
                    showMenu = false
                }
            )
        }
    }
}

// File picker for DB import
@Composable
fun filePickerScreen(
    onFileSelected: (Uri?) -> Unit
): ActivityResultLauncher<Array<String>> {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val selectedFileUri: MutableState<Uri> = remember { mutableStateOf(Uri.EMPTY) }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri: Uri? ->
            selectedFileUri.value = uri ?: Uri.EMPTY
            // TODO: DO additional checks if valid db
            if (uri != null && uri != Uri.EMPTY) {
                coroutineScope.launch {
                    importDatabaseFromUri(context, uri)
                    restartMainActivity(context)
                }
            }
            onFileSelected(uri) // Pass the selected URI back
        }
    )

    return filePickerLauncher
}
