package com.savage9ishere.tiwarimart.main_flow.ui.user.orders.particular_current_order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.savage9ishere.tiwarimart.main_flow.ui.home.AddressItem
import com.savage9ishere.tiwarimart.main_flow.ui.home.CartItems
import java.lang.IllegalArgumentException

class ParticularCurrentOrderViewModelFactory(
    private val listItems: ArrayList<CartItems>,
    private val address: AddressItem,
    private val paymentMethod: String,
    private val orderPlacedTime: Long,
    private val status: String
) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ParticularCurrentOrderViewModel::class.java)){
            return ParticularCurrentOrderViewModel(listItems, address, paymentMethod, orderPlacedTime, status) as T
        }
        throw IllegalArgumentException("Unknown viewModel class")
    }
}