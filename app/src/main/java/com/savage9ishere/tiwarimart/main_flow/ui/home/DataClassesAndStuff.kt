package com.savage9ishere.tiwarimart.main_flow.ui.home

import android.os.Parcelable
import com.savage9ishere.tiwarimart.main_flow.ui.user.profile.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(val name: String = "", val uri: String = "", val key : String = "") : Parcelable

@Parcelize
data class Item(val name: String = "", var size: String = "", var price: String = "", val discount: String = "", val description: String = "", val deliveryDuration: String = "", val photosUrl: ArrayList<String> = ArrayList(), val key: String? = "", val ratingTotal: Float = 0f, val peopleRatingCount: Long = 0L, var otherSizes: MutableMap<String, String> = mutableMapOf(), val inStock: Boolean = true) : Parcelable

@Parcelize
data class Review(val user : User = User(), val photosUrl : ArrayList<String> = arrayListOf(), val review : String = "", val title : String = "", val ratingVal : Float = 0F, val useful: Int = 0, val key: String = "") : Parcelable

@Parcelize
data class CartItems(val name: String = "", val size: String = "", val price: String = "", val priceOriginal: String = "", val qty : Int = 0, val photoUrl: String = "", val key: String? = "", val itemCategory: String = "", val deliveryDuration: String = "") : Parcelable

@Parcelize
data class AddressItem(val fullName: String = "", val mobileNumber: String = "", val pinCode: String = "", val flatHouseNoName: String = "", val areaColonyStreet: String = "", val landMark : String = "", val townCity : String = "", val state : String = "", val deliveryInstructions: String = "") : Parcelable{

    fun getAddress() : String {
        return "$fullName\n$mobileNumber\n$flatHouseNoName,\n$areaColonyStreet, $landMark\n$townCity, $pinCode\n$state"
    }
}