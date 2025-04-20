package com.GrandSphere.Topiks.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.GrandSphere.Topiks.ui.components.global.CustomTextBox
import com.GrandSphere.Topiks.ui.viewmodels.GlobalViewModelHolder
import com.GrandSphere.Topiks.ui.viewmodels.MessageViewModelContract
import kotlinx.coroutines.launch

@Composable
fun MessageViewScreen(navController: NavController, viewModel: MessageViewModelContract) {
    val tempMessageID: Int = viewModel.getTempMessageID()
    var inputText by remember { mutableStateOf( viewModel.getMessageContentById(tempMessageID))}
    val coroutineScope = rememberCoroutineScope()

    val topBarViewModel = GlobalViewModelHolder.getTopBarViewModel()
    LaunchedEffect(Unit) {
        topBarViewModel.setMenuItems(
            listOf(
            )
        )
    }
    Box(){
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)) {
            CustomTextBox(
                onValueChange = { newtext -> inputText = newtext },
                inputText = inputText ?: "",
                sPlaceHolder = "",
                boxModifier = Modifier.fillMaxSize(),
                focusModifier = Modifier.fillMaxSize()
            )

        }
        FloatingActionButton(
            onClick = {
                coroutineScope.launch {
                    viewModel.editMessageOnly(
                        messageId = tempMessageID,
                        topicId = viewModel.topicId.value,
                        content = inputText ?: "",
                        priority = 1,
                        categoryId = 1,
                        type = 1
                    )
                }
                navController.popBackStack()
            },
            shape = CircleShape, // Change the shape to rounded corners
            modifier = Modifier
                .align(Alignment.BottomEnd)
                //.align(Alignment.BottomEnd) // Align it to bottom end of the Box
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Add Topic",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}