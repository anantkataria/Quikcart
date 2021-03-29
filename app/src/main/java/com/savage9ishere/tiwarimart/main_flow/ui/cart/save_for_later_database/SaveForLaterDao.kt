package com.savage9ishere.tiwarimart.main_flow.ui.cart.save_for_later_database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.savage9ishere.tiwarimart.main_flow.ui.cart.cart_items_database.CartItemEntity

@Dao
interface SaveForLaterDao {

    @Insert
    suspend fun insert(item: SaveForLaterEntity)

    @Update
    suspend fun update(item: SaveForLaterEntity)

    @Delete
    suspend fun delete(item: SaveForLaterEntity)

    @Query("SELECT * FROM save_for_later_table")
    fun getAllSaveForLaterItems(): LiveData<List<SaveForLaterEntity>>

    @Query("SELECT SUM(item_qty) FROM save_for_later_table")
    fun getItemCount() : LiveData<Int?>
}