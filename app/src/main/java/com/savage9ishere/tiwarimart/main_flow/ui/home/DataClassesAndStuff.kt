package com.savage9ishere.tiwarimart.main_flow.ui.home

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(val name: String = "", val uri: String = "", val key : String = "") : Parcelable

@Parcelize
data class Item(val name: String = "", var size: String = "", var price: String = "", val discount: String = "", val description: String = "", val deliveryDuration: String = "", val photosUrl: ArrayList<String> = ArrayList(), val key: String? = "", val ratingTotal: Long = 0L, val peopleRatingCount: Long = 0L, var otherSizes: MutableMap<String, String> = mutableMapOf(), val inStock: Boolean = true) : Parcelable

@Parcelize
//todo can add photos functionality
data class Review(val userName: String="", val ratingVal : Float = 0F, val reviewDescription: String = "", val useful: Int = 0, val key: String = "") : Parcelable