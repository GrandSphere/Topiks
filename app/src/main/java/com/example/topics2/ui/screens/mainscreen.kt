package com.example.topics2.ui.screens


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.topics2.ui.viewmodels.GreetingViewModel

@Composable
fun NameInputScreen(navController: NavController, viewModel: GreetingViewModel = viewModel()) {
    val name = viewModel.name.value

    // UI for entering a name
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = TextFieldValue(name),
            onValueChange = {
                viewModel.updateName(it.text)
            },
            label = { Text("Enter your name") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("greeting") }) {
            Text("Go to Greeting")
        }
    }
}
