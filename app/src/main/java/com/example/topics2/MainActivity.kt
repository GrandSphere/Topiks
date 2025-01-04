package com.example.topics2

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController

// MainActivity.kt

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.example.topics2.db.AppDatabase
import com.example.topics2.ui.screens.GreetingScreen
import com.example.topics2.ui.screens.NameInputScreen
import com.example.topics2.ui.screens.TopicListScreen
import com.example.topics2.ui.screens.insertTestData
import com.example.topics2.ui.viewmodels.TopicViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Create the AppDatabase and TopicDao

        setContent {
          //  insertTestData(applicationContext)
            GreetingApp(applicationContext)
        }
    }
}

@Composable
fun GreetingApp(context: Context) {
    val database = AppDatabase.getDatabase(context)
    val topicDao = database.topicDao()
    val topicViewModel = TopicViewModel(topicDao)
    val navController = rememberNavController()

    // Setting up the NavHost with two screens
    NavHost(navController = navController, startDestination = "navtopicListScreen") {
        composable("navtopicListScreen") { TopicListScreen(navController, topicViewModel) }
        composable("greeting") { GreetingScreen(navController) }
    }
}
