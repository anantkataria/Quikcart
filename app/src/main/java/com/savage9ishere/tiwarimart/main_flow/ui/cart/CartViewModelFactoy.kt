package com.savage9ishere.tiwarimart.main_flow.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.savage9ishere.tiwarimart.main_flow.ui.cart.cart_items_database.CartItemDao
import com.savage9ishere.tiwarimart.main_flow.ui.cart.save_for_later_database.SaveForLaterDao
import java.lang.IllegalArgumentException

class CartViewModelFactory(private val cartItemsDatabase: CartItemDao, private val saveForLaterDatabase: SaveForLaterDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CartViewModel::class.java)){
            return CartViewModel(cartItemsDatabase, saveForLaterDatabase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}