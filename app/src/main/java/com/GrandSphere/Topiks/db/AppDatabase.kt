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


