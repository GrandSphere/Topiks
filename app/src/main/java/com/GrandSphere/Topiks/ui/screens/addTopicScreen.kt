package com.GrandSphere.Topiks.ui.screens

// Moved to viewmodel

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.GrandSphere.Topiks.ui.components.addTopic.TopicCategory
import com.GrandSphere.Topiks.ui.components.addTopic.TopicColour
import com.GrandSphere.Topiks.ui.components.addTopic.TopicName
import com.GrandSphere.Topiks.ui.components.addTopic.argbToColor
import com.GrandSphere.Topiks.ui.focusClear
import com.GrandSphere.Topiks.ui.viewmodels.GlobalViewModelHolder
import com.GrandSphere.Topiks.ui.viewmodels.TopicViewModel

@Composable
fun AddTopicScreen(navController: NavController, viewModel: TopicViewModel, topicId: Int = -1) {
    val bEditMode: Boolean by viewModel.bEditMode.collectAsState()
    val topBarViewModel = GlobalViewModelHolder.getTopBarViewModel()
    LaunchedEffect(Unit) {
        topBarViewModel.setMenuItems( listOf() )

        if (bEditMode) {
            viewModel.initializeEditMode(topicId)
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
            .imePadding()
            .focusClear()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                TopicColour(navController, viewModel)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                TopicCategory(viewModel)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                TopicName(navController, viewModel, topicId)
            }
        }
    }
}