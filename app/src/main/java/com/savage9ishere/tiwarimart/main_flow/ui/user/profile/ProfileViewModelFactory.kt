package com.savage9ishere.tiwarimart.main_flow.ui.user.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.savage9ishere.tiwarimart.checkout.address.address_database.AddressDao
import java.lang.IllegalArgumentException

class ProfileViewModelFactory (private val addressDatabase : AddressDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)){
            return ProfileViewModel(addressDatabase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}