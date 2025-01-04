package com.example.topics2

import android.app.Application
import com.example.topics2.db.AppDatabase
import com.example.topics2.db.dao.TopicDao


class MyApplication : Application() {

    // Declare the TopicDao and TopicRepository as properties
    val topicDao: TopicDao by lazy {
        val db = AppDatabase.getDatabase(this)  // Get database instance
        db.topicDao()  // Get TopicDao from the database
    }

    override fun onCreate() {
        super.onCreate()
        // Initialize any other app-wide dependencies here if necessary
    }
}
