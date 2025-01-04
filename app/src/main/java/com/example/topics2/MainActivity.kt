package com.example.topics2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.topics2.ui.themes.TopicsTheme


class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // Set the content view for the MainActivity using Jetpack Compose
        setContent {
            TopicsTheme {
                // The main composable content

            }
        }
    }
}
