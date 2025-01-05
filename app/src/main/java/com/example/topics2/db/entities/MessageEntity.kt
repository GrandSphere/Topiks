package com.example.topics2.db.enitities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageTbl(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val topicId: Int?,  // Foreign key to the Topic table
    val messageContent: String,
    val messageTimestamp: Long,
    val messagePriority: Int,
    val filePath: String="",
    val fileType: Int = 0,
)
