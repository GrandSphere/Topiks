package com.example.topics2.unused

import kotlin.random.Random

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


fun generateMyList(numberOfEntries: Int): List<String> {
    // Define vocabulary categories
    val adjectives = listOf("beautiful", "fast", "slow", "ancient", "bright", "dark", "tall", "small", "grumpy", "peaceful", "shiny", "dirty", "quiet", "loud", "colorful")
    val nouns = listOf("cat", "dog", "bird", "tree", "car", "mountain", "river", "city", "forest", "lake", "house", "sky", "sun", "moon", "star")
    val verbs = listOf("runs", "flies", "sits", "jumps", "swims", "walks", "climbs", "dives", "sings", "barks", "howls", "shines", "builds", "paints", "explores")
    val adverbs = listOf("quickly", "loudly", "gracefully", "happily", "silently", "randomly", "brightly", "gently", "strongly", "awkwardly", "suddenly", "playfully")

    // Function to generate a random sentence
    fun generateRandomSentence(): String {
        // Choose a random structure for the sentence
        val structure = Random.nextInt(1, 4)

        return when (structure) {
            1 -> {
                // Adjective + Noun + Verb + Adverb
                "${adjectives.random()} ${nouns.random()} ${verbs.random()} ${adverbs.random()}"
            }
            2 -> {
                // Noun + Verb + Noun + Verb
                "${nouns.random()} ${verbs.random()} ${nouns.random()} ${verbs.random()}"
            }
            3 -> {
                // Adjective + Noun + Verb
                "${adjectives.random()} ${nouns.random()} ${verbs.random()}"
            }
            else -> {
                // Fallback to a simple combination of a verb and two nouns
                "${verbs.random()} ${nouns.random()} ${nouns.random()}"
            }
        }
    }

    // Generate the list of sentences
    return (1..numberOfEntries).map { generateRandomSentence() }
}