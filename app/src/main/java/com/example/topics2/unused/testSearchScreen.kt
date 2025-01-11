package com.example.topics2.unused

import android.util.Log
import android.widget.Space
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.nio.file.WatchEvent



@Composable
fun fTestSearchScreen(navController: NavController) {
    Column() {

        Box(
            Modifier.fillMaxWidth().background(Color.Red)
        ) {
            LongClickTextField()
        }

        Box(
            Modifier.fillMaxWidth().background(Color.Yellow)
        ) {

        }

        //TestCustomSearchBox   ("",{}, navController)

    }
}



@Composable
fun LongClickTextField() {
    val textState = remember { androidx.compose.runtime.mutableStateOf(TextFieldValue()) }
    val focusRequester = remember { FocusRequester() }

    Column (

        modifier = Modifier
            .focusRequester(focusRequester)
            .background(Color.Yellow)
            .padding(80.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {

                        Log.d("zzee", ".............aaaaaaaaaaaaa")
                    },
                    onLongPress = {

                        // Handle the long press here
                        Log.d("zzee", ".............aaaaaaaaaaaaa")
                    }
                )
            }


    ){
        TextField(
            value = textState.value,
            onValueChange = { textState.value = it },
            modifier = Modifier
                .padding(80.dp)
                .focusRequester(focusRequester)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {

                            Log.d("zzee", ".............aaaaaaaaaaaaa")
                        },
                        onLongPress = {

                            // Handle the long press here
                            Log.d("zzee", ".............aaaaaaaaaaaaa")
                        }
                    )
                }
        )
    }
}