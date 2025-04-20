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

package com.GrandSphere.Topiks.db.enitities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.GrandSphere.Topiks.db.entities.CategoriesTbl

@Entity(
    tableName = "topic_tbl",
    foreignKeys = [
        ForeignKey(
            entity = CategoriesTbl::class, // Reference to CategoryTbl
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["lastEditTime"]),  // Index on lastEditTime for faster sorting
        Index(value = ["createTime"]),    // Index on createTime for faster sorting
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