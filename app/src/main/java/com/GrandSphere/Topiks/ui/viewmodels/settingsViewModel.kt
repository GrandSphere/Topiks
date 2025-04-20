/*******************************************************************************
 * Copyright (C) <2025> GrandSphere <GrandSphereStudios@protonmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
