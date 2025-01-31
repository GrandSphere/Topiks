package com.example.topics2.db.enitities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.topics2.db.entities.CategoriesTbl


@Entity(
    tableName = "message_tbl",
    foreignKeys = [
        ForeignKey(
            entity = TopicTbl::class, // Reference to TopicTbl
            parentColumns = ["id"],
            childColumns = ["topicId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CategoriesTbl::class, // Reference to CategoryTbl
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["topicId"]),  // Index on topicId for faster lookups by topic
        Index(value = ["lastEditTime"])  // Index on lastEditTime for faster sorting by last edit time
    ]
)
data class MessageTbl(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val topicId: Int, // Foreign key from TopicTbl
    val content: String,
    val lastEditTime: Long,
    val createTime: Long,
    val categoryId: Int, // Foreign key from CategoryTbl
    val priority: Int,
    val type: Int
)


/*
    fileType 0 = message
    fileType 1 = image
    fileType 2 = rest
 */