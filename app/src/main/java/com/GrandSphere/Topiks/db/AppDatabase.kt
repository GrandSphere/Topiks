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

package com.GrandSphere.Topiks.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.GrandSphere.Topiks.db.dao.CategoriesDao
import com.GrandSphere.Topiks.db.dao.FilesDao
import com.GrandSphere.Topiks.db.dao.MessageDao
import com.GrandSphere.Topiks.db.dao.TopicDao
import com.GrandSphere.Topiks.db.enitities.MessageTbl
import com.GrandSphere.Topiks.db.enitities.TopicTbl
import com.GrandSphere.Topiks.db.entities.CategoriesTbl
import com.GrandSphere.Topiks.db.entities.FileTbl


@Database(entities = [TopicTbl::class, MessageTbl::class, CategoriesTbl::class, FileTbl::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun topicDao(): TopicDao
    abstract fun messageDao(): MessageDao
    abstract fun fileDao(): FilesDao
    abstract fun categoryDao(): CategoriesDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // This function returns the instance of AppDatabase (Singleton pattern)
        fun getDatabase(context: Context): AppDatabase {
            // If an instance already exists, return it
            return INSTANCE ?: synchronized(this) {
                // If no instance exists, create one
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "topiks_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
                // Add this function to clear the existing instance
        fun clearInstance() {
            INSTANCE = null
        }
    }
}


