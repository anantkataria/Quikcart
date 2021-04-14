package com.savage9ishere.tiwarimart.checkout.address.edit_address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.savage9ishere.tiwarimart.checkout.address.address_database.AddressDao
import com.savage9ishere.tiwarimart.main_flow.ui.home.AddressItem
import java.lang.IllegalArgumentException

class EditAddressViewModelFactory(
    private val addressDatabase: AddressDao,
    private val address: AddressItem
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditAddressViewModel::class.java)){
            return EditAddressViewModel(addressDatabase, address) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}