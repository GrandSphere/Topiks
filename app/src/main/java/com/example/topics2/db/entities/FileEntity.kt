package com.example.topics2.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.topics2.db.enitities.MessageTbl
import com.example.topics2.db.enitities.TopicTbl

@Entity(
    tableName = "file_tbl",
    foreignKeys = [
        ForeignKey(
            entity = TopicTbl::class, // Reference to TopicTbl
            parentColumns = ["id"],
            childColumns = ["topicId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MessageTbl::class, // Reference to MessageTbl
            parentColumns = ["id"],
            childColumns = ["messageId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CategoriesTbl::class, // Reference to CategoryTbl
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FileTbl(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val topicId: Int, // Foreign key from TopicTbl
    val messageId: Int, // Foreign key from MessageTbl
    val fileType: String,
    val filePath: String,
    val description: String,
    val iconPath: String,
    val categoryId: Int, // Foreign key from CategoryTbl
    val createTime: Long
)
data class FileInfo(
   val filePath: String,
   val fileType: String,
   val iconPath: String
)
data class FilePath(
    val id: Int,
    val filePath: String
)

data class FileInfoWithIcon(
    val filePath: String,
    val iconPath: String?
)