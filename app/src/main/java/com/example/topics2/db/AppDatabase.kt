package com.example.topics2.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.topics2.model.dao.MessageDao
import com.example.topics2.model.dao.TopicDao
import com.example.topics2.model.enitities.MessageTbl
import com.example.topics2.model.enitities.TopicTbl

@Database(entities = [TopicTbl::class, MessageTbl::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun topicDao(): TopicDao
    abstract fun messageDao(): MessageDao

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
