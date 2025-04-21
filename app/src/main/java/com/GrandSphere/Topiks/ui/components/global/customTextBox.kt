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

package com.GrandSphere.Topiks.ui.components.global

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomTextBox(
    inputText: String,
    onValueChange: (String) -> Unit,
    vMaxLinesSize: Dp = 100.dp,
    vFontSize: TextUnit = 16.sp,
    sPlaceHolder: String = "Enter message",
    isFocused: Boolean = false,
    focusModifier: Modifier = Modifier,
    onFocus: () -> Unit = {},
    boxModifier: Modifier = Modifier // Modifier for the Box
) {
    val colors = MaterialTheme.colorScheme
    Box(
        modifier = boxModifier // Apply the modifier passed for the Box
    ) {
        BasicTextField(
            value = inputText,
            onValueChange = onValueChange,
            modifier = focusModifier
                .fillMaxWidth()
                .padding(start = 3.dp, top = 3.dp)
            ,
            textStyle = TextStyle(
                fontSize = vFontSize,
                color = colors.onBackground,
                lineHeight = 20.sp,

            ),
//                            maxLines = 6,

            cursorBrush = if (isFocused) SolidColor(colors.onPrimary) else SolidColor(Color.Transparent), // Hide cursor if not focused
            decorationBox = @Composable { innerTextField ->
                if (inputText.isEmpty()) {
                    Text(
                        text = sPlaceHolder,
                        style = TextStyle(
                            color = colors.secondary, // Placeholder color
                            fontSize = vFontSize,
                            lineHeight = 20.sp
                        ),
                        modifier = Modifier.padding(start = 0.dp, top = 0.dp)
                    )
                }
                innerTextField()
            }
        )
    }
}