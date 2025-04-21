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

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.GrandSphere.Topiks.R
import com.GrandSphere.Topiks.Version


@Composable
fun AboutScreen() {
    val uriHandler= LocalUriHandler.current
    val githubUrl = "https://github.com/GrandSphere/Topiks"
    val licenseUrl = "https://github.com/GrandSphere/Topiks/blob/master/LICENSE"
    val kofiUrl = "https://ko-fi.com/grandspherestudios"
    val liberapayUrl = "https://liberapay.com/GrandSphere"
    val isDarkTheme : Boolean = isSystemInDarkTheme()
    val hyperlinkColour: Color = if (isDarkTheme) Color(0xFF00B2FF) else Color(0xFF0051FF)
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id =
                if (isDarkTheme) R.mipmap.topiks_icon_fulltransparent_foreground
                else R.mipmap.topiks_icon_fulltransparent_light_foreground),
            contentDescription = "App Icon",
            modifier = Modifier
                .size(128.dp)
                .align(Alignment.CenterHorizontally),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Topiks",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Grand Sphere Studios",
            style = MaterialTheme.typography.bodyMedium,
        )
        Text(
            text = "Version: ${Version.APP}",
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "License:",
            style = MaterialTheme.typography.bodyMedium,
        )
        ClickableText(
            text = AnnotatedString("GNU General Public License 3"),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = hyperlinkColour,
            ),
            onClick = {
                uriHandler.openUri(licenseUrl)
            }
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        )
        {
            ClickableText(
                text = AnnotatedString("(GPLv3)"),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = hyperlinkColour,
                ),
                onClick = {
                    uriHandler.openUri(licenseUrl)
                }
            )

            Spacer(modifier = Modifier.width(2.dp))
            Icon(
                imageVector = Icons.Default.OpenInNew,

                contentDescription = "Hyperlink Icon",
                modifier = Modifier.size(10.dp)
                    .align(alignment = Alignment.CenterVertically),
                tint =  hyperlinkColour
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Github:",
            style = MaterialTheme.typography.bodyMedium,
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        )
        {
            ClickableText(
                text = AnnotatedString("GitHub Repository"),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = hyperlinkColour,
                ),
                onClick = {
                    uriHandler.openUri(githubUrl)
                }
            )
            Spacer(modifier = Modifier.width(2.dp))
            Icon(
                imageVector = Icons.Default.OpenInNew,
                contentDescription = "Hyperlink Icon",
                modifier = Modifier.size(10.dp).align(alignment = Alignment.CenterVertically),
                tint = hyperlinkColour
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Donate:",
            style = MaterialTheme.typography.bodyMedium,
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        )
        {
            ClickableText(
                text = AnnotatedString("Ko-fi"),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = hyperlinkColour,
                ),
                onClick = {
                    uriHandler.openUri(kofiUrl)
                }
            )
            Spacer(modifier = Modifier.width(2.dp))
            Icon(
                imageVector = Icons.Default.OpenInNew,
                contentDescription = "Hyperlink Icon",
                modifier = Modifier.size(10.dp)
                    .align(alignment = Alignment.CenterVertically),
                tint = hyperlinkColour
            )
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        )
        {
            ClickableText(
                text = AnnotatedString("Liberapay"),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = hyperlinkColour,
                ),
                onClick = {
                    uriHandler.openUri(liberapayUrl)
                }
            )
            Spacer(modifier = Modifier.width(2.dp))
            Icon(
                imageVector = Icons.Default.OpenInNew,
                contentDescription = "Hyperlink Icon",
                modifier = Modifier.size(10.dp)
                    .align(alignment = Alignment.CenterVertically),
                tint = hyperlinkColour
            )
        }
    }
}