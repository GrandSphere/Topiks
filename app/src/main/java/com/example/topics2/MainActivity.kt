package com.example.topics2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController

// MainActivity.kt

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.example.topics2.ui.screens.GreetingScreen
import com.example.topics2.ui.screens.NameInputScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GreetingApp()
        }
    }
}

@Composable
fun GreetingApp() {
    val navController = rememberNavController()

    // Setting up the NavHost with two screens
    NavHost(navController = navController, startDestination = "name_input") {
        composable("name_input") { NameInputScreen(navController) }
        composable("greeting") { GreetingScreen(navController) }
    }
}
