package com.savage9ishere.tiwarimart.main_flow.ui.user.profile

import android.os.Parcelable
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.savage9ishere.tiwarimart.checkout.address.address_database.AddressDao
import kotlinx.parcelize.Parcelize

class ProfileViewModel(addressDatabase: AddressDao) : ViewModel() {

    private val databaseRef = Firebase.database.reference
    private val auth = Firebase.auth

    private val _notLoggedIn = MutableLiveData<Boolean?>()
    val notLoggedIn : LiveData<Boolean?>
        get() = _notLoggedIn

    private val _userName = MutableLiveData<String?>()
    val userName : LiveData<String?>
        get() = _userName

    private val _userPhone = MutableLiveData<String?>()
    val userPhone : LiveData<String?>
        get() = _userPhone

    private val _userEmail = MutableLiveData<String?>()
    val userEmail : LiveData<String?>
        get() = _userEmail

    val addresses = addressDatabase.getAllAddresses()

    private val _photoUrl = MutableLiveData<String?>()
    val photoUrl : LiveData<String?>
        get() = _photoUrl


    init {
        val user = auth.currentUser
        val phoneNumber = user!!.phoneNumber

        if (phoneNumber == null || phoneNumber.isEmpty()){
            _notLoggedIn.value = true
        }
        else {
            _notLoggedIn.value = false

            databaseRef.child("users").child(phoneNumber).addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        //user has filled his/her profile
                        val usr = snapshot.getValue(User::class.java)
                        _userName.value = usr!!.name
                        _userEmail.value = usr.email
                        _userPhone.value = usr.phoneNumber
                        _photoUrl.value = usr.photoUrl
                    }
                    else {
                        //user has not yet filled his/her profile
                        _userName.value = ""
                        _userPhone.value = ""
                        _userEmail.value = ""
                        _photoUrl.value = ""
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }

    }

    fun doneNotLoggedIn() {
        _notLoggedIn.value = null
    }

    private val _changesSaved = MutableLiveData<Boolean?>()
    val changesSaved : LiveData<Boolean?>
        get() = _changesSaved

    fun storeUserInDatabase(imgUrl: String, name: String, phone: String, email: String) {
        val authPhone = Firebase.auth.currentUser!!.phoneNumber
        val storageRef = Firebase.storage.reference
        if(imgUrl.isNotEmpty()){
            val userRef = storageRef.child("user").child(authPhone!!)


                userRef.putFile(imgUrl.toUri()).continueWithTask {
                task -> if(!task.isSuccessful){
                    task.exception?.let {
                        throw it
                    }
                }
                userRef.downloadUrl
            }.addOnCompleteListener {
                if (it.isSuccessful){
                    val downloadUrl = it.result.toString()
                    val user = User(name, phone, email, downloadUrl)
                    databaseRef.child("users").child(authPhone).setValue(user).addOnCompleteListener { itt->
                        _changesSaved.value = itt.isSuccessful
                    }
                }
            }
        }
        else {
            val user = User(name, phone, email, "")
            databaseRef.child("users").child(authPhone!!).setValue(user).addOnCompleteListener {
                _changesSaved.value = it.isSuccessful
            }
        }

    }

    fun doneSavingChanges(){
        _changesSaved.value = null
    }


}

@Parcelize
data class User(val name : String = "", val phoneNumber : String = "", val email : String = "", val photoUrl : String = "") : Parcelable