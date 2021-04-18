package com.savage9ishere.tiwarimart.fragment_container.particular_item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.savage9ishere.tiwarimart.main_flow.ui.cart.cart_items_database.CartItemDao
import com.savage9ishere.tiwarimart.main_flow.ui.home.Item
import com.savage9ishere.tiwarimart.main_flow.ui.user.favorites.favorites_database.FavoritesDao
import java.lang.IllegalArgumentException

class ParticularItemViewModelFactory(
    private val item: Item,
    private val cartItemsDatabase: CartItemDao,
    private val categoryName: String,
    private val favoriteDataSource: FavoritesDao
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ParticularItemViewModel::class.java)){
            return ParticularItemViewModel(item, cartItemsDatabase, categoryName, favoriteDataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}