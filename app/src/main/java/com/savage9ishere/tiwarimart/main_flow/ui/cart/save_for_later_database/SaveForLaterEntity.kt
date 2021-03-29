package com.savage9ishere.tiwarimart.main_flow.ui.cart.save_for_later_database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "save_for_later_table")
data class SaveForLaterEntity(
    @PrimaryKey(autoGenerate = true)
    val saveForLaterId : Long = 0L,

    @ColumnInfo(name = "item_name")
    val itemName: String = "",

    @ColumnInfo(name = "item_price")
    val itemPrice: Int = 0,

    @ColumnInfo(name = "item_size")
    val itemSize: String = "",

    @ColumnInfo(name = "item_qty")
    val itemQty: Int = 0,

    @ColumnInfo(name = "stock_availability")
    val stockAvailability: Boolean = false,

    @ColumnInfo(name = "photo_url")
    val photoUrl: String = "",

    @ColumnInfo(name = "item_key")
    val itemKey: String? = "",

    @ColumnInfo(name = "item_category")
    val itemCategory: String = ""
)