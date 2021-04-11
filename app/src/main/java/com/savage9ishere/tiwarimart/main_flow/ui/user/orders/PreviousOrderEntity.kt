package com.savage9ishere.tiwarimart.main_flow.ui.user.orders

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.savage9ishere.tiwarimart.main_flow.ui.home.CartItems

@Entity(tableName = "previous_order_table")
data class PreviousOrderEntity(

    @PrimaryKey(autoGenerate = true)
    val pOrderId : Long = 0L,


    val listItems : ArrayList<CartItems> = arrayListOf(),


)