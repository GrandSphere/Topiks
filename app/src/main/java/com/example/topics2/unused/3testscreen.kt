package com.example.topics2.unused

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun testScreen3() {
    val modalBottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetContent = {
            // Content for the bottom sheet
            Text("This is the bottom sheet")
        }
    ) {
        Column {
            Button(onClick = {
                scope.launch {
                    modalBottomSheetState.show()
                }
            }) {
                Text("Show Bottom Sheet")
            }
        }
    }
}