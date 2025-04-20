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

package com.GrandSphere.Topiks.ui

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
