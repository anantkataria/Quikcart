package com.savage9ishere.tiwarimart.main_flow.ui.user.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.FtsOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.savage9ishere.tiwarimart.checkout.final_bill.OrderItem

class OrdersViewModel : ViewModel() {
    private val databaseRef = Firebase.database.reference
    private val auth = Firebase.auth
    private val user = auth.currentUser

    private val _currentItems = MutableLiveData<ArrayList<OrderItem>>()
    val currentItems : LiveData<ArrayList<OrderItem>>
        get() = _currentItems

    private val currentItemsList : ArrayList<OrderItem> = arrayListOf()

    private val _notLoggedIn = MutableLiveData<Boolean?>()
    val notLoggedIn : LiveData<Boolean?>
        get() = _notLoggedIn

    init {
        if (user!!.phoneNumber == null){
            //user not logged in
            _notLoggedIn.value = true
        }
        else {
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
                        TODO("Not yet implemented")
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
        }
    }

    fun doneNotLoggedIn() {
        _notLoggedIn.value = null
    }
}