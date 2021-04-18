package com.savage9ishere.tiwarimart.main_flow.ui.user.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.savage9ishere.tiwarimart.main_flow.ui.user.favorites.favorites_database.FavoritesDao
import java.lang.IllegalArgumentException

class FavoritesViewModelFactory(private val favoritesDatabase: FavoritesDao): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)){
            return FavoritesViewModel(favoritesDatabase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}