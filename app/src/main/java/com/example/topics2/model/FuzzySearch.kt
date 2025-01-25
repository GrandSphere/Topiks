package com.example.topics2.model

import com.example.topics2.unused.old.TableEntry
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Data Classes

// ViewModel-like functionality using a single class
class T2SearchHandler(private val dataset: List<TableEntry>) {
    private var results: List<TableEntry> = listOf()
    private var currentJob: Job? = null // blue: Store current search job

    fun search(query: String, debounceTime: Long = 150L, onResults: (List<TableEntry>) -> Unit) {
        // blue: Cancel the previous job if there is one before starting a new one
        currentJob?.cancel()

        val includes = mutableListOf<String>()
        val excludes = mutableListOf<String>()
        query.split(" ").forEach { part ->
            if (part.startsWith("!")) excludes.add(part.substring(1).lowercase())
            else includes.add(part.lowercase())
        }

        // blue: Perform debounced search in a coroutine
        currentJob = GlobalScope.launch {
            delay(debounceTime) // blue: Ensure debounce before starting the search

            if (query.isNotEmpty()) { // blue: Handle empty search query (shows no results)
                results = dataset.filter { entry ->
                    includes.all { word -> word in entry.messageContentLower } && // blue: Checking for substrings
                            excludes.none { word -> word in entry.messageContentLower }
                }.take(30)
            } else {
                results = emptyList() // blue: Ensure no results are shown for empty queries
            }
            onResults(results)
        }
    }
}