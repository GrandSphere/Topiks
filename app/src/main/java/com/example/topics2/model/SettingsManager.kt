// SettingsManager.kt
package com.example.topics2.model

import android.content.Context
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

object SettingsManager {

    private const val SETTINGS_FILE_NAME = "settings.xml"
    private var settings: MutableMap<String, Any> = DefaultSettings.settingsMap.toMutableMap()

    // Initialize settings from XML
    fun initialize(context: Context) {
        val file = File(context.filesDir, SETTINGS_FILE_NAME)
        if (file.exists()) {
            val document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)
            val root = document.documentElement

            // Iterate through all elements in XML and set values dynamically
            for (key in settings.keys) {
                val element = root.getElementsByTagName(key).item(0)
                if (element != null) {
                    settings[key] = when (settings[key]) {
                        is Boolean -> element.textContent.toBoolean()
                        else -> element.textContent // Default to string
                    }
                }
            }
        } else {
            saveSettings(context)
        }
    }

    // Get the current settings
    fun getSettings(): Map<String, Any> = settings

    // Update a specific setting dynamically
    fun updateSetting(key: String, value: Any) {
        settings[key] = value

    }

    // Save settings to XML
    fun saveSettings(context: Context) {
        val document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument()
        val rootElement = document.createElement("settings")
        document.appendChild(rootElement)

        // Write each setting from the map to XML
        for ((key, value) in settings) {
            val element = document.createElement(key)
            element.appendChild(document.createTextNode(value.toString()))
            rootElement.appendChild(element)
        }

        // Write the document to a file
        val transformer: Transformer = TransformerFactory.newInstance().newTransformer()
        transformer.setOutputProperty(OutputKeys.INDENT, "yes")
        val file = File(context.filesDir, SETTINGS_FILE_NAME)
        val result = StreamResult(file)
        transformer.transform(DOMSource(document), result)
    }
}