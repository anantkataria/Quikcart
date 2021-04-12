package com.savage9ishere.tiwarimart.main_flow.ui.user.orders.previous_orders

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PreviousOrderDao {

    @Insert
    suspend fun insert(item : PreviousOrderEntity)

    @Update
    suspend fun update(item: PreviousOrderEntity)

    @Delete
    suspend fun delete(item: PreviousOrderEntity)

    @Query("SELECT * FROM previous_order_table")
    fun getAllPreviousOrders() : LiveData<List<PreviousOrderEntity>>

}

