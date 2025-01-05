package com.example.topics2

import android.app.Application
import com.example.topics2.db.AppDatabase
import com.example.topics2.db.dao.MessageDao
import com.example.topics2.db.dao.TopicDao


class DbTopics : Application() {

    val db = AppDatabase.getDatabase(this)  // Get database instance
    val topicDao: TopicDao by lazy {
        db.topicDao()
    }

    val messageDao: MessageDao by lazy {
        db.messageDao()
    }
    override fun onCreate() {
        super.onCreate()
    }
}

