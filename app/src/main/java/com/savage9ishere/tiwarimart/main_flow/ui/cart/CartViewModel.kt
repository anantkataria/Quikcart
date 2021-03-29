package com.savage9ishere.tiwarimart.main_flow.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.savage9ishere.tiwarimart.main_flow.ui.cart.cart_items_database.CartItemDao
import com.savage9ishere.tiwarimart.main_flow.ui.cart.cart_items_database.CartItemEntity
import com.savage9ishere.tiwarimart.main_flow.ui.cart.save_for_later_database.SaveForLaterDao
import com.savage9ishere.tiwarimart.main_flow.ui.cart.save_for_later_database.SaveForLaterEntity
import kotlinx.coroutines.launch

class CartViewModel(private val cartItemsDatabase: CartItemDao,private val saveForLaterDatabase: SaveForLaterDao) : ViewModel() {

    val cartItems = cartItemsDatabase.getAllCartItems()
    val saveForLaterItems = saveForLaterDatabase.getAllSaveForLaterItems()
    val itemCount = cartItemsDatabase.getItemCount()
    val subTotal = cartItemsDatabase.getSubTotal()

    val itemCountForSaveForLater = saveForLaterDatabase.getItemCount()

    fun onIncrementClick(cartItemEntity: CartItemEntity) {
        //increment item quantity by one and update in the database,
        //which will automatically update the listView adapter

        val qty = cartItemEntity.itemQty + 1
        cartItemEntity.itemQty = qty

        viewModelScope.launch {
            cartItemsDatabase.update(cartItemEntity)
        }
    }

    fun onDecrementClick(cartItemEntity: CartItemEntity) {
        //if qty is >1, then simply decrement the qty by one
        //otherwise if qty == 1, then remove that item from the database
        //this will automatically reomve the item from the adapter
        viewModelScope.launch {
            var qty = cartItemEntity.itemQty
            if(qty > 1){
                qty -= 1
                cartItemEntity.itemQty = qty
                cartItemsDatabase.update(cartItemEntity)
            }
            else {
                cartItemsDatabase.delete(cartItemEntity)
            }
        }
    }

    fun onSaveForLaterClick(cartItemEntity: CartItemEntity) {
        //remove this item from the cart item database
        //and add it to saveForLater database
        viewModelScope.launch {
            cartItemsDatabase.delete(cartItemEntity)
            val saveForLaterItem = SaveForLaterEntity(itemName = cartItemEntity.itemName,
            itemPrice = cartItemEntity.itemPrice,
            itemSize = cartItemEntity.itemSize,
            itemQty = cartItemEntity.itemQty,
            stockAvailability = cartItemEntity.stockAvailability,
            photoUrl = cartItemEntity.photoUrl,
            itemKey = cartItemEntity.itemKey,
            itemCategory = cartItemEntity.itemCategory)

            saveForLaterDatabase.insert(saveForLaterItem)
        }
    }

    fun onDeleteClick(cartItemEntity: CartItemEntity) {
        viewModelScope.launch {
            cartItemsDatabase.delete(cartItemEntity)
        }
    }

    fun onDeleteClick2(saveForLaterEntity: SaveForLaterEntity) {
        viewModelScope.launch {
            saveForLaterDatabase.delete(saveForLaterEntity)
        }
    }

    fun onMoveToCartClick(saveForLaterEntity: SaveForLaterEntity) {
        viewModelScope.launch {
            saveForLaterDatabase.delete(saveForLaterEntity)

            val cartItem = CartItemEntity(
                itemName = saveForLaterEntity.itemName,
                itemPrice = saveForLaterEntity.itemPrice,
                itemSize = saveForLaterEntity.itemSize,
                itemQty = saveForLaterEntity.itemQty,
                stockAvailability = saveForLaterEntity.stockAvailability,
                photoUrl = saveForLaterEntity.photoUrl,
                itemKey = saveForLaterEntity.itemKey,
                itemCategory = saveForLaterEntity.itemCategory
            )
            cartItemsDatabase.insert(cartItem)
        }
    }
}