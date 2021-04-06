package com.savage9ishere.tiwarimart.checkout.final_bill

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.savage9ishere.tiwarimart.main_flow.ui.home.AddressItem
import com.savage9ishere.tiwarimart.main_flow.ui.home.CartItems

class FinalBillViewModel(
    private val listItems: ArrayList<CartItems>,
    private val address: AddressItem,
    private val paymentMethod: String
) : ViewModel() {

    private val _itemAddress = MutableLiveData<String?>()
    val itemAddress: LiveData<String?>
        get() = _itemAddress

    private val _deliveryTime = MutableLiveData<String?>()
    val deliveryTime: LiveData<String?>
        get() = _deliveryTime

    private val _billAmountTotal = MutableLiveData<String?>()
    val billAmountTotal: LiveData<String?>
        get() = _billAmountTotal

    private val _deliveryCharge = MutableLiveData<String?>()
    val deliveryCharge: LiveData<String?>
        get() = _deliveryCharge

    private val _totalSavings = MutableLiveData<String?>()
    val totalSavings : LiveData<String?>
        get() = _totalSavings

    private val _paymentMethodd = MutableLiveData<String?>()
    val paymentMethodd: LiveData<String?>
        get() = _paymentMethodd

    private val _cartItems = MutableLiveData<List<CartItems>?>()
    val cartItems : LiveData<List<CartItems>?>
        get() = _cartItems

}