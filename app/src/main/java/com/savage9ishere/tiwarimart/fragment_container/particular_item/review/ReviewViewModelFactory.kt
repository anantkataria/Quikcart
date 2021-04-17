package com.savage9ishere.tiwarimart.fragment_container.particular_item.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.savage9ishere.tiwarimart.main_flow.ui.home.Item
import java.lang.IllegalArgumentException

class ReviewViewModelFactory(private val item : Item, private val categoryName : String): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReviewViewModel::class.java)){
            return ReviewViewModel(item, categoryName) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}