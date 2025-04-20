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

package com.GrandSphere.Topiks.utilities.helper

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withStyle

 /**
 * Creates an AnnotatedString with highlighted search query matches in the message content.
 *
 * This function splits the message content into words and highlights substrings that match the search query
 * (case-insensitive). Non-matching text is styled with the provided `topicFontColor`, while matching text
 * is styled with the specified highlight background and font colors. Words are separated by spaces in the output.
 *
 * @param messageContent The content of the message to format.
 * @param searchQuery The search query to highlight in the message content.
 * @param topicColor The color used as the default foreground for highlighted text.
 * @param topicFontColor The color used for non-highlighted text.
 * @param highlightBackgroundColor The background color for highlighted text. Defaults to `topicFontColor`.
 * @param highlightFontColor The foreground color for highlighted text. Defaults to `topicColor`.
 * @return An AnnotatedString with highlighted search matches, or a plain AnnotatedString if the query is empty.
 */
fun highlightSearchText(
    messageContent: String,
    searchQuery: String,
    topicColor: Color,
    topicFontColor: Color,
    highlightBackgroundColor: Color = topicFontColor,
    highlightFontColor: Color = topicColor
): AnnotatedString {
     Log.d("QQWWEE", "INSIDE")
    val normalizedQuery = searchQuery.split(" ").map { it.trim() }.filter { it.isNotEmpty() }
    if (normalizedQuery.isEmpty()) {
        return AnnotatedString(messageContent)
    }
    return AnnotatedString.Builder().apply {
        val contentWords = messageContent.split(" ")
        contentWords.forEach { word ->
            var currentIndex = 0
            normalizedQuery.forEach { substring ->
                while (true) {
                    val matchIndex = word.indexOf(substring, currentIndex, ignoreCase = true)
                    if (matchIndex == -1) break
                    // Before matched substring
                    withStyle(style = SpanStyle(color = topicFontColor)) {
                        append(word.substring(currentIndex, matchIndex))
                    }
                    // Matched substring
                    withStyle(style = SpanStyle(background = highlightBackgroundColor, color = highlightFontColor)) {
                        append(word.substring(matchIndex, matchIndex + substring.length))
                    }
                    currentIndex = matchIndex + substring.length
                }
            }
            if (currentIndex < word.length) {
                // After matched substring
                withStyle(style = SpanStyle(color = topicFontColor)) {
                    append(word.substring(currentIndex))
                }
            }
            append(" ")
        }
    }.toAnnotatedString()
}