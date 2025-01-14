package com.example.topics2.db.enitities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.topics2.db.entities.CategoriesTbl

@Entity(
    tableName = "topic_tbl",
    foreignKeys = [
        ForeignKey(
            entity = CategoriesTbl::class, // Reference to CategoryTbl
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TopicTbl(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val lastEditTime: Long,
    val createTime: Long,
    val colour: Int,
    val categoryId: Int, // Foreign key from CategoryTbl
    val iconPath: String,
    val priority: Int
)

