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
import com.example.topics2.unused.old.MyScreen
import com.example.topics2.unused.old.PreviewChatBubble
import com.example.topics2.unused.old.PreviewOverflowingLayout
import com.example.topics2.unused.old.UniqueFuzzySearchScreen
import com.example.topics2.unused.old.fTestSearchScreen
import com.example.topics2.unused.old.generateTableData
import com.example.topics2.unused.old.testScreen


class TestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { TopicsTheme { TestApp(applicationContext) } }
    }
}

//val Purple200 = Color(0xFFBB86FC) FIX THIS
@Composable
fun TestApp(context: Context) {
}