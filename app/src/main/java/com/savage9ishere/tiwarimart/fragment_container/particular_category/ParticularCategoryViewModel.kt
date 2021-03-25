package com.savage9ishere.tiwarimart.fragment_container.particular_category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.savage9ishere.tiwarimart.main_flow.ui.home.Item

class ParticularCategoryViewModel(categoryName  : String) : ViewModel() {
    private val databaseRef = Firebase.database.reference

    private val _items = MutableLiveData<ArrayList<Item?>>()
    val items : LiveData<ArrayList<Item?>>
        get() = _items

    private val itemsList : ArrayList<Item?> = arrayListOf()

    init {
        databaseRef.child("categoryWiseItems").child(categoryName).addChildEventListener(
            object : ChildEventListener{
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    itemsList.add(snapshot.getValue(Item::class.java))
                    _items.value = itemsList
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    val item = snapshot.getValue(Item::class.java)
                    itemsList.remove(item)
                    _items.value = itemsList
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