@file:Suppress("PreviewAnnotationInFunctionWithParameters")

package com.example.topics2.activities

//import T2RunApp


//import com.example.topics2.unused.T2RunApp
//import com.example.topics2.unused.T2SearchHandler
//import com.example.topics2.unused.T2RunApp

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
import com.example.topics2.ui.screens.ShowMorePictures
import com.example.topics2.ui.screens.TopicListScreen
import com.example.topics2.ui.themes.TopicsTheme
import com.example.topics2.ui.viewmodels.MessageViewModel
import com.example.topics2.ui.viewmodels.TopBarViewModel
import com.example.topics2.ui.viewmodels.TopicViewModel
import com.example.topics2.unused.MyScreen
import com.example.topics2.unused.NewSearchScreen
import com.example.topics2.unused.PreviewChatBubble
import com.example.topics2.unused.PreviewOverflowingLayout
import com.example.topics2.unused.UniqueFuzzySearchScreen
import com.example.topics2.unused.fTestSearchScreen
import com.example.topics2.unused.messageScreen.TestMessageScreen
import com.example.topics2.unused.testScreen


class TestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { TopicsTheme { TestApp(applicationContext) } }
    }
}

//val Purple200 = Color(0xFFBB86FC) FIX THIS
@Composable
fun TestApp(context: Context) {
    val database = AppDatabase.Companion.getDatabase(context)
    val topicViewModel: TopicViewModel = viewModel(factory = TopicViewModel.Companion.Factory)
    val messageViewModel: MessageViewModel = viewModel(factory = MessageViewModel.Companion.Factory)

    val topBarViewModel: TopBarViewModel = viewModel()
    val navController = rememberNavController()

    //val topBarTitle by topBarViewModel.topBarTitle.collectAsState()
    //val topBarTitle by topBarViewModel.topBarTitle.collectAsState()
    val backStackEntry = navController.currentBackStackEntryAsState()

    //messageViewModel.insertTestMessages()
    //  Log.d("aabbccd", colorToArgb(Color.Cyan).toString())

    // Listen for changes in the navController's back stack and update the title accordingly
    LaunchedEffect(backStackEntry.value) {
        val currentRoute = navController.currentBackStackEntry?.destination?.route
        topBarViewModel.updateTopBarTitle(currentRoute, navController.currentBackStackEntry)
    }
    Scaffold(
        modifier = Modifier.Companion.fillMaxSize(),
        topBar = {
            CustomTopAppBar(
                title = "Testing",
                onSettingsClick = { /* Handle settings click here */ },
                reloadTopics = {//topicController.loadTopics()
                },
                navController = navController,
            )
        },

        content = { paddingValues ->
            Box(
                modifier = Modifier.Companion
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding()) // Respect the top bar space
            ) {
                // Setting up the NavHost with two screens
                //NavHost(navController = navController, startDestination = "navtopicListScreen") {
                //NavHost(navController = navController, startDestination = "navnotescreen/1/abc") {
                NavHost(navController = navController, startDestination = "navDebugChat") {
                  //  composable("navcTest3") {T2RunApp()}



                    composable("navShowMorePictures") { ShowMorePictures(navController) }
                    composable("navDebugChat") {TestMessageScreen(navController, messageViewModel,3)}
//                    composable("navDebugChat") { MessageScreen(navController,messageViewModel,1)}
                    composable("navSearchTest") { fTestSearchScreen( navController) }
                    composable("navnewFuzzy") { UniqueFuzzySearchScreen () }
                    composable("newSearch") { NewSearchScreen () }
                    //composable("navFuzzy") { FuzzySearchScreen () }
                    composable("navTest4") { PreviewOverflowingLayout () }
                    composable("navTest2") { PreviewChatBubble () }
                    composable("navTest") { testScreen () }
                    composable("navState2") { ShowMorePictures (navController) }
                    composable("navTest") { testScreen() }
                   // composable("navMessageBubble") { MessageBubble(navController) }
                    composable("navmyscreen") { MyScreen() }
                    composable("navtopicListScreen") {
                        TopicListScreen(
                            navController,
                            topicViewModel
                        )
                    }
                    composable("navaddtopic") { AddTopicScreen(navController, topicViewModel) }
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
                    composable(
                        "navnotescreen/{topicId}/{topicName}",
                        arguments = listOf(navArgument("topicId") {
                            type = NavType.Companion.IntType
                        })
                    ) { backStackEntry ->
                        val topicId = backStackEntry.arguments?.getInt("topicId")
                        if (topicId != -1) {
                            MessageScreen(
                                // TODO: Check topicId!!
                                navController, messageViewModel, topicId ?: -1,
                                topicColor = topicViewModel.cTopicColor,
                            )
                        }

                    }
                }
            }
        }
    )
}