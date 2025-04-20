/*******************************************************************************
 * Copyright (C) <2025> GrandSphere <GrandSphereStudios@protonmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.GrandSphere.Topiks.activities

import android.content.Context
import android.content.res.Configuration
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.GrandSphere.Topiks.db.AppDatabase
import com.GrandSphere.Topiks.ui.components.CustomTopAppBar
import com.GrandSphere.Topiks.ui.screens.AboutScreen
import com.GrandSphere.Topiks.ui.screens.AddTopicScreen
import com.GrandSphere.Topiks.ui.screens.ColorGridScreen
import com.GrandSphere.Topiks.ui.screens.ColourPickerScreen
import com.GrandSphere.Topiks.ui.screens.MessageScreen
import com.GrandSphere.Topiks.ui.screens.MessageViewScreen
import com.GrandSphere.Topiks.ui.screens.ShowMorePictures
import com.GrandSphere.Topiks.ui.screens.TopicListScreen
import com.GrandSphere.Topiks.ui.screens.allSearch
import com.GrandSphere.Topiks.ui.themes.TopiksTheme
import com.GrandSphere.Topiks.ui.viewmodels.CategoryViewModel
import com.GrandSphere.Topiks.ui.viewmodels.GlobalViewModelHolder
import com.GrandSphere.Topiks.ui.viewmodels.MessageViewModelContract
import com.GrandSphere.Topiks.ui.viewmodels.TopBarViewModel
import com.GrandSphere.Topiks.ui.viewmodels.TopicViewModel
import com.GrandSphere.Topiks.ui.viewmodels.messageViewmodelRepos.ExportRepositoryImpl
import com.GrandSphere.Topiks.ui.viewmodels.messageViewmodelRepos.FileRepositoryImpl
import com.GrandSphere.Topiks.ui.viewmodels.messageViewmodelRepos.MessageRepositoryImpl
import com.GrandSphere.Topiks.ui.viewmodels.messageViewmodelRepos.MessageViewModelImpl
import com.GrandSphere.Topiks.ui.viewmodels.messageViewmodelRepos.SearchRepositoryImpl
import com.GrandSphere.Topiks.ui.viewmodels.searchViewModel
import com.GrandSphere.Topiks.viewmodel.SettingsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val settingsViewModel: SettingsViewModel = viewModel()
            var iTheme = settingsViewModel.getTheme()

            if (iTheme == 2) {
                val uiMode = applicationContext.resources.configuration.uiMode
                val bDarkMode =
                    (uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
                iTheme = if (bDarkMode) 0 else 1
            }
            TopiksTheme(iTheme) { TopiksApp(applicationContext) }
        }
    }
}

@Composable
fun TopiksApp(context: Context) {
    val database = AppDatabase.getDatabase(context)
    val topicViewModel: TopicViewModel = viewModel(factory = TopicViewModel.Factory)

    // Manually instantiate MessageViewModelImpl
    val messageRepository = MessageRepositoryImpl(database.messageDao(), database.topicDao())
    val fileRepository = FileRepositoryImpl(database.fileDao())
    val searchRepository = SearchRepositoryImpl()
    val exportRepository = ExportRepositoryImpl()

    // Use viewModel() with custom factory
    val messageViewModel: MessageViewModelContract = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val messageRepository = MessageRepositoryImpl(database.messageDao(), database.topicDao())
                val fileRepository = FileRepositoryImpl(database.fileDao())
                val searchRepository = SearchRepositoryImpl()
                val exportRepository = ExportRepositoryImpl()

                return MessageViewModelImpl(
                    messageRepository = messageRepository,
                    fileRepository = fileRepository,
                    searchRepository = searchRepository,
                    exportRepository = exportRepository
                ) as T
            }
        }
    )

    val categoryViewModel: CategoryViewModel = viewModel(factory = CategoryViewModel.Factory)
    val settingsViewModel: SettingsViewModel = viewModel()
    val searchViewModel: searchViewModel = viewModel() // Note: SearchViewModel is unchanged
    val topBarViewModel = viewModel<TopBarViewModel>()
    GlobalViewModelHolder.setTopBarViewModel(topBarViewModel)
    val navController = rememberNavController()
    val topBarTitle by topBarViewModel.topBarTitle.collectAsState()
    val backStackEntry = navController.currentBackStackEntryAsState()

    // Add test category
    LaunchedEffect(true) {
        categoryViewModel.addtestcat()
        // databaseSeeder.generateSampleData(categoryId = 1)
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
                // topicViewModel = topicViewModel // Commented out as per original code
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
                    startDestination = "navtopicListScreen"
                ) {
                    composable("navtopicListScreen") {
                        TopicListScreen(
                            navController,
                            topicViewModel
                        )
                    }
                    composable("newSearch") {
                        allSearch(messageViewModel, searchViewModel, navController)
                    }
                    composable("navViewMessage") {
                        MessageViewScreen(navController, messageViewModel)
                    }
                    composable("navAboutScreen") {
                        AboutScreen()
                    }
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
                    composable("navShowMorePictures") {
                        ShowMorePictures(navController)
                    }
                    composable(
                        "navnotescreen/{topicId}/{topicName}/{messageId}",
                        arguments = listOf(
                            navArgument("topicId") { type = NavType.IntType },
                            navArgument("messageId") { type = NavType.IntType }
                        )
                    ) { backStackEntry ->
                        val topicId = backStackEntry.arguments?.getInt("topicId")
                        val messageId = backStackEntry.arguments?.getInt("messageId") ?: -1
                        if (topicId != -1) {
                            MessageScreen(
                                navController,
                                messageViewModel,
                                topicId ?: -1,
                                topicColor = topicViewModel.cTopicColor,
                                messageId = messageId
                            )
                        }
                    }
                }
            }
        }
    )
}