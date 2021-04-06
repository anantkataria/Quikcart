package com.savage9ishere.tiwarimart.checkout.address.new_address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.savage9ishere.tiwarimart.checkout.address.address_database.AddressDao
import java.lang.IllegalArgumentException

class AddNewAddressViewModelFactory(private val addressDatabase: AddressDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddNewAddressViewModel::class.java)){
            return AddNewAddressViewModel(addressDatabase) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}