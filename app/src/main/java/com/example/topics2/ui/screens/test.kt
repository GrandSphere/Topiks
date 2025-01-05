package com.example.topics2.ui.screens

import android.content.Context
import android.util.Log
import com.example.topics2.db.AppDatabase
import com.example.topics2.db.enitities.TopicTbl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun insertTestData(context: Context) {
    // Create some test data
    val topics = listOf(
        TopicTbl(
            topicName = "Technology",
            topicLastEdit = System.currentTimeMillis(),
            topicCreated = System.currentTimeMillis(),
            topicColour = 123,
            topicCategory = "Tech",
            topicIcon = "ic_tech",
            topicPriority = 1
        ),
        TopicTbl(
            topicName = "Science",
            topicLastEdit = System.currentTimeMillis(),
            topicCreated = System.currentTimeMillis(),
            topicColour = 321,
            topicCategory = "Science",
            topicIcon = "ic_science",
            topicPriority = 2
        ),
        TopicTbl(
            topicName = "Art",
            topicLastEdit = System.currentTimeMillis(),
            topicCreated = System.currentTimeMillis(),
            topicColour = 123,
            topicCategory = "Art",
            topicIcon = "ic_art",
            topicPriority = 3
        )
    )

    // Launching a coroutine to insert the data into the database
    CoroutineScope(Dispatchers.IO).launch {

        Log.d("aabbcc","before val database")
        val database = AppDatabase.getDatabase(context) // Get the AppDatabase instance
        Log.d("aabbcc","after val database")
        val topicDao = database.topicDao() // Get the TopicDao

        Log.d("aabbcc","after topicdao")
        // Insert the test data into the database
        for (topic in topics) {
            topicDao.insertTopic(topic)
        }

        // Switch back to the main thread if needed
        withContext(Dispatchers.Main) {
            // Do something on the main thread (e.g., show a message or update UI)
            // For now, let's just print a log
            println("Test data inserted successfully!")
        }
    }
    Log.d("aabbcc","we got to the end")
}