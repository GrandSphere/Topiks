package com.example.topics2.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.topics2.ui.components.global.CustomTextBox
import com.example.topics2.ui.viewmodels.MessageViewModel
import com.example.topics2.utilities.helper.TemporaryDataHolder

@Composable
fun MessageViewScreen(navController: NavController, viewModel: MessageViewModel) {
    var inputText by remember { mutableStateOf(""
  // viewModel.messages(3)
  //      viewModel.gettempID()
    ) }
    Box(){
Column(modifier = Modifier.padding(horizontal = 8.dp)) {
    CustomTextBox(
        onValueChange = { newtext -> inputText = newtext },
        inputText = inputText,
        sPlaceHolder = "",
        boxModifier = Modifier.weight(1f),
        focusModifier = Modifier.weight(1f)
    )

}
    FloatingActionButton(
        onClick = {
            navController.popBackStack()
        },
        shape = CircleShape, // Change the shape to rounded corners
        modifier = Modifier
            .align(Alignment.BottomEnd)
            //.align(Alignment.BottomEnd) // Align it to bottom end of the Box
            .padding(16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "Add Topic",
            modifier = Modifier.size(24.dp)
        )
    }
}

}