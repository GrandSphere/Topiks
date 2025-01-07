package com.example.topics.utilities

//@Composable
//fun SelectImageWithPicker(topicViewModel: TopicViewModel, navController: NavController) {
//    var fileUri by remember { mutableStateOf<Uri?>(null) }
//
//    if (fileUri == null) {
//        FilePickerIcon(onFileSelected = { selectedUri ->
//            fileUri = selectedUri
//        }, fileTypes = arrayOf(
//                "image/jpeg",
//                "image/png",
//                "image/bmp",
//                "image/gif",
//                "image/webp",
//                "image/tiff",
//                "image/x-icon",
//                "image/heif",
//                "image/heic",
//            ),
//            navController = navController
//        )
//    }
//
//    val context = LocalContext.current
//    LaunchedEffect(fileUri) {
//        fileUri?.let { uri ->
//            // Set the URI path from the original location to the ViewModel
//            topicViewModel.setURI(uri.toString())
//            topicViewModel.setShowPicker( false )
//            fileUri = null // Reset state after selection
//            Toast.makeText(context, "File selected: $uri", Toast.LENGTH_SHORT).show()
//
//        }
//    }
//}
//
//fun copyIconToAppFolder(context: Context, topicViewModel: TopicViewModel) {
//    val currentUri = topicViewModel.fileURI.value // Assuming ViewModel exposes URI as LiveData or StateFlow
//    Log.d("AABBCCDD", "THIS IS THE URI BEFORE COPY: $currentUri")
//    if (currentUri.isNullOrBlank()) {
//        Toast.makeText(context, "No file selected to import.", Toast.LENGTH_SHORT).show()
//        return
//    }
//
//    val uri = Uri.parse(currentUri)
//    try {
//        val iconsDir = File(context.filesDir, "icons")
//        if (!iconsDir.exists()) iconsDir.mkdirs()
//
//        // Extract the file name from the URI
//        val imageName = getFileNameFromUri(context, uri)
//        Log.d("ZZZ IMAGE NAME", imageName)
//
//        // Create the URI for the new location of the compressed image
//        val compressedUri = Uri.fromFile(File(iconsDir, imageName))
//
//        // Compress and copy the image to the app folder
//        val success = compressImageToUri(context, uri, compressedUri)
//
//        if (success) {
//            // Update ViewModel URI path to the app folder path
//            topicViewModel.setURI(compressedUri.path?:"")
//
//            Toast.makeText(context, "Image imported and compressed successfully!", Toast.LENGTH_SHORT).show()
//        } else {
//            Toast.makeText(context, "Error compressing and saving the image.", Toast.LENGTH_SHORT).show()
//        }
//    } catch (e: IOException) {
//        Toast.makeText(context, "Error importing image: ${e.message}", Toast.LENGTH_LONG).show()
//    }
//}
//
//
//
//