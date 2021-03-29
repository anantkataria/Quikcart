package com.savage9ishere.tiwarimart.fragment_container.particular_item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.savage9ishere.tiwarimart.main_flow.ui.cart.cart_items_database.CartItemDao
import com.savage9ishere.tiwarimart.main_flow.ui.home.Item
import java.lang.IllegalArgumentException

class ParticularItemViewModelFactory(private val item : Item, private val cartItemsDatabase: CartItemDao, private val categoryName: String): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ParticularItemViewModel::class.java)){
            return ParticularItemViewModel(item, cartItemsDatabase, categoryName) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}