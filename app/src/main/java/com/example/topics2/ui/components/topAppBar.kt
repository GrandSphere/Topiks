package com.example.topics2.ui.components

import ExportDatabaseWithPicker
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.topics.utilities.importDatabaseFromUri
import com.example.topics2.db.AppDatabase
import com.example.topics2.db.AppDatabase_Impl
import com.example.topics2.ui.viewmodels.TopicViewModel
import iconFilePicker
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    title: String,
    onSettingsClick: () -> Unit,
    reloadTopics:() -> Unit,
    navController: NavController,
    topicViewModel: TopicViewModel
) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    val colors = MaterialTheme.colorScheme

    TopAppBar(
        title = {
            Text(text = title)
        },
        actions = {
            IconButton(onClick = { isMenuExpanded = true }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Settings",
                    //tint = colors.onSurface
                )
            }

            CustomTopMenu (
                isMenuExpanded = isMenuExpanded,
                onDismiss = { isMenuExpanded = false },
                reloadTopics = reloadTopics,
                navController = navController,
                topicViewModel = topicViewModel
            )
        },
    )
}


@Composable
fun CustomTopMenu(
    isMenuExpanded: Boolean,
    onDismiss: () -> Unit,
    reloadTopics: () -> Unit,
    navController: NavController,
    topicViewModel: TopicViewModel
) {
    val colors = MaterialTheme.colorScheme // Use the theme color scheme
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // FilePicker Logic

    // FilePicker Logic
    val selectedFileUri: MutableState<Uri> = remember { mutableStateOf(Uri.EMPTY) }
    val openFileLauncher = iconFilePicker(
        onFileSelected = { uri: Uri? ->
            selectedFileUri.value = uri ?: Uri.parse("")
            Log.d("QQWWEE Debug_ this is where i set", "${uri}")
            // TODO: DO additional checks if valid db
            if (uri != null && uri != Uri.parse("")) {
                coroutineScope.launch {
                    importDatabaseFromUri(context, uri)
                }
            }
        }
    )
    DropdownMenu(
        expanded = isMenuExpanded,
        onDismissRequest = { onDismiss() },
        modifier = Modifier.background(colors.background) // Background color for the dropdown
    ) {
        DropdownMenuItem(
            text = { Text("Export", color = colors.onBackground) },
            onClick = {
                coroutineScope.launch {
                    ExportDatabaseWithPicker(context)
                }
                onDismiss()

            }
        )
        DropdownMenuItem(
            text = { Text("Import", color = colors.onBackground) }, // Use onSurface for text color
            onClick = {
                openFileLauncher.launch(arrayOf("*/*"))
                AppDatabase.getDatabase(context)
                val databaseName = "topics_database"
                val currentDatabaseFile = File(context.getDatabasePath(databaseName).absolutePath)

                Log.d("QQWWEE THIS IS SELECTED", "${currentDatabaseFile}")
                onDismiss()
            }
        )
        DropdownMenuItem(
            text = { Text("Close", color = colors.onBackground) }, // Use onSurface for text color
            onClick = {
                onDismiss()
                // Handle Close action here
            }
        )
    }
}
