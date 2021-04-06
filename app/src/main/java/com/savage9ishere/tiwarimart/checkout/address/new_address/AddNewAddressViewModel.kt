package com.savage9ishere.tiwarimart.checkout.address.new_address

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.savage9ishere.tiwarimart.checkout.address.address_database.AddressDao
import com.savage9ishere.tiwarimart.checkout.address.address_database.AddressEntity
import kotlinx.coroutines.launch

class AddNewAddressViewModel(val addressDatabase: AddressDao) : ViewModel() {

    private val _doneInserting = MutableLiveData<Boolean?>()
    val doneInserting: LiveData<Boolean?>
        get() = _doneInserting

    fun addAddress(address: AddressEntity) {
        viewModelScope.launch {
            addressDatabase.insert(address)
            _doneInserting.value = true
        }
    }

    fun doneDoneInserting() {
        _doneInserting.value = null
    }

}