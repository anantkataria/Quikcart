package com.savage9ishere.tiwarimart.checkout.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.savage9ishere.tiwarimart.checkout.address.address_database.AddressDao
import java.lang.IllegalArgumentException

class AddressViewModelFactory(private val addressItemsDatabase: AddressDao): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddressViewModel::class.java)){
            return AddressViewModel(addressItemsDatabase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}