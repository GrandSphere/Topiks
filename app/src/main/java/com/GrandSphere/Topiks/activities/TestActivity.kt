@file:Suppress("PreviewAnnotationInFunctionWithParameters")

package com.GrandSphere.Topiks.activities

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.GrandSphere.Topiks.ui.themes.TopiksTheme


class TestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { TopiksTheme { TestApp(applicationContext) } }
    }
}

//val Purple200 = Color(0xFFBB86FC) FIX THIS
@Composable
fun TestApp(context: Context) {
}