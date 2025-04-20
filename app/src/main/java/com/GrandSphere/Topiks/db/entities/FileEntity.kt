/*******************************************************************************
 * Copyright (C) <2025> GrandSphere <GrandSphereStudios@protonmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.GrandSphere.Topiks.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.GrandSphere.Topiks.db.enitities.MessageTbl
import com.GrandSphere.Topiks.db.enitities.TopicTbl

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
    ],
    indices = [
        Index(value = ["topicId"]),  // Index on topicId for faster lookups by topic
        Index(value = ["messageId"])  // Index on messageId for faster lookups by message
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