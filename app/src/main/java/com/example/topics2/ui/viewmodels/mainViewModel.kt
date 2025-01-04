package com.example.topics2.ui.viewmodels

// GreetingViewModel.kt

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State

class GreetingViewModel : ViewModel() {

    // State for the name input
    private val _name = mutableStateOf("")
    val name: State<String> get() = _name

    // Function to update the name
    fun updateName(newName: String) {
        _name.value = newName
    }
}
