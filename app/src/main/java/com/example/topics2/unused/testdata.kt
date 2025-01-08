package com.example.topics2.unused

fun getTestImagePaths(): List<String> {
    // Example paths (you would replace these with actual file paths)
    return listOf(
        "content://com.android.externalstorage.documents/document/primary%3ADocuments%2FtopicsContent%2F27.jpg",
        "content://com.android.externalstorage.documents/document/primary%3ADocuments%2FtopicsContent%2F22.jpg",
        "content://com.android.externalstorage.documents/document/primary%3ADocuments%2FtopicsContent%2F24.jpg",
        "content://com.android.externalstorage.documents/document/primary%3ADocuments%2FtopicsContent%2F28.jpg",
        "content://com.android.externalstorage.documents/document/primary%3ADocuments%2FtopicsContent%2F23.jpg",
        "content://com.android.externalstorage.documents/document/primary%3ADocuments%2FtopicsContent%2F20.png",
        "content://com.android.externalstorage.documents/document/primary%3ADocuments%2FtopicsContent%2F26.png",
        "content://com.android.externalstorage.documents/document/primary%3ADocuments%2FtopicsContent%2F21.png",
        "content://com.android.externalstorage.documents/document/primary%3ADocuments%2FtopicsContent%2F25.png",
    )
}

// Sample Message Function
fun getSampleChatMessage(): String {
    return """
        This is a long sample chat message designed for debugging purposes.
        It has multiple lines of text to test overflow functionality in the chat bubble.
        
        Here's another paragraph to simulate a real-world chat scenario.
        This message should include enough content to exceed the maximum height
        or number of lines allowed in the chat bubble.
        
        Line 1: Testing line wrapping in Jetpack Compose.
        Line 2: Checking if the 'Show More' button is displayed as expected.
        Line 3: Ensuring that the UI updates when expanding or collapsing the chat bubble.
        Line 4: Adding even more lines to push the limits of the chat bubble size.
        
        Paragraph 2: Lorem ipsum dolor sit amet, consectetur adipiscing elit.
        Donec vehicula ullamcorper mi a tempor. Praesent at libero ornare, 
        facilisis sem sed, elementum nunc. Proin tincidunt posuere odio, 
        vitae malesuada nisl bibendum vel.
        
        Extra lines for debugging:
        - Line A
        - Line B
        - Line C
        
        End of the sample message. Happy debugging!
    """.trimIndent()
}