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

package com.GrandSphere.Topiks.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.GrandSphere.Topiks.db.entities.CategoriesTbl
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoriesTbl): Long

    @Update
    suspend fun updateCategory(category: CategoriesTbl)

    @Delete
    suspend fun deleteCategory(category: CategoriesTbl)

    @Query("SELECT * FROM categories_tbl")
    fun getAllCategories(): Flow<List<CategoriesTbl>>

    @Query("SELECT * FROM categories_tbl WHERE id = :categoryId")
    fun getCategoryById(categoryId: Int): Flow<CategoriesTbl>

    @RawQuery
    fun checkpoint(query: SupportSQLiteQuery): Int

}