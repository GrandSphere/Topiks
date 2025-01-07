package com.example.topics2.ui.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.topics2.DbTopics
import com.example.topics2.db.dao.CategoriesDao
import com.example.topics2.db.entities.CategoriesTbl


class CategoryViewModel (private val categoriesDao: CategoriesDao): ViewModel() {



    // add a new category
    suspend fun addtestcat() {
        categoriesDao.insertCategory(
            CategoriesTbl(
               // id = 0,
                name = "temp",
                createTime = 1,
                lastEditTime = 1,
                colour = 123,
                iconPath = "",
                priority = 1
            )
        )
    }




    companion object {
        // Factory to create the ViewModel
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                // Get the Application object from extras
                val application = checkNotNull(extras[APPLICATION_KEY])

                // Get the TopicDao from the Application class
                val myApplication = application as DbTopics
                return CategoryViewModel(myApplication.categoryDao) as T
            }
        }
    }
}