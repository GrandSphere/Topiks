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

package com.GrandSphere.Topiks

import android.app.Application
import com.GrandSphere.Topiks.db.AppDatabase
import com.GrandSphere.Topiks.db.dao.CategoriesDao
import com.GrandSphere.Topiks.db.dao.FilesDao
import com.GrandSphere.Topiks.db.dao.MessageDao
import com.GrandSphere.Topiks.db.dao.TopicDao


class DbTopics : Application() {

    val topicDao: TopicDao by lazy {
    val db = AppDatabase.getDatabase(this)  // Get database instance
        db.topicDao()
    }

    val messageDao: MessageDao by lazy {
        val db = AppDatabase.getDatabase(this)  // Get database instance
        db.messageDao()
    }

    val filesDao: FilesDao by lazy {
        val db = AppDatabase.getDatabase(this)  // Get database instance
        db.fileDao()
    }

    val categoryDao: CategoriesDao by lazy {
        val db = AppDatabase.getDatabase(this)  // Get database instance
        db.categoryDao()
    }

    override fun onCreate() {
        super.onCreate()
    }
}

