package com.example.topics2.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.topics2.db.dao.CategoriesDao
import com.example.topics2.db.dao.FilesDao
import com.example.topics2.db.dao.MessageDao
import com.example.topics2.db.dao.TopicDao
import com.example.topics2.db.enitities.MessageTbl
import com.example.topics2.db.enitities.TopicTbl
import com.example.topics2.db.entities.CategoriesTbl
import com.example.topics2.db.entities.FileTbl


@Database(entities = [TopicTbl::class, MessageTbl::class, CategoriesTbl::class, FileTbl::class], version = 2)
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
                    "topics_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}


