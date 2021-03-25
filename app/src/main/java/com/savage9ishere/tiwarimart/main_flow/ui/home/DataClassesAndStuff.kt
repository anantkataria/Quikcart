package com.savage9ishere.tiwarimart.main_flow.ui.home

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(val name: String = "", val uri: String = "", val key : String = "") : Parcelable

@Parcelize
data class Item(val name: String = "",val qty: String = "", val price: String = "", val discount: String = "", val description: String = "", val deliveryDuration: String = "", val photosUrl: ArrayList<String> = ArrayList(), val key: String? = "", val ratingTotal: Long = 0L, val peopleRatingCount: Long = 0L) : Parcelable