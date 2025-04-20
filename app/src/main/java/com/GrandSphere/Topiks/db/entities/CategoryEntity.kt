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
