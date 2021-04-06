package com.savage9ishere.tiwarimart.checkout.final_bill

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.savage9ishere.tiwarimart.main_flow.ui.home.AddressItem
import com.savage9ishere.tiwarimart.main_flow.ui.home.CartItems
import java.lang.IllegalArgumentException

class FinalBillViewModelFactory(private val listItems : ArrayList<CartItems>, private val address : AddressItem, private val paymentMethod : String): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FinalBillViewModel::class.java)){
            return FinalBillViewModel(listItems, address, paymentMethod) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}