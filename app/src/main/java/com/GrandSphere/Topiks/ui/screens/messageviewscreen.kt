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

package com.GrandSphere.Topiks.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
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