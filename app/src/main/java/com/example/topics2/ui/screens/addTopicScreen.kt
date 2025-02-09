package com.example.topics2.ui.screens

import android.util.Log
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.topics2.db.enitities.TopicTbl
import com.example.topics2.ui.components.addTopic.TopicCategory
import com.example.topics2.ui.components.addTopic.TopicColour
import com.example.topics2.ui.components.addTopic.TopicName
import com.example.topics2.ui.components.addTopic.argbToColor
import com.example.topics2.ui.viewmodels.GlobalViewModelHolder
import com.example.topics2.ui.viewmodels.TopicViewModel

@Composable
fun AddTopicScreen(navController: NavController, viewModel: TopicViewModel, topicId: Int = -1) {
    val focusManager = LocalFocusManager.current

    val bEditMode: Boolean by viewModel.bEditMode.collectAsState()
    val topicObj: MutableState<TopicTbl?> = remember { mutableStateOf(null) }
    var bEditedMode = remember { mutableStateOf(false) }
    val topBarViewModel = GlobalViewModelHolder.getTopBarViewModel()
    LaunchedEffect(Unit) {
        topBarViewModel.setMenuItems(
            listOf(
            )
        )
    }
    LaunchedEffect (bEditMode) {
        if (bEditMode) {
            bEditedMode.value = true
            topicObj.value = viewModel.getTopicObjectById(topicId)
            val topicName = topicObj.value?.name ?: ""
            val fileUri = topicObj.value?.iconPath ?: ""
            val colour = topicObj.value?.colour ?: 11111111
            Log.d("TOPICEDITING", "THIS IS THE name: ${topicName}")

            viewModel.setTempTopicName(topicName)

            viewModel.setFileURI(fileUri)

            viewModel.setTempColour(argbToColor(colour))
            viewModel.setColour(argbToColor(colour))

            //Log.d("TOPICEDITING print", "STILL ${bEditMode}")
            viewModel.setEditMode(false)
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
            .imePadding()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus() // Clear focus when tapping outside
                })
            }
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
                TopicName(navController, viewModel, bEditedMode.value, topicId)
                //onAddTopic = onAddTopic,
                //onCancel = onCancel
            }
        }
    }
}

