package com.savage9ishere.tiwarimart.main_flow.ui.cart.cart_items_database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Dao
interface CartItemDao {

    @Insert
    suspend fun insert(item: CartItemEntity)

    @Update
    suspend fun update(item: CartItemEntity)

    @Delete
    suspend fun delete(item: CartItemEntity)

    @Query("SELECT * FROM cart_item_table")
    fun getAllCartItems() : LiveData<List<CartItemEntity>>

    @Query("SELECT * FROM cart_item_table WHERE cartId = :id")
    suspend fun getItemById(id: Long) : CartItemEntity

    @Query("SELECT SUM(item_qty) FROM cart_item_table")
    fun getItemCount() : LiveData<Int?>

    @Query("SELECT SUM(item_price*item_qty) FROM cart_item_table")
    fun getSubTotal() : LiveData<Int?>

}