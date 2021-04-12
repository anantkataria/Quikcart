package com.savage9ishere.tiwarimart.main_flow.ui.user.orders.previous_orders

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.savage9ishere.tiwarimart.main_flow.ui.home.AddressItem
import com.savage9ishere.tiwarimart.main_flow.ui.home.CartItems

@Entity(tableName = "previous_order_table")
data class PreviousOrderEntity(

    @PrimaryKey(autoGenerate = true)
    val orderId : Long = 0L,

    @Embedded
    val address: AddressItem = AddressItem(),

    @ColumnInfo(name = "auth_phone")
    val authPhone : String = "",

    @ColumnInfo(name = "list_items")
    val listItems : ArrayList<CartItems> = arrayListOf(),

    @ColumnInfo(name = "order_delivered_or_cancelled_time")
    val orderDeliveredOrCancelledTime : Long = 0L,

    @ColumnInfo(name = "order_key")
    val orderKey : String = "",

    @ColumnInfo(name = "order_placed_time")
    val orderPlacedTime : Long = 0L,

    @ColumnInfo(name = "payment_method")
    val paymentMethod : String = "",

    val status : String = ""
)