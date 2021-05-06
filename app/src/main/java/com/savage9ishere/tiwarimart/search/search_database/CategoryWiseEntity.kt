package com.savage9ishere.tiwarimart.search.search_database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_wise_items_table")
data class CategoryWiseEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "item_id")
    val itemId : Long = 0L,

    @ColumnInfo(name = "price_with_discount")
    val priceWithDiscount :Float = 0f,

    @ColumnInfo(name = "item_name")
    val itemName : String = "",

    @ColumnInfo(name = "category_of_item")
    val itemCategory : String = "",

    @ColumnInfo(name = "discount")
    val discount : String = "",

    @ColumnInfo(name = "rating_count")
    val ratingCount : Long = 0L,

    @ColumnInfo(name = "rating")
    val ratingTotal : Float = 0f,

    @ColumnInfo(name = "price")
    val price : String = "",

    @ColumnInfo(name = "img_url")
    val imageUrl : String = "",

    @ColumnInfo(name =  "in_stock")
    val inStock : Boolean = false,

    @ColumnInfo(name = "item_key")
    val itemKey : String = ""
)