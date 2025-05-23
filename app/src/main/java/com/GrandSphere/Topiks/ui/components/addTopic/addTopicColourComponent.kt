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

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.GrandSphere.Topiks.ui.viewmodels.TopicViewModel

@Composable
fun TopicColour(navController: NavController, viewModel: TopicViewModel ) {
    val noteColour by viewModel.colour.collectAsState()

    val colours = MaterialTheme.colorScheme
    var imageUrl = viewModel.fileURI.collectAsState().value

    val imageMimeTypes = arrayOf(
        "image/jpeg", "image/png", "image/gif", "image/bmp", "image/webp" )

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        viewModel.setFileURI(uri?.toString() ?: "")
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box( modifier = Modifier.size(60.dp)){}
        Spacer(modifier = Modifier.width(16.dp))
        Box(
            modifier = Modifier
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        viewModel.launchFilePicker(filePickerLauncher, imageMimeTypes)
                    })
                }
                .clip(CircleShape)
                .background(colours.secondary)
                .size(60.dp),
                 contentAlignment = Alignment.Center
        ) {

            if (imageUrl.length > 4) {
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = "Circular Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            } else {  // Show an icon as a fallback if no image URL is provided
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add Image",
                    tint = colours.onBackground,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(24.dp)
                        .padding(0.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Box(
            modifier = Modifier .size(60.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            IconButton(
                onClick = {
                    viewModel.setTempColour(noteColour)
                    navController.navigate("navcolourpicker")
                },
                modifier = Modifier.size(40.dp),

            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(noteColour)
                        .semantics { contentDescription =  "Colour Picker" }
                )
            }
        }
    }
}