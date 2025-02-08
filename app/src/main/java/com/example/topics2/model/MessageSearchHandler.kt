package com.example.topics2.model

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MessageSearchHandler(private var dataset: List<Message>) {
    private var results: List<Message> = listOf()
    private var currentJob: Job? = null

    fun search(query: String, debounceTime: Long = 150L, onResults: (List<Message>) -> Unit) {
        currentJob?.cancel()

        val includes = mutableListOf<String>()
        val excludes = mutableListOf<String>()

        query.split(" ").forEach { part ->
            if (part.startsWith("!") && part.length > 1) {
                excludes.add(part.drop(1).lowercase())
            } else if (!part.startsWith("!")) {
                includes.add(part.lowercase())
            }
        }

        currentJob = GlobalScope.launch {
            delay(debounceTime)

            if (query.isNotEmpty()) {
                results = dataset.filter { entry ->
                    val matchesInclude = includes.all { word -> word in entry.content.lowercase() }
                    val matchesExclude = excludes.none { word -> word in entry.content.lowercase() }
                    matchesInclude && matchesExclude
                }.take(30)

            } else {
                results = emptyList()
            }

            onResults(results)
        }
    }

    fun updateDataset(newDataset: List<Message>) {
        dataset = newDataset
    }
}