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

