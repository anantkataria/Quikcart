package com.savage9ishere.tiwarimart.checkout.address.address_database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "address_table")
data class AddressEntity (

    @PrimaryKey(autoGenerate = true)
    val addressId : Long = 0L,

    val fullName : String  = "",

    val mobileNumber : String = "",

    val pinCode : String = "",

    val flatHouseNoName : String = "",

    val areaColonyStreet : String = "",

    val landmark : String  = "",

    val townCity : String = "",

    val state : String = "",

    val deliveryInstructions: String = ""
)