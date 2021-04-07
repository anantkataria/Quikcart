package com.savage9ishere.tiwarimart.main_flow.ui.cart.cart_items_database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_item_table")
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true)
    val cartId: Long = 0L,

    @ColumnInfo(name = "item_name")
    val itemName: String = "",

    @ColumnInfo(name = "item_price")
    val itemPrice: Int = 0,

    @ColumnInfo(name = "item_price_original")
    val itemPriceOriginal: Int = 0,

    @ColumnInfo(name = "item_size")
    val itemSize: String = "",

    @ColumnInfo(name = "item_qty")
    var itemQty: Int = 0,

    @ColumnInfo(name = "stock_availability")
    val stockAvailability: Boolean = false,

    @ColumnInfo(name = "photo_url")
    val photoUrl: String = "",

    @ColumnInfo(name = "item_key")
    val itemKey: String? = "",

    @ColumnInfo(name = "item_category")
    val itemCategory: String = "",

    @ColumnInfo(name = "delivery_duration")
    val deliveryDuration: String = ""
)