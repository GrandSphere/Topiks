package com.example.topics.utilities

// TODO: Add to settings where user want app folder

//@Composable
//fun SelectFileWithPicker(navController: NavController, messageViewModel: MessageViewModel) {
//
//    var fileUri by remember { mutableStateOf<Uri?>(null) }
//    if (fileUri == null) { FilePickerMessage(
//        onFileSelected = {  selectedUri -> fileUri = selectedUri },
//        navController = navController, fileTypes = arrayOf("*/*"),
//        messageViewModel = messageViewModel
//    ) }
//
//    val context = LocalContext.current
//    // Without the LaunchEffect things will go bad. Do not remove, filepicker will re-open
//    LaunchedEffect(fileUri) {
//        messageViewModel.setfilePicked(false)
//        fileUri?.let { uri ->
//            Log.d("AABBCCSelectFileWithPicker2", "Selected URI: $uri")
//            messageViewModel.setURI(uri.toString())
//            messageViewModel.setfilePicked(true)
//            messageViewModel.setShowPicker(false)
//            fileUri = null
//            Toast.makeText(context, "File selected: $uri", Toast.LENGTH_SHORT).show()
//        }
//
//    }
//}
//
//fun copyFileToUserFolder(context: Context, messageViewModel: MessageViewModel) {
//    val currentUri = messageViewModel.fileURI.value // Assuming ViewModel exposes URI as LiveData or StateFlow
//    Log.d("AABBCCDD", "THIS IS THE URI BEFORE COPY: $currentUri")
//    if (currentUri.isNullOrBlank()) {
//        Toast.makeText(context, "No file selected to import.", Toast.LENGTH_SHORT).show()
//        return
//    }
//
//    val uri = Uri.parse(currentUri)
//    try {
//        // Use a public directory like Downloads
//        val externalDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "topics/files")
//        Log.d("AABBCCDD", "THIS IS DESTINATION FILE: $externalDir")
//        if (!externalDir.exists() && !externalDir.mkdirs()) {
//            throw IOException("Failed to create directory: $externalDir")
//        }
//
//        // Extract the file name from the URI
//        val fileName = getFileNameFromUri(context, uri)
//        messageViewModel.setfileName(uri.toString())
//
//        // Create the file in the accessible directory
//        val destinationFile = File(externalDir, fileName)
//
//        if (destinationFile.exists()) {
//            Toast.makeText(context, "File already exists in destination folder.", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        // Copy the file to the accessible folder
//        val inputStream = context.contentResolver.openInputStream(uri) ?: throw IOException("Unable to open input stream for URI: $uri")
//        val outputStream = destinationFile.outputStream()
//        copyStream(inputStream, outputStream) // Use existing function to copy file contents
//        inputStream.close()
//        outputStream.close()
//
//        // Update ViewModel URI path to the new location
//        messageViewModel.setURI(destinationFile.absolutePath)
//
//        Toast.makeText(context, "File imported successfully! You can find it in Documents/topics/files.", Toast.LENGTH_SHORT).show()
//        messageViewModel.setfilePicked(false)
//    } catch (e: IOException) {
//        messageViewModel.setfilePicked(false)
//        Toast.makeText(context, "Error importing file: ${e.message}", Toast.LENGTH_LONG).show()
//    }
//}
//
//
//