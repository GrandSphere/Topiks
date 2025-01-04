package com.example.topics2.activities

// MainActivity.kt

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.topics2.db.AppDatabase
import com.example.topics2.ui.components.CustomTopAppBar
import com.example.topics2.ui.screens.AddTopicScreen
import com.example.topics2.ui.screens.ColourPickerScreen
import com.example.topics2.ui.screens.TopicListScreen
import com.example.topics2.ui.themes.TopicsTheme
import com.example.topics2.ui.viewmodels.TopicViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            TopicsTheme {
                TopicsApp(applicationContext)
            }
        }
    }
}

@Composable
fun TopicsApp(context: Context) {
    val database = AppDatabase.getDatabase(context)
   // val topicDao = database.topicDao()
 //   val topicViewModel = TopicViewModel(topicDao)
    //val topicViewModel: TopicViewModel = viewModel(factory = TopicViewModel.Factory)

    val topicViewModel: TopicViewModel = viewModel(
       factory = TopicViewModel.Factory
   )

    val navController = rememberNavController()


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CustomTopAppBar(
                title = "topBarBarTitle",
                onSettingsClick = { /* Handle settings click here */ },
                reloadTopics = {//topicController.loadTopics()
                }
            )
        },

        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding()) // Respect the top bar space
            ) {
                // Setting up the NavHost with two screens
                NavHost(navController = navController, startDestination = "navtopicListScreen") {
                    composable("navtopicListScreen") {
                        TopicListScreen( navController,
                            topicViewModel
                        )
                    }

                    composable("navaddtopic") {
                        AddTopicScreen(
                            navController,
                            topicViewModel
                        )
                    }

                    composable("navcolourpicker") {
                        ColourPickerScreen(
                            navController,
                            topicViewModel
                        )
                    }
                }
            }
        }
    )


}
