// SettingsViewModel.kt
package com.GrandSphere.Topiks.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.GrandSphere.Topiks.model.SettingsManager

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

    fun getTheme(): Int {
        val theme = settingsLiveData.value?.get("theme") as? String
        Log.d("QQWWEE","${theme}")
        return when { // Determine the output color based on luminance
            theme == "light" -> 1
            theme == "device" -> 2
            theme == "custom" -> 3
            else -> 0
        }
    }

    // Method to save the settings to XML
    fun saveSettings() {
        SettingsManager.saveSettings(getApplication())
    }

}
