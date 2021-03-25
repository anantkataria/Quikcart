package com.savage9ishere.tiwarimart.fragment_container.particular_category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException


class ParticularCategoryViewModelFactory(private val categoryName: String): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ParticularCategoryViewModel::class.java)){
            return ParticularCategoryViewModel(categoryName) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}