package com.savage9ishere.tiwarimart.main_flow.ui.user.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.savage9ishere.tiwarimart.main_flow.ui.user.orders.previous_orders.PreviousOrderDao
import java.lang.IllegalArgumentException

class OrdersViewModelFactory(private val previousOrdersDatabase : PreviousOrderDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrdersViewModel::class.java)){
            return OrdersViewModel(previousOrdersDatabase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}