package com.example.topics2.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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
import com.example.topics2.ui.viewmodels.GlobalViewModelHolder
import com.example.topics2.ui.viewmodels.TopicViewModel

@Composable
fun ColorGridScreen(navController: NavController, viewModel: TopicViewModel) {
    // Get screen width and calculate block size (5 blocks per row
    val myNonsenseColours = viewModel.recentColoursList.collectAsState().value

    val topBarViewModel = GlobalViewModelHolder.getTopBarViewModel()
    LaunchedEffect(Unit) {
        topBarViewModel.setMenuItems(
            listOf(
            )
        )
    }
    //val scrollState = rememberScrollState()

    Column(
   //     modifier = Modifier
   //         .fillMaxHeight()
   //         .verticalScroll(scrollState),
        //.fillMaxSize() // Make sure the Box fills the screen,,
        verticalArrangement = Arrangement.Top, // Align items to the top
        horizontalAlignment = Alignment.CenterHorizontally,
        //.verti
    ) {
        // LazyVerticalGrid for displaying colors
        colourPopulator(
            colours = getColorsWithMaterialColors(MaterialTheme.colorScheme),
            boxMod = Modifier,
            viewModel = viewModel,
            navController = navController,
        )

        Divider(
            modifier = Modifier
                .fillMaxWidth() // Make sure it spans the full width
                .padding(12.dp)
                .size(3.dp)
                .background(MaterialTheme.colorScheme.onBackground) // Set the line color
        )
        colourPopulator(
            colours = myNonsenseColours,
            boxMod = Modifier.weight(1f),
            viewModel = viewModel,
            navController = navController,
        )
        // IconButton placed at the bottom center of the screen
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                //.align(Alignment.BottomCenter) // Align the button at the bottom center
                .padding(16.dp) // Add padding around the button
                .size(30.dp) // Adjust the size of the button as needed
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
    val blockSize = screenWidth / iItemWidth // 5 blocks in a row
    LazyVerticalGrid(
        columns = GridCells.Fixed(iItemWidth), // 5 blocks per row
        boxMod.fillMaxWidth() // Ensure the grid takes up the full width
        //.weight(1f) // Take up the remaining space
        //.weight(1f)
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
                color = colours[index], // Set the background color
                shape = CircleShape // Circle shape for the surface
            ) {
                // No content inside, just the color and clickable area
            }
        }
    }

}

// Helper function to prepend all colors from MaterialTheme's ColorScheme
fun getColorsWithMaterialColors(colorScheme: ColorScheme): List<Color> {
    // Prepend all colors from the provided ColorScheme
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