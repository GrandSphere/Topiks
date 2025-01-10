package com.example.topics2.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.topics2.db.entities.CategoriesTbl
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
}