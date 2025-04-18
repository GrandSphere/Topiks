// SettingsManager.kt
package com.GrandSphere.Topiks.model

import android.content.Context
import android.os.Environment
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

object SettingsManager {

    private const val SETTINGS_FILE_NAME = "settings.xml"
    private val settingsDir: File = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
        "topiks/files"
    )
    private val settingsFile: File = File(settingsDir, SETTINGS_FILE_NAME)

    private var settings: MutableMap<String, Any> = DefaultSettings.settingsMap.toMutableMap()

    // Initialize settings from XML
    fun initialize(context: Context) {
        if (!settingsDir.exists()) {
            settingsDir.mkdirs() // Ensure the directory exists
        }

        if (settingsFile.exists()) {
            val document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(settingsFile)
            val root = document.documentElement

            for (key in settings.keys) {
                val element = root.getElementsByTagName(key).item(0)
                if (element != null) {
                    settings[key] = when (settings[key]) {
                        is Boolean -> element.textContent.toBoolean()
                        else -> element.textContent
                    }
                }
            }
        } else {
            saveSettings(context = context)
            settingsFile.setReadable(true)
            settingsFile.setWritable(true)
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
        if (!settingsDir.exists()) {
            settingsDir.mkdirs() // Ensure directory exists
        }

        val document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument()
        val rootElement = document.createElement("settings")
        document.appendChild(rootElement)

        for ((key, value) in settings) {
            val element = document.createElement(key)
            element.appendChild(document.createTextNode(value.toString()))
            rootElement.appendChild(element)
        }

        val transformer: Transformer = TransformerFactory.newInstance().newTransformer()
        transformer.setOutputProperty(OutputKeys.INDENT, "yes")
        val result = StreamResult(settingsFile)
        transformer.transform(DOMSource(document), result)
    }
}
