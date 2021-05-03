package com.savage9ishere.tiwarimart.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.savage9ishere.tiwarimart.search.search_database.CategoryDao
import com.savage9ishere.tiwarimart.search.search_database.CategoryWiseDao
import java.lang.IllegalArgumentException

class SearchViewModelFactory(private val categoryDatabase : CategoryDao, private val categoryWiseDatabase : CategoryWiseDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)){
            return SearchViewModel(categoryDatabase, categoryWiseDatabase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}