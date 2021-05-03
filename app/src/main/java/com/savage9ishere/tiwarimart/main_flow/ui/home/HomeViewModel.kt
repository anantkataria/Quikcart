package com.savage9ishere.tiwarimart.main_flow.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class HomeViewModel : ViewModel() {

    private val databaseRef = Firebase.database.reference

    private val _categories = MutableLiveData<List<Category?>>()
    val categories:LiveData<List<Category?>>
        get() =_categories

    private val categoriesList: ArrayList<Category?> = arrayListOf()


    init {
        databaseRef.child("categories").addChildEventListener(
            object: ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    categoriesList.add(snapshot.getValue(Category::class.java))
                    _categories.value = categoriesList
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    val updatedCategory = snapshot.getValue(Category::class.java)
                    for(index in categoriesList.indices){
                        if(categoriesList[index]!!.key == updatedCategory!!.key){
                            categoriesList.removeAt(index)
                            categoriesList.add(index, updatedCategory)
                        }
                    }
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    val category = snapshot.getValue(Category::class.java)
                    categoriesList.remove(category)
                    _categories.value = categoriesList
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