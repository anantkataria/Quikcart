package com.savage9ishere.tiwarimart.main_flow.ui.user.favorites.on_favorite_click

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.savage9ishere.tiwarimart.main_flow.ui.cart.cart_items_database.CartItemDao
import com.savage9ishere.tiwarimart.main_flow.ui.user.favorites.favorites_database.FavoritesDao
import java.lang.IllegalArgumentException

class LoadItemDataViewModelFactory(private val categoryName : String, private val databaseKey : String, private val cartDatabase : CartItemDao, private val favoritesDatabase: FavoritesDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoadItemDataViewModel::class.java)){
            return LoadItemDataViewModel(categoryName, databaseKey, cartDatabase, favoritesDatabase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}