package com.savage9ishere.tiwarimart.checkout.address

import android.location.Address
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.savage9ishere.tiwarimart.checkout.address.address_database.AddressDao
import com.savage9ishere.tiwarimart.checkout.address.address_database.AddressEntity

class AddressViewModel(addressItemsDatabase: AddressDao) : ViewModel() {

    val addressItems = addressItemsDatabase.getAllAddresses()

    private val _onRootClicked = MutableLiveData<Boolean?>()
    val onRootClicked : LiveData<Boolean?>
        get() = _onRootClicked

    private val _onDeliverToThisAddress = MutableLiveData<AddressEntity?>()
    val onDeliverToThisAddress : LiveData<AddressEntity?>
        get() = _onDeliverToThisAddress

    private val _onEditThisAddress = MutableLiveData<AddressEntity?>()
    val onEditThisAddress: LiveData<AddressEntity?>
        get() = _onEditThisAddress

    private val _onAddDeliveryInstructions = MutableLiveData<AddressEntity?>()
    val onAddDeliveryInstructions: LiveData<AddressEntity?>
        get() = _onAddDeliveryInstructions

    fun onRootClicked() {
        _onRootClicked.value = true
    }

    fun onDeliverToThisAddressClicked(addressEntity: AddressEntity) {
        _onDeliverToThisAddress.value = addressEntity
    }

    fun onEditAddressClicked(addressEntity: AddressEntity) {
        _onEditThisAddress.value = addressEntity
    }

    fun onAddDeliveryInstructionsClicked(addressEntity: AddressEntity) {
        _onAddDeliveryInstructions.value = addressEntity
    }

    fun doneOnRootClicked() {
        _onRootClicked.value = null
    }

    fun doneOnDeliverToThisAddress() {
        _onDeliverToThisAddress.value = null
    }

    fun doneOnEditAddress() {
        _onEditThisAddress.value = null
    }

    fun doneAddDeliveryInstructions() {
        _onAddDeliveryInstructions.value = null
    }
}