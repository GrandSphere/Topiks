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

package com.GrandSphere.Topiks.model

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