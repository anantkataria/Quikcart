package com.savage9ishere.tiwarimart.main_flow.ui.user.orders.previous_orders.particular_previous_order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.savage9ishere.tiwarimart.main_flow.ui.home.AddressItem
import com.savage9ishere.tiwarimart.main_flow.ui.home.CartItems
import java.lang.IllegalArgumentException

class ParticularPreviousOrderViewModelFactory(
    private val listItems: ArrayList<CartItems>,
    private val address : AddressItem,
    private val paymentMethod : String,
    private val orderPlacedTime : Long,
    private val orderDeliveredOrCancelledTime : Long,
    private val status : String
) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ParticularPreviousOrderViewModel::class.java)){
            return ParticularPreviousOrderViewModel(listItems, address, paymentMethod, orderPlacedTime, orderDeliveredOrCancelledTime, status) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}