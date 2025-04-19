package com.GrandSphere.Topiks.ui.screens
// Moved to viewmodel

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
                    // Handle cleanup here
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
        // Topic List Column
        Column(
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
            // Topic List
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
        // Button to add new topic, aligned at the bottom end of the screen
        FloatingActionButton(
            onClick = {
                viewModel.setTempCategory("Topics")
                viewModel.setTempTopicName("")
                viewModel.setFileURI("")
                navController.navigate("navaddtopic/-1")
            },
            shape = CircleShape,
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
    var showDialog by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            //.background(Color.Transparent)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { // Go to specific Topic
                        viewModel.cTopicColor=argbToColor(topic.colour)
                        //viewModel.setTopicColor(topic)
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
                    contentScale = ContentScale.Crop, // Crop the image to fill the circle
                    modifier = Modifier
                        .size(35.dp)
                        .fillMaxSize()
                        .clip(CircleShape)
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

        if (showDialog) {

            val styledText = buildAnnotatedString {
                append("Delete topic: ") // Default text
//                withStyle(style = SpanStyle(color = argbToColor(topic.colour))) {
                withStyle(style = SpanStyle(color = colors.error)) {
                    append(topic.name) // Styled part (topic.name)
                }
                append(" ?") // End of the text
            }
            AlertDialog(

                modifier = Modifier
                    .background(Color.Transparent, shape = RoundedCornerShape(8.dp)),
//                containerColor = colors.background,
                containerColor = colors.surface,
//                 containerColor = Color.Red.copy(alpha = 0.7f),
                tonalElevation = 0.dp,
                onDismissRequest = { showDialog = false }, // Close on outside tap
                title = { Text("Confirm Delete", color = colors.onSurface) },
//                text = { Text("Delete topic: '${topic.name}'?", color = colors.onBackground) },
                text = {
                    Text(
                        styledText,
                        color = colors.onSurface // Default text color
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
//                .fillMaxHeight()
//                .fillMaxWidth(0.4f)
//                .background(colors.background)
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
