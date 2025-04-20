package com.GrandSphere.Topiks.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.GrandSphere.Topiks.R
import com.GrandSphere.Topiks.Version


@Composable
fun AboutScreen() {
    val uriHandler = LocalUriHandler.current
    val githubUrl = "https://github.com/riaan-ve/Topiks"

fun logVersion() {
    println("CuIrrent version: ${Version.APP}")
}
   val context = LocalContext.current
    val colors = MaterialTheme.colorScheme
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.mipmap.topiks_icon_blockfont_foreground), // Use the foreground resource
            contentDescription = "App Icon",
            modifier = Modifier
                .size(128.dp)
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp),
            contentScale = ContentScale.Fit
        )

        Text(
            text = "Topiks",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Version: ${Version.APP}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "Grand Sphere Studios",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        ClickableText(
            text = AnnotatedString("GitHub Repository"),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.Blue,
                fontSize = 16.sp
            ),
            onClick = {
                uriHandler.openUri(githubUrl)
            }
        )
    }
}