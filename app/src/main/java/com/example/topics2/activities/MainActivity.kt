package com.example.topics2.activities
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.topics2.db.AppDatabase
import com.example.topics2.ui.components.CustomTopAppBar
import com.example.topics2.ui.screens.AddTopicScreen
import com.example.topics2.ui.screens.ColourPickerScreen
import com.example.topics2.ui.screens.TopicListScreen
import com.example.topics2.ui.themes.TopicsTheme
import com.example.topics2.ui.viewmodels.TopBarViewModel
import com.example.topics2.ui.viewmodels.TopicViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { TopicsTheme { TopicsApp(applicationContext) } }
    }
}

@Composable
fun TopicsApp(context: Context) {
    val database = AppDatabase.getDatabase(context)
    val topicViewModel: TopicViewModel = viewModel( factory = TopicViewModel.Factory )
    val topBarViewModel: TopBarViewModel = viewModel()
    val navController = rememberNavController()
    val topBarTitle by topBarViewModel.topBarTitle.collectAsState()
    val backStackEntry = navController.currentBackStackEntryAsState()

    // Listen for changes in the navController's back stack and update the title accordingly
    LaunchedEffect(backStackEntry.value) {
        val currentRoute = navController.currentBackStackEntry?.destination?.route
        topBarViewModel.updateTopBarTitle(currentRoute, navController.currentBackStackEntry)
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CustomTopAppBar(
                title = topBarTitle,
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
                    composable("navtopicListScreen") { TopicListScreen( navController, topicViewModel ) }
                    composable("navaddtopic") { AddTopicScreen( navController, topicViewModel ) }
                    composable("navcolourpicker") { ColourPickerScreen( navController, topicViewModel ) }
                }
            }
        }
    )


}
