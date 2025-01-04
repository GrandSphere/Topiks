package com.example.topics2.model.enitities

import androidx.compose.foundation.MutatePriority
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "topics")
data class TopicTbl(
    @PrimaryKey(autoGenerate = true) val topicId: Int = 0,
    val topicName: String,
    val topicLastEdit: Long,
    val topicCreated: Long,
    val topicColour: String,
    val topicCategory: String,
    val topicIcon: String,
    val topicPriority: Int
)
