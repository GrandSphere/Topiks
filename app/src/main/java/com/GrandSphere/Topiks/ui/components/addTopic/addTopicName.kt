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


package com.GrandSphere.Topiks.ui.components.addTopic

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.GrandSphere.Topiks.ui.components.global.CustomTextBox
import com.GrandSphere.Topiks.ui.viewmodels.TopicViewModel

@Composable
fun TopicName(navController: NavController, viewModel: TopicViewModel, topicId: Int = -1) {
    val sPlaceHolder: String = "Topic Name..."
    val vFontSize: TextUnit = 18.sp
    val vButtonSize: Dp = 30.dp
    val vIconSize: Dp = 30.dp
    val vMaxLinesSize: Dp = 80.dp
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }
    val tempTopicName by viewModel.temptopicname.collectAsState()
    val bEditedMode by viewModel.bEditedMode.collectAsState()
    val colours = MaterialTheme.colorScheme

    LaunchedEffect(bEditedMode) {
        if (bEditedMode) {
            viewModel.loadIconPathForEdit(topicId)
        }
    }

    val focusModifier = Modifier
        .focusRequester(focusRequester)
        .onFocusChanged { focusState ->
            isFocused = focusState.isFocused
        }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 3.dp, start = 5.dp, end = 0.dp, bottom = 4.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        CustomTextBox(
            inputText = tempTopicName,
            onValueChange = { newText ->
                viewModel.setTempTopicName(newText)
            },
            vMaxLinesSize = vMaxLinesSize,
            vFontSize = vFontSize,
            sPlaceHolder = sPlaceHolder,
            isFocused = isFocused,
            focusModifier = focusModifier,
            boxModifier = Modifier
                .weight(1f)
                .padding(bottom = 5.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton( // Close button
            onClick = {
                viewModel.clearViewModelState()
                navController.popBackStack()
            },
            modifier = Modifier.size(vButtonSize)
                .align(Alignment.Bottom)
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Cancel",
                tint = colours.onBackground,
                modifier = Modifier.size(vIconSize)
            )
        }
        Spacer(modifier = Modifier.width(5.dp))
        IconButton( // Confirm button
            onClick = {
                viewModel.confirmTopic(topicId, tempTopicName)
                navController.popBackStack()
            },
            modifier = Modifier
                .size(vButtonSize)
                .align(Alignment.Bottom)
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = "Confirm",
                tint = colours.onBackground,
                modifier = Modifier.size(vIconSize)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    BackHandler {
        viewModel.clearViewModelState()
        navController.popBackStack()
    }
}