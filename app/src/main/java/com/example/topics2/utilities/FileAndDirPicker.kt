package com.example.topics.utilities

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.topics2.ui.viewmodels.MessageViewModel

//@Composable
//fun FilePickerIcon(onFileSelected: (Uri?) -> Unit, fileTypes: Array<String> = arrayOf("*/*"), navController: NavController) {
    //val context = LocalContext.current

    //// Register the launcher for opening a document picker
    //val openFileLauncher = rememberLauncherForActivityResult(
        //contract = ActivityResultContracts.OpenDocument(),
        //onResult = { uri: Uri? ->
            //uri?.let { selectedUri ->
                //// ex content://com.android.providers.media.documents/document/image%3A1000084819
                //// Persist access permission to the URI
                //val takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                //context.contentResolver.takePersistableUriPermission(selectedUri, takeFlags)

                //// Return the selected file URI to the caller
                //onFileSelected(selectedUri)
            //} ?: run {
                //onFileSelected(null)
            //}
        //}
    //)
    //// Trigger the file picker explicitly
    //LaunchedEffect(Unit) { openFileLauncher.launch(fileTypes) }
//}

//@Composable
//fun FilePickerMessage(onFileSelected: (Uri?) -> Unit, fileTypes: Array<String> = arrayOf("*/*"), navController: NavController, messageViewModel: MessageViewModel) {
    //val context = LocalContext.current
  ////  messageViewModel.setfilePicked(false)
   //// messageViewModel.setShowPicker(false)
    //// Register the launcher for opening a document picker
    //val openFileLauncher = rememberLauncherForActivityResult(
        //contract = ActivityResultContracts.OpenDocument(),
        //onResult = { uri: Uri? ->
           //if (uri == null){ messageViewModel.setShowPicker(false)}
            //uri?.let { selectedUri ->
                //// Persist access permission to the URI
                //val takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                //context.contentResolver.takePersistableUriPermission(selectedUri, takeFlags)
                //// Return the selected file URI to the caller
                //onFileSelected(selectedUri)
            //} ?: run {
                //onFileSelected(null)
            //}
        //}
    //)
    //// Trigger the file picker explicitly
    //LaunchedEffect(Unit) { openFileLauncher.launch(fileTypes) }
//}

//@Composable
//fun DirectoryPicker(onDirectorySelected: (Uri?) -> Unit) {
    //val context = LocalContext.current

    //// Register the launcher for opening document tree (directory picker)
    //val openDirectoryLauncher = rememberLauncherForActivityResult(
        //contract = ActivityResultContracts.OpenDocumentTree(),
        //onResult = { uri: Uri? ->
            //uri?.let { documentUri ->
                //// Persist access permission to the URI
                //val takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                //context.contentResolver.takePersistableUriPermission(documentUri, takeFlags)

                //// Return the selected directory URI to the caller
                //onDirectorySelected(documentUri)
            //}
        //}
    //)
    //// Open the directory picker when the composable is first displayed or triggered
    //LaunchedEffect(Unit) {
        //openDirectoryLauncher.launch(null)
    //}
//}