package com.savage9ishere.tiwarimart.main_flow.ui.user.orders.previous_orders.particular_previous_order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.savage9ishere.tiwarimart.main_flow.ui.home.AddressItem
import com.savage9ishere.tiwarimart.main_flow.ui.home.CartItems
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ParticularPreviousOrderViewModel(
    listItems: ArrayList<CartItems>,
    address: AddressItem,
    paymentMethod: String,
    orderPlacedTime: Long,
    orderDeliveredOrCancelledTime: Long,
    status: String
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

    private val _billAmountTotalOriginal = MutableLiveData<String?>()
    val billAmountTotalOriginal : LiveData<String?>
        get() = _billAmountTotalOriginal

    private val _deliveryCharge = MutableLiveData<String?>()
    val deliveryCharge: LiveData<String?>
        get() = _deliveryCharge

    private val _billAmountWithDeliveryCharge = MutableLiveData<String?>()
    val billAmountWithDeliveryCharge : LiveData<String?>
        get() = _billAmountWithDeliveryCharge

    private val _totalSavings = MutableLiveData<String?>()
    val totalSavings : LiveData<String?>
        get() = _totalSavings

    private val _paymentMethodd = MutableLiveData<String?>()
    val paymentMethodd: LiveData<String?>
        get() = _paymentMethodd

    private val _cartItems = MutableLiveData<List<CartItems>?>()
    val cartItems : LiveData<List<CartItems>?>
        get() = _cartItems

    private val _orderPlacedTime = MutableLiveData<String?>()
    val orderPlacedTime : LiveData<String?>
        get() = _orderPlacedTime

    private val _orderDeliveredOrCancelledTime = MutableLiveData<String?>()
    val orderDeliveredOrCancelledTime : LiveData<String?>
        get() = _orderDeliveredOrCancelledTime

    private val _status = MutableLiveData<String?>()
    val status : LiveData<String?>
        get() = _status

    init {
        val addresss = address.getAddress()
        _itemAddress.value = addresss

        val deliveryDurations = Array(listItems.size){""}
        for(i in deliveryDurations.indices){
            deliveryDurations[i] = listItems[i].deliveryDuration
        }

        val durationInt = Array(deliveryDurations.size){0}
        for(i in durationInt.indices){
            val duration = deliveryDurations[i]
            val timeUnit = duration.substring(duration.indexOf(" ")+1)
            val time = duration.substringBefore(" ").toInt()
            when (timeUnit) {
                "Minutes" -> {
                    durationInt[i] = time
                }
                "Hours" -> {
                    durationInt[i] = time * 60
                }
                else -> {
                    durationInt[i] = time * 60 * 24
                }
            }
        }

        var max = 0
        for(duration in durationInt){
            if(duration > max) max = duration
        }

        var deliveryTime = "$max Minutes"

        if(max in 101..1439){
            max /= 60
            deliveryTime = "$max Hours"
        }
        else if(max > 1439){
            max /= 1440
            deliveryTime = "$max Days"
        }

        _deliveryTime.value = deliveryTime

        var billAmountTotal = 0
        var billTotalOriginal = 0
        for (item in listItems){
            billAmountTotal += item.price.toInt()
            billTotalOriginal += item.priceOriginal.toInt()
        }

        _billAmountTotal.value = billAmountTotal.toString()
        _billAmountTotalOriginal.value = billTotalOriginal.toString()

        _totalSavings.value = (billTotalOriginal - billAmountTotal).toString()


        if(billAmountTotal > 499){
            _deliveryCharge.value = "0"
        }
        else {
            _deliveryCharge.value = "40"
        }

        _billAmountWithDeliveryCharge.value = (_billAmountTotal.value!!.toInt() + _deliveryCharge.value!!.toInt()).toString()
        _paymentMethodd.value = paymentMethod

        _cartItems.value = listItems


        val date = Date(orderPlacedTime)
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
        simpleDateFormat.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
        _orderPlacedTime.value = simpleDateFormat.format(date)

        val dateP = Date(orderDeliveredOrCancelledTime)
        _orderDeliveredOrCancelledTime.value = simpleDateFormat.format(dateP)

        _status.value = status

    }

}