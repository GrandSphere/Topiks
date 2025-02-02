package com.example.topics2

import android.app.Application
import com.example.topics2.db.AppDatabase
import com.example.topics2.db.dao.CategoriesDao
import com.example.topics2.db.dao.FilesDao
import com.example.topics2.db.dao.MessageDao
import com.example.topics2.db.dao.TopicDao


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

