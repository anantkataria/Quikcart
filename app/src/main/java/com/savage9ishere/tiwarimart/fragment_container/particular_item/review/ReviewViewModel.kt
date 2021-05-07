package com.savage9ishere.tiwarimart.fragment_container.particular_item.review

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
import com.savage9ishere.tiwarimart.main_flow.ui.home.Item
import com.savage9ishere.tiwarimart.main_flow.ui.home.Review
import com.savage9ishere.tiwarimart.main_flow.ui.user.profile.User

class ReviewViewModel(val item: Item, val categoryName: String) : ViewModel() {

    private val databaseRef = Firebase.database.reference
    private val storageRef = Firebase.storage.reference

    private val _itemImage = MutableLiveData<String?>()
    val itemImage: LiveData<String?>
        get() = _itemImage

    private val _itemName = MutableLiveData<String?>()
    val itemName: LiveData<String?>
        get() = _itemName

    private val _itemPhotos = MutableLiveData<ArrayList<String>>()
    val itemPhotos: LiveData<ArrayList<String>>
        get() = _itemPhotos

    private val itemPhotosList: ArrayList<String> = arrayListOf()

    init {
        _itemImage.value = item.photosUrl[0]
        _itemName.value = item.name
    }

    fun removeItem(it: Int) {
        itemPhotosList.removeAt(it)
        _itemPhotos.value = itemPhotosList
    }

    fun addPhotoToArrayList(string: String) {
        itemPhotosList.add(string)
        _itemPhotos.value = itemPhotosList
    }

    fun addNewReview(review: String, title: String, rating: Float) {
        //first add photos to storage, get their url
        //and store url with other review docs

        //add one to peopleRatingcount
        //and add this rating to total rating in the current item
        if (itemPhotosList.isNotEmpty()){
            uploadImages(ArrayList(), itemPhotosList, review, title, rating, categoryName)
        }
        else {
            uploadToDatabase(arrayListOf(), review, title, rating, categoryName)
        }


    }

    private val _uploadComplete = MutableLiveData<Boolean?>()
    val uploadComplete :  LiveData<Boolean?>
        get() = _uploadComplete

    private fun uploadImages(
        arrayList: ArrayList<String>,
        itemPhotosList: ArrayList<String>,
        review: String,
        title: String,
        rating: Float,
        categoryName: String
    ) {
        val uri = itemPhotosList[arrayList.size].toUri()
        val uriPaths = uri.pathSegments
        val lastSegment = uriPaths[uriPaths.size - 1]
        val trulyLastSegment = lastSegment.substringAfterLast("/")
        //Log.e("91", "trulyLastSegment = $trulyLastSegment")
        val currentImageRef =
            storageRef.child("review_images").child(categoryName).child(item.key!!)
                .child(trulyLastSegment)

        currentImageRef.putFile(uri).continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            currentImageRef.downloadUrl
        }.addOnCompleteListener {
            if (it.isSuccessful){
                val downloadUrl = it.result.toString()
                arrayList.add(downloadUrl)

                if (arrayList.size == itemPhotosList.size){
                    uploadToDatabase(arrayList, review, title, rating, categoryName)
                }
                else {
                    uploadImages(arrayList, itemPhotosList, review, title, rating, categoryName)
                }

            }
            else {
                _uploadComplete.value = false
            }
        }
    }

    private fun uploadToDatabase(arrayList: ArrayList<String>, review: String, title: String, rating: Float, categoryName: String) {
        val reviewReference = databaseRef.child("reviews").child(categoryName).child(item.key!!)
        val key = reviewReference.push().key ?: return

        val auth = Firebase.auth
        val user = auth.currentUser
        val phone = user!!.phoneNumber

        databaseRef.child("users").child(phone!!).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var userr : User? = null
                if (snapshot.exists()){
                    userr = snapshot.getValue(User::class.java)
                }
                else {
                    userr = User("Anonymous", "Nil", "Nil")
                }

                val revieww = Review(userr!!, arrayList, review, title, rating, 0, key)

                val map = hashMapOf(
                    "/reviews/$categoryName/${item.key}/$key" to revieww,
                    "/categoryWiseItems/$categoryName/${item.key}/peopleRatingCount" to item.peopleRatingCount + 1,
                    "/categoryWiseItems/$categoryName/${item.key}/ratingTotal" to item.ratingTotal + rating
                )

                databaseRef.updateChildren(map).addOnCompleteListener {
                    _uploadComplete.value = it.isSuccessful
                }

            }

            override fun onCancelled(error: DatabaseError) {
                _uploadComplete.value = false
            }

        })
    }

    fun doneUploadComplete() {
        _uploadComplete.value = null
    }

    fun donePuttingImage() {
        _itemImage.value = null
    }

}