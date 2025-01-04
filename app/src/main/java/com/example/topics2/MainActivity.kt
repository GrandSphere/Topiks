package com.example.topics2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController

// MainActivity.kt

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.example.topics2.ui.screens.GreetingScreen
import com.example.topics2.ui.screens.TopicListScreen
import com.example.topics2.ui.themes.TopicsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            TopicsTheme {
                //insertTestData(applicationContext)
                TopicsApp()
            }
        }
    }
}

@Composable
fun TopicsApp() {
    val navController = rememberNavController()

    // Setting up the NavHost with two screens
    NavHost(navController = navController, startDestination = "navtopicListScreen") {
        composable("navtopicListScreen") { TopicListScreen(navController) }
        composable("greeting") { GreetingScreen(navController) }
    }
}
