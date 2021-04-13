package com.savage9ishere.tiwarimart.main_flow.ui.user.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.savage9ishere.tiwarimart.checkout.final_bill.OrderItem
import com.savage9ishere.tiwarimart.main_flow.ui.user.orders.previous_orders.PreviousOrderDao
import com.savage9ishere.tiwarimart.main_flow.ui.user.orders.previous_orders.PreviousOrderEntity
import kotlinx.coroutines.launch

class OrdersViewModel(previousOrdersDatabase: PreviousOrderDao) : ViewModel() {
    private val databaseRef = Firebase.database.reference
    private val auth = Firebase.auth
    private val user = auth.currentUser

    private val _currentItems = MutableLiveData<ArrayList<OrderItem>>()
    val currentItems : LiveData<ArrayList<OrderItem>>
        get() = _currentItems

    private val currentItemsList : ArrayList<OrderItem> = arrayListOf()

    val previousOrderItems = previousOrdersDatabase.getAllPreviousOrders()

    private val _notLoggedIn = MutableLiveData<Boolean?>()
    val notLoggedIn : LiveData<Boolean?>
        get() = _notLoggedIn

    init {
        if (user!!.phoneNumber == null || user.phoneNumber!!.isEmpty()){
            //user not logged in
            _notLoggedIn.value = true
        }
        else {

            _notLoggedIn.value = false

            databaseRef.child("orders").child(user.phoneNumber!!).addChildEventListener(
                object  : ChildEventListener {
                    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                        val order = snapshot.getValue(OrderItem::class.java)
                        currentItemsList.add(order!!)
                        _currentItems.value = currentItemsList
                    }

                    override fun onChildChanged(
                        snapshot: DataSnapshot,
                        previousChildName: String?
                    ) {
                        val order = snapshot.getValue(OrderItem::class.java)
                        for (i in currentItemsList.indices){
                            val order2 = currentItemsList[i]
                            if(order2.orderKey == order!!.orderKey){
                                currentItemsList.removeAt(i)
                                currentItemsList.add(i, order)
                            }
                        }
                        _currentItems.value = currentItemsList
                    }

                    override fun onChildRemoved(snapshot: DataSnapshot) {
                        val order = snapshot.getValue(OrderItem::class.java)
                        currentItemsList.remove(order)
                        _currentItems.value = currentItemsList
                    }

                    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                        TODO("Not yet implemented")
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                }
            )

            databaseRef.child("deliveredOrCancelled").child(user.phoneNumber!!).addChildEventListener(
                object : ChildEventListener {
                    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                        val order = snapshot.getValue(OrderItem::class.java)
                        val previousOrder = PreviousOrderEntity(address = order!!.address, authPhone = order.authPhone, listItems = order.listItems, orderDeliveredOrCancelledTime = order.orderDeliveredOrCancelledTime, orderKey = order.orderKey, orderPlacedTime = order.orderPlacedTime, paymentMethod = order.paymentMethod, status = order.status)

                        viewModelScope.launch {
                            previousOrdersDatabase.insert(previousOrder)
                        }

                        databaseRef.child("deliveredOrCancelled").child(user.phoneNumber!!).child(snapshot.key!!).removeValue()
                    }

                    override fun onChildChanged(
                        snapshot: DataSnapshot,
                        previousChildName: String?
                    ) {
                    }

                    override fun onChildRemoved(snapshot: DataSnapshot) {
                    }

                    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                }
            )


        }
    }

    fun doneNotLoggedIn() {
        _notLoggedIn.value = null
    }
}