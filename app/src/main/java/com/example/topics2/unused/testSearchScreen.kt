package com.example.topics2.unused

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


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