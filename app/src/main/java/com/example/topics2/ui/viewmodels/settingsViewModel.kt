// SettingsViewModel.kt
package com.example.topics2.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.topics2.model.SettingsManager

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    // LiveData to hold the current settings
    val settingsLiveData: MutableLiveData<Map<String, Any>> = MutableLiveData()

    init {
        // Initialize settings from the SettingsManager when ViewModel is created
        SettingsManager.initialize(application.applicationContext)
        settingsLiveData.value = SettingsManager.getSettings()
    }

    // Method to update a specific setting
    fun updateSetting(key: String, value: Any) {
        SettingsManager.updateSetting(key, value)
        settingsLiveData.value = SettingsManager.getSettings() // Update LiveData
        saveSettings()
    }

    // Method to save the settings to XML
    fun saveSettings() {
        SettingsManager.saveSettings(getApplication())
    }

    companion object {
        // Factory to create the ViewModel
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                // Get the Application object from extras
                val application = checkNotNull(extras[APPLICATION_KEY])

                return SettingsViewModel(application as Application) as T
            }
        }
    }
}
