package com.GrandSphere.Topiks.ui.components.global

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun CustomButton(
    buttonModifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    imageVector: ImageVector,
    contentDescription: String,
    onLongPress: () -> Unit = {},
    onClick:() -> Unit = {},
    tint: Color = Color.Unspecified,
){
    Box(
        modifier = buttonModifier
//            .background(Color.Red)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        onLongPress()
                    },
                    onTap = {
                        onClick()
                    },
                )
            },
        contentAlignment = Alignment.Center

    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = tint,
            modifier = iconModifier
        )
    }
}
