package com.savage9ishere.tiwarimart.main_flow.ui.user.profile

import android.net.Uri
import android.os.Parcelable
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.savage9ishere.tiwarimart.checkout.address.address_database.AddressDao
import com.savage9ishere.tiwarimart.checkout.address.address_database.AddressEntity
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

class ProfileViewModel(private val addressDatabase: AddressDao) : ViewModel() {

    private val databaseRef = Firebase.database.reference
    private val auth = Firebase.auth

    private val _notLoggedIn = MutableLiveData<Boolean?>()
    val notLoggedIn: LiveData<Boolean?>
        get() = _notLoggedIn

    private val _userName = MutableLiveData<String?>()
    val userName: LiveData<String?>
        get() = _userName

    private val _userPhone = MutableLiveData<String?>()
    val userPhone: LiveData<String?>
        get() = _userPhone

    private val _userEmail = MutableLiveData<String?>()
    val userEmail: LiveData<String?>
        get() = _userEmail

    val addresses = addressDatabase.getAllAddresses()

    private val _photoUrl = MutableLiveData<String?>()
    val photoUrl: LiveData<String?>
        get() = _photoUrl

    private val _photoUrlSaved = MutableLiveData<Boolean?>()
    val photoUrlSaved: LiveData<Boolean?>
        get() = _photoUrlSaved

    private val _photoUrlDeleted = MutableLiveData<Boolean?>()
    val photoUrlDeleted : LiveData<Boolean?>
        get() = _photoUrlDeleted


    init {
        val user = auth.currentUser
        val phoneNumber = user!!.phoneNumber

        if (phoneNumber == null || phoneNumber.isEmpty()) {
            _notLoggedIn.value = true
        } else {
            _notLoggedIn.value = false

            databaseRef.child("users").child(phoneNumber)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            //user has filled his/her profile
                            val usr = snapshot.getValue(User::class.java)
                            _userName.value = usr!!.name
                            _userEmail.value = usr.email
                            _userPhone.value = usr.phoneNumber
                            _photoUrl.value = usr.photoUrl
                        } else {
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
    val changesSaved: LiveData<Boolean?>
        get() = _changesSaved

    fun storeUserInDatabase(name: String, phone: String, email: String) {
        val authPhone = Firebase.auth.currentUser!!.phoneNumber

        val user = User(name, phone, email, _photoUrl.value!!)
        databaseRef.child("users").child(authPhone!!).setValue(user)
            .addOnCompleteListener { itt ->
                _changesSaved.value = itt.isSuccessful
            }
    }

    fun doneSavingChanges() {
        _changesSaved.value = null
    }

    fun addPhotoToDatabase(imageUri: Uri?) {
        val storageRef = Firebase.storage.reference
        val authPhone = Firebase.auth.currentUser!!.phoneNumber

        val userRef = storageRef.child("users").child(authPhone!!)

        userRef.putFile(imageUri!!).continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            userRef.downloadUrl
        }.addOnCompleteListener {
            if (it.isSuccessful) {
                val downloadUrl = it.result.toString()
                databaseRef.child("users").child(authPhone).child("photoUrl").setValue(downloadUrl)
                    .addOnCompleteListener { itt ->
                        _photoUrlSaved.value = itt.isSuccessful
                    }
            }
        }
    }

    fun donePhotoUrlSaved() {
        _photoUrlSaved.value = null
    }

    fun removeImageFromStorage(imageUri: Uri) {
        val storageRef = Firebase.storage.reference
        val authPhone = Firebase.auth.currentUser!!.phoneNumber

        storageRef.child("users").child(authPhone!!).delete().addOnCompleteListener {
            if (it.isSuccessful){
                databaseRef.child("users").child(authPhone).child("photoUrl").setValue("").addOnCompleteListener { itt ->
                    _photoUrlDeleted.value = it.isSuccessful
                }
            }
        }
    }

    fun donePhotoUrlDeleted() {
        _photoUrlDeleted.value = null
    }

    fun deleteThisAddress(addressEntity: AddressEntity) {
        viewModelScope.launch {
            addressDatabase.delete(addressEntity)
        }
    }
}

@Parcelize
data class User(
    val name: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val photoUrl: String = ""
) : Parcelable