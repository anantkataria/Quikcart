package com.savage9ishere.tiwarimart.checkout.address.edit_address

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.savage9ishere.tiwarimart.checkout.address.address_database.AddressDao
import com.savage9ishere.tiwarimart.checkout.address.address_database.AddressEntity
import com.savage9ishere.tiwarimart.main_flow.ui.home.AddressItem
import kotlinx.coroutines.launch

class EditAddressViewModel(private val addressDatabase: AddressDao, address: AddressItem) : ViewModel() {

    private val _fullName = MutableLiveData<String?>()
    val fullName : LiveData<String?>
        get() = _fullName

    private val _mobileNumber = MutableLiveData<String?>()
    val mobileNumber : LiveData<String?>
        get() = _mobileNumber

    private val _pinCode = MutableLiveData<String?>()
    val pinCode : LiveData<String?>
        get() = _pinCode

    private val _flatHouseNoName = MutableLiveData<String?>()
    val flatHouseNoName : LiveData<String?>
        get() = _flatHouseNoName

    private val _areaColonyStreet = MutableLiveData<String?>()
    val areaColonyStreet : LiveData<String?>
        get() = _areaColonyStreet

    private val _landmark = MutableLiveData<String?>()
    val landMark : LiveData<String?>
        get() = _landmark

    private val _townCity = MutableLiveData<String?>()
    val townCity : LiveData<String?>
        get() = _townCity

    private val _state = MutableLiveData<String?>()
    val state : LiveData<String?>
        get() = _state

    private val _updateComplete = MutableLiveData<Boolean?>()
    val updateComplete : LiveData<Boolean?>
        get() = _updateComplete

    init {
        _fullName.value = address.fullName
        _mobileNumber.value = address.mobileNumber
        _pinCode.value = address.pinCode
        _flatHouseNoName.value = address.flatHouseNoName
        _areaColonyStreet.value = address.areaColonyStreet
        _landmark.value = address.landMark
        _townCity.value = address.townCity
        _state.value = address.state
    }

    fun setState(state: String) {
        _state.value = state
    }

    fun updateAddress(address: AddressEntity) {
        viewModelScope.launch {
            addressDatabase.update(address)
        }
        _updateComplete.value = true
    }

    fun doneUpdateComplete(){
        _updateComplete.value = null
    }
}