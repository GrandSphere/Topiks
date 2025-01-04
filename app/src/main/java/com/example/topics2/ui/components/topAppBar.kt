package com.example.topics2.ui.components

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    title: String,
    onSettingsClick: () -> Unit,
    reloadTopics:() -> Unit
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
                reloadTopics = reloadTopics
            )
        },
    )
}


@Composable
fun CustomTopMenu(
    isMenuExpanded: Boolean,
    onDismiss: () -> Unit,
    reloadTopics: () -> Unit
) {
    val colors = MaterialTheme.colorScheme // Use the theme color scheme

    var showDirectoryPicker by remember { mutableStateOf(false) }
    var showImportPicker by remember { mutableStateOf(false) }
    // Show the directory picker when the state changes
    if (showDirectoryPicker) {
        //ExportDatabaseWithPicker(onExportComplete = {
        //    // Reset state after the export process completes
        //    showDirectoryPicker = false
        //})
    }
    // Show the import picker when the state changes
    if (showImportPicker) {
        //ImportDatabaseWithPicker(onImportComplete = {
        //    reloadTopics()
        //    showImportPicker = false

        //})
    }


    DropdownMenu(
        expanded = isMenuExpanded,
        onDismissRequest = { onDismiss() },
        modifier = Modifier.background(colors.background) // Background color for the dropdown
    ) {
        DropdownMenuItem(
            text = { Text("Export", color = colors.onBackground) },
            onClick = {
                onDismiss()
                showDirectoryPicker = true

            }
        )
        DropdownMenuItem(
            text = { Text("Import", color = colors.onBackground) }, // Use onSurface for text color
            onClick = {
                onDismiss()
                showImportPicker = true
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
