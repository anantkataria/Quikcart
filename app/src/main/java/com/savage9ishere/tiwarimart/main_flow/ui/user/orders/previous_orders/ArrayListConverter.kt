package com.savage9ishere.tiwarimart.main_flow.ui.user.orders.previous_orders

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.savage9ishere.tiwarimart.main_flow.ui.home.CartItems

class ArrayListConverter {

    @TypeConverter
    fun fromString(str : String) : ArrayList<CartItems> {
        val listType = object : TypeToken<ArrayList<CartItems>>(){}.type
        return Gson().fromJson(str, listType)
    }

    @TypeConverter
    fun fromArrayList(listItems : ArrayList<CartItems>) : String {
        val gson = Gson()
        return gson.toJson(listItems)
    }

}