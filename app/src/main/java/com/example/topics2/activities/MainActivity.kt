package com.example.topics2.activities

import android.content.Context
import android.os.Bundle
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.topics2.db.AppDatabase
import com.example.topics2.ui.components.CustomTopAppBar
import com.example.topics2.ui.screens.AddTopicScreen
import com.example.topics2.ui.screens.ColorGridScreen
import com.example.topics2.ui.screens.ColourPickerScreen
import com.example.topics2.ui.screens.MessageScreen
import com.example.topics2.ui.screens.MessageViewScreen
import com.example.topics2.ui.screens.ShowMorePictures
import com.example.topics2.ui.screens.TopicListScreen
import com.example.topics2.ui.screens.allSearch
import com.example.topics2.ui.themes.TopicsTheme
import com.example.topics2.ui.viewmodels.CategoryViewModel
import com.example.topics2.ui.viewmodels.GlobalViewModelHolder
import com.example.topics2.ui.viewmodels.MessageViewModel
import com.example.topics2.ui.viewmodels.TopBarViewModel
import com.example.topics2.ui.viewmodels.TopicViewModel
import com.example.topics2.ui.viewmodels.searchViewModel
import com.example.topics2.viewmodel.SettingsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent { TopicsTheme { TopicsApp(applicationContext) } }

    }

    //val Purple200 = Color(0xFFBB86FC) FIX THIS
    @Composable
    fun TopicsApp(context: Context) {
            val database = AppDatabase.getDatabase(context)
            val topicViewModel: TopicViewModel = viewModel(factory = TopicViewModel.Factory)

            val messageViewModel: MessageViewModel = viewModel(factory = MessageViewModel.Factory)
            val categoryViewModel: CategoryViewModel =
                viewModel(factory = CategoryViewModel.Factory)
            val settingsViewModel: SettingsViewModel = viewModel()
            val searchViewModel: searchViewModel = viewModel()
//            val topBarViewModel: TopBarViewModel = viewModel()

        val topBarViewModel = viewModel<TopBarViewModel>()
         GlobalViewModelHolder.setTopBarViewModel(topBarViewModel)
            val navController = rememberNavController()
            val topBarTitle by topBarViewModel.topBarTitle.collectAsState()
            val backStackEntry = navController.currentBackStackEntryAsState()

        //Add test category
        LaunchedEffect(true)
        {
             categoryViewModel.addtestcat()
            //databaseSeeder.generateSampleData(categoryId = 1)
        }
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
                    navController = navController,
//                    topicViewModel = topicViewModel
                )
            },

            content = { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = paddingValues.calculateTopPadding()) // Respect the top bar space
                ) {
                    NavHost(
                        navController = navController,
                        // startDestination = "navtopicListScreen"
                        startDestination = "navtopicListScreen"
                    ) {
                        composable("navtopicListScreen") {
                            TopicListScreen(
                                navController,
                                topicViewModel
                            )
                        }
                        composable("newSearch") { allSearch(messageViewModel, searchViewModel, navController ) }
                        composable("navViewMessage"){ MessageViewScreen(navController, messageViewModel) }
                        composable(
                            route = "navaddtopic/{topicId}",

                            arguments = listOf(
                                navArgument("topicId") { type = NavType.IntType },
                            )
                        ) { backStackEntry ->
                            val topicId = backStackEntry.arguments?.getInt("topicId")
                            AddTopicScreen(navController, topicViewModel, topicId ?: -1)
                        }
                        composable("navcolourpicker") {
                            ColourPickerScreen(
                                navController,
                                topicViewModel
                            )
                        }
                        composable("navrecentcolours") {

                            ColorGridScreen(
                                navController,
                                topicViewModel
                            )
                        }
                        composable("navShowMorePictures") { ShowMorePictures(navController) }

                        composable(
                            "navnotescreen/{topicId}/{topicName}/{messageId}",
                            //arguments = listOf(navArgument("topicId") { type = NavType.IntType })
                            arguments = listOf(
                                navArgument("topicId") { type = NavType.IntType },
                                navArgument("messageId") { type = NavType.IntType }
                            )
                        ) { backStackEntry ->
                            val topicId = backStackEntry.arguments?.getInt("topicId")
                            val messageId = backStackEntry.arguments?.getInt("messageId") ?: -1
                            if (topicId != -1) {
                                MessageScreen(
                                    navController, messageViewModel, topicId ?: -1,
                                    topicColor = topicViewModel.cTopicColor, messageId = messageId
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}