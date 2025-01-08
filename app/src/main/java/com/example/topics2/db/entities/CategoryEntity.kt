package com.example.topics2.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories_tbl")
data class CategoriesTbl(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val createTime: Long,
    val lastEditTime: Long,
    val colour: Int,
    val iconPath: String,
    val priority: Int
)
