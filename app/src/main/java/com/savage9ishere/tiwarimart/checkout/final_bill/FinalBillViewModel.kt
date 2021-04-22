package com.savage9ishere.tiwarimart.checkout.final_bill

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.savage9ishere.tiwarimart.main_flow.ui.cart.cart_items_database.CartItemDao
import com.savage9ishere.tiwarimart.main_flow.ui.home.AddressItem
import com.savage9ishere.tiwarimart.main_flow.ui.home.CartItems
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

class FinalBillViewModel(
    private val listItems: ArrayList<CartItems>,
    val address: AddressItem,
    private val paymentMethod: String,
    private val cartDatabase: CartItemDao
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

    init {
        val address = address.getAddress()
        _itemAddress.value = address

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
            billAmountTotal += item.price.toInt()*item.qty
            billTotalOriginal += item.priceOriginal.toInt()*item.qty
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

    }

    private val _orderPlacedSuccessfully = MutableLiveData<Boolean?>()
    val orderPlacedSuccessfully : LiveData<Boolean?>
        get() = _orderPlacedSuccessfully

    fun placeOrder() {
        val databaseRef = Firebase.database.reference
        val auth = Firebase.auth
        val user = auth.currentUser
        val phoneNumber = user!!.phoneNumber

        val orderRef = databaseRef.child("orders").child(phoneNumber!!)
        val key = orderRef.push().key ?: return

        val item = OrderItem(listItems, address, paymentMethod, key, System.currentTimeMillis(), 0L, "ORDER PROCESSING", user.phoneNumber!!)

        orderRef.child(key).setValue(item).addOnCompleteListener {
            viewModelScope.launch {
                cartDatabase.deleteAllItems()
            }
            _orderPlacedSuccessfully.value = it.isSuccessful
        }
    }

    fun doneOrderPlaced() {
        _orderPlacedSuccessfully.value = null
    }

}

@Parcelize
data class OrderItem(val listItems: ArrayList<CartItems> = arrayListOf(), val address: AddressItem = AddressItem(), val paymentMethod : String = "", val orderKey : String = "", val orderPlacedTime : Long = 0L, var orderDeliveredOrCancelledTime : Long = 0L, var status : String = "", val authPhone : String = "") : Parcelable