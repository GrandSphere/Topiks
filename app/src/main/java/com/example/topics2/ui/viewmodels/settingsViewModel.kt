// SettingsViewModel.kt
package com.example.topics2.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.topics2.model.DefaultSettings.settingsMap
import com.example.topics2.model.SettingsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val _settings = MutableStateFlow<Map<String, String>>(emptyMap())
    val settings: StateFlow<Map<String, String>> = _settings

    // Initialize settings when ViewModel is created
    init {
        loadSettings()
    }

    // Load settings from SettingsManager (this could be async)
    private fun loadSettings() {
        // Using a coroutine to load settings asynchronously
        viewModelScope.launch {
            SettingsManager.initialize(getApplication())
            _settings.value = SettingsManager.getSettings().mapValues { it.value.toString() }
        }
    }

    // Update a specific setting and persist it
    fun updateSetting(key: String, value: String) {
        // Update in the state flow
        _settings.value = _settings.value.toMutableMap().apply {
            this[key] = value
        }

        SettingsManager.updateSetting(key, value)
        saveSettings()
    }

    // Reload the settings from the SettingsManager (e.g., from file)
    fun reloadSettings() {
        viewModelScope.launch {
            SettingsManager.initialize(getApplication())
            _settings.value = SettingsManager.getSettings().mapValues { it.value.toString() }
        }
    }

    // Get the theme setting
    fun getTheme(): String {
        return _settings.value["theme"] ?: settingsMap["theme"] as? String ?: "Default"
    }

    // Additional getter methods for other settings (e.g., notificationsEnabled)
    fun getNotificationsEnabled(): Boolean {
        return _settings.value["notificationsEnabled"]?.toBoolean() ?: false
    }

    fun getNewSetting(): String {
        return _settings.value["newSetting"] ?: "Default Value"
    }

    // Save settings to the persistent storage (XML)
    private fun saveSettings() {
        SettingsManager.saveSettings(getApplication())
    }

    companion object {
        // Factory for providing SettingsViewModel instance
        fun Factory(settingsViewModel: SettingsViewModel): ViewModelProvider.Factory =
            ViewModelProvider.NewInstanceFactory()  // You can skip custom initialization if no need for params
    }
}

/* CREATE OBJECT LIKE THIS in composables
val activity = LocalContext.current as ComponentActivity
    val settingsViewModel: SettingsViewModel = viewModel(
        factory = Factory(activity.settingsViewModel)
    )
 */