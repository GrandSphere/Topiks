package com.GrandSphere.Topiks.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager

/**
 * Adds a tap gesture to clear focus from any focused element, such as a TextField.
 *
 * This modifier detects tap gestures on the composable it is applied to and clears the current
 * focus using the [LocalFocusManager]. It is useful for dismissing the keyboard or removing focus
 * from input fields when the user taps outside the focused element.
 *
 * Example usage:
 * ```
 * Box(
 *     modifier = Modifier
 *         .fillMaxSize()
 *         .focusClear()
 * ) {
 *     TextField(value = text, onValueChange = { text = it })
 * }
 * ```
 *
 * @return A [Modifier] that adds tap-to-clear-focus behavior to the composable.
 */
@Composable
fun Modifier.focusClear(): Modifier {
    val focusManager = LocalFocusManager.current
    return this.then(
        Modifier.pointerInput(Unit) {
            detectTapGestures(onTap = {
                focusManager.clearFocus()
            })
        }
    )
}
