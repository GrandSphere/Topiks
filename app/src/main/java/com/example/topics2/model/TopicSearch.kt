package com.example.topics2.model

import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TopicSearchHandler(private var dataset: List<tblTopicIdName>) {
    private var results: List<tblTopicIdName> = listOf()
    private var currentJob: Job? = null

    fun search(query: String, debounceTime: Long = 150L, onResults: (List<tblTopicIdName>) -> Unit) {
        currentJob?.cancel()

        val includes = mutableListOf<String>()
        val excludes = mutableListOf<String>()

        query.split(" ").forEach { part ->
            if (part.startsWith("!")) {
                excludes.add(part.substring(1).lowercase())
            } else {
                includes.add(part.lowercase())
            }
        }

        currentJob = GlobalScope.launch {
            delay(debounceTime)

            if (query.isNotEmpty()) {
                results = dataset.filter { entry ->
                    val matchesInclude = includes.all { word -> word in entry.name.lowercase() }
                    val matchesExclude = excludes.none { word -> word in entry.name.lowercase() }
                    matchesInclude && matchesExclude
                }.take(30)

            } else {
                results = emptyList()
            }

            onResults(results)
        }
    }

    fun updateDataset(newDataset: List<tblTopicIdName>) {
        dataset = newDataset
    }
}
