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

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.GrandSphere.Topiks.ui.viewmodels.GlobalViewModelHolder
import com.GrandSphere.Topiks.ui.viewmodels.TopicViewModel

@Composable
fun ColorGridScreen(navController: NavController, viewModel: TopicViewModel) {
    val myNonsenseColours = viewModel.recentColoursList.collectAsState().value

    val topBarViewModel = GlobalViewModelHolder.getTopBarViewModel()
    LaunchedEffect(Unit) {
        topBarViewModel.setMenuItems(
            listOf(
            )
        )
    }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        colourPopulator(
            colours = getColorsWithMaterialColors(MaterialTheme.colorScheme),
            boxMod = Modifier,
            viewModel = viewModel,
            navController = navController,
        )

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .size(3.dp)
                .background(MaterialTheme.colorScheme.onBackground)
        )
        colourPopulator(
            colours = myNonsenseColours,
            boxMod = Modifier.weight(1f),
            viewModel = viewModel,
            navController = navController,
        )

        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .padding(16.dp)
                .size(30.dp)
        ) {
            Icon(
                modifier = Modifier.size(30.dp),
                imageVector = Icons.Filled.Close,
                contentDescription = "Cancel",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
fun colourPopulator(
    colours: List<Color>,
    iItemWidth: Int = 7,
    boxMod : Modifier,
    viewModel: TopicViewModel,
    navController: NavController,
)
{
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val blockSize = screenWidth / iItemWidth // Modify to account for large screens
    LazyVerticalGrid(
        columns = GridCells.Fixed(iItemWidth),
        boxMod.fillMaxWidth()
    ) {
        items(colours.size) { index ->
            Surface(
                modifier = Modifier.size(blockSize)
                    .padding(4.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, MaterialTheme.colorScheme.onPrimary,CircleShape)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                viewModel.setTempColour(colours[index])
                                navController.popBackStack()
                            }

                        )
                    },
                color = colours[index],
                shape = CircleShape
            ) {
            }
        }
    }

}

// Helper function to prepend all colors from MaterialTheme's ColorScheme
fun getColorsWithMaterialColors(colorScheme: ColorScheme): List<Color> {
    return listOf <Color>(
        colorScheme.primary,
        colorScheme.onPrimary,
        colorScheme.secondary,
        colorScheme.onSecondary,
        colorScheme.background,
        colorScheme.onBackground,
        colorScheme.surface,
        colorScheme.onSurface,
        colorScheme.error,
        colorScheme.onError,
        colorScheme.outline,
        colorScheme.inversePrimary,
        colorScheme.scrim,
        colorScheme.surfaceVariant,
        colorScheme.onSurfaceVariant,
        colorScheme.tertiary,
        colorScheme.onTertiary,
        colorScheme.tertiaryContainer,
        colorScheme.onTertiaryContainer
    )
}