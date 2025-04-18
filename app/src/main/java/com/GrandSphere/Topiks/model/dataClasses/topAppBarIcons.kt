package com.GrandSphere.Topiks.model.dataClasses

import androidx.compose.ui.graphics.vector.ImageVector

data class CustomIcon(
    val icon: ImageVector,               // Icon image vector
    val onClick: () -> Unit,             // Action onClick for the icon
    val contentDescription: String      // Content description for accessibility
)