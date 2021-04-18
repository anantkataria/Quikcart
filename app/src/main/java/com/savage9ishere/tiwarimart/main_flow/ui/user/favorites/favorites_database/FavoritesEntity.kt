package com.savage9ishere.tiwarimart.main_flow.ui.user.favorites.favorites_database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_items_table")
data class FavoritesEntity(

    @PrimaryKey(autoGenerate = true)
    val favoriteItemId : Long = 0L,

    @ColumnInfo(name = "category_name")
    val categoryName : String = "",

    @ColumnInfo(name = "item_name")
    val itemName : String = "",

    val size: String = "",

    val price: String = "",

    val discount: String = "",

    @ColumnInfo(name = "delivery_duration")
    val deliveryDuration : String = "",

    val photoUrl : String = "",

    val databaseKey : String? = "",

    @ColumnInfo(name = "rating_total")
    val ratingTotal : Float = 0f,

    @ColumnInfo(name = "people_rating_count")
    val peopleRatingCount : Long = 0L,

    @ColumnInfo(name = "in_stock")
    val inStock: Boolean = true
)