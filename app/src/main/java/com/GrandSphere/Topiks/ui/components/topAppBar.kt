package com.GrandSphere.Topiks.ui.components
// Moved to viewmodel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavController
import com.GrandSphere.Topiks.ui.viewmodels.GlobalViewModelHolder
import com.GrandSphere.Topiks.ui.viewmodels.TopBarViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    title: String,
    navController: NavController,
) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    val colours = MaterialTheme.colorScheme

    val topBarViewModel = GlobalViewModelHolder.getTopBarViewModel()
    val customIcons by topBarViewModel.customIcons.collectAsState()
    TopAppBar(
        modifier = Modifier.height(45.dp),
        title = {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(vertical = 0.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(text = title)
            }
        },
        actions = {
            LazyRow(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(customIcons.size)
                {
                        index ->
                    IconButton(
                        onClick = { customIcons[index].onClick() }
                    ) {
                        Icon(
                            imageVector = customIcons[index].icon,
                            contentDescription = customIcons[index].contentDescription,
                            tint = colours.onBackground
                        )
                    }
                }
            }
            IconButton(
                onClick = { isMenuExpanded = true }
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Settings",
                    tint = colours.onBackground
                )
            }
            CustomTopMenu(
                isMenuExpanded = isMenuExpanded,
                onDismiss = { isMenuExpanded = false },
                navController = navController,
                topBarViewModel = topBarViewModel,
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            titleContentColor = colours.onBackground,
            actionIconContentColor = colours.onBackground,
            containerColor = colours.background
        )
    )
}

@Composable
fun CustomTopMenu(
    isMenuExpanded: Boolean,
    onDismiss: () -> Unit,
    navController: NavController,
    topBarViewModel: TopBarViewModel
) {
    val colours = MaterialTheme.colorScheme
    val menuItems by topBarViewModel.menuItems.collectAsState()

    DropdownMenu(
        expanded = isMenuExpanded,
        onDismissRequest = { onDismiss() },
        properties = PopupProperties(focusable = true),
        modifier = Modifier
            .background(colours.surface)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(), // Ensure it always takes up full height
            contentAlignment = Alignment.TopStart // Keep items at the top
        ) {
            Column {
                menuItems.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item.label, color = colours.onSurface) },
                        onClick = {
                            item.onClick()
                            onDismiss()
                        }
                    )
                }
            }
        }
    }
}
