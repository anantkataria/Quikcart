package com.savage9ishere.tiwarimart.main_flow.ui.user.favorites.on_favorite_click

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.savage9ishere.tiwarimart.main_flow.ui.cart.cart_items_database.CartItemDao
import com.savage9ishere.tiwarimart.main_flow.ui.cart.cart_items_database.CartItemEntity
import com.savage9ishere.tiwarimart.main_flow.ui.home.CartItems
import com.savage9ishere.tiwarimart.main_flow.ui.home.Item
import com.savage9ishere.tiwarimart.main_flow.ui.home.Review
import com.savage9ishere.tiwarimart.main_flow.ui.user.favorites.favorites_database.FavoritesDao
import com.savage9ishere.tiwarimart.main_flow.ui.user.favorites.favorites_database.FavoritesEntity
import kotlinx.coroutines.launch

private var quantity = 0

class LoadItemDataViewModel(
    val categoryName: String,
    private val databaseKey: String,
    private val cartDatabase: CartItemDao,
    private val favoritesDatabase: FavoritesDao
) : ViewModel() {
    private val databaseRef = Firebase.database.reference

//    private var thisItem : Item? = null
    private val thisItem = MutableLiveData<Item>()
    val thisItemPublic : LiveData<Item>
        get() = thisItem

    val itemCountInFavorite = favoritesDatabase.countInstance(databaseKey)

    private val _moveAheadToBuy = MutableLiveData<CartItems?>()
    val moveAheadToBuy : LiveData<CartItems?>
        get() = _moveAheadToBuy

    private val _itemName = MutableLiveData<String>()
    val itemName : LiveData<String>
        get() = _itemName

    private val _rating = MutableLiveData<Float>()
    val rating : LiveData<Float>
        get() = _rating

    private val _peopleRatingCount = MutableLiveData<Long>()
    val peopleRatingCount : LiveData<Long>
        get() = _peopleRatingCount

    private val _itemImages = MutableLiveData<ArrayList<String>>()
    val itemImages : LiveData<ArrayList<String>>
        get() = _itemImages

    private val _itemPrice = MutableLiveData<String>()
    val itemPrice : LiveData<String>
        get() = _itemPrice

    private val _mrpPrice = MutableLiveData<String>()
    val mrpPrice: LiveData<String>
        get() = _mrpPrice

    private val _savePrice = MutableLiveData<String>()
    val savePrice: LiveData<String>
        get() = _savePrice

    private val _deliveryTimeText = MutableLiveData<String>()
    val deliveryTimeText: LiveData<String>
        get() = _deliveryTimeText

    private val _stockAvailability = MutableLiveData<String>()
    val stockAvailability: LiveData<String>
        get() = _stockAvailability


    private val _itemDescription = MutableLiveData<String>()
    val itemDescription: LiveData<String>
        get() = _itemDescription

    private val _itemReviews = MutableLiveData<ArrayList<Review>>()
    val itemReviews : LiveData<ArrayList<Review>>
        get() = _itemReviews

    val reviewList : ArrayList<Review> = arrayListOf()

    private val _doneInserting = MutableLiveData<Boolean?>()
    val doneInserting : LiveData<Boolean?>
        get() = _doneInserting

    private val _otherSizes = MutableLiveData<List<String>>()
    val otherSizes : LiveData<List<String>>
        get() = _otherSizes

    init {

        fetchFromDatabase(object : AfterFetchInterface {
            override fun afterFetch(item: Item) {
                thisItem.value = item

                showItem()

                viewModelScope.launch {
                    val favoriteItem = FavoritesEntity(categoryName = categoryName, itemName = item.name, size = item.size, price = item.price, discount = item.discount, deliveryDuration = item.deliveryDuration, photoUrl = item.photosUrl[0], databaseKey = item.key, ratingTotal = item.ratingTotal, peopleRatingCount = item.peopleRatingCount, inStock = item.inStock)
                    favoritesDatabase.update(favoriteItem)
                }
            }

        })


        databaseRef.child("reviews").child(categoryName).child(databaseKey).addChildEventListener(object :
            ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val review = snapshot.getValue(Review::class.java)
                reviewList.add(review!!)
                _itemReviews.value = reviewList
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private interface AfterFetchInterface {
        fun afterFetch(item : Item)
    }

    private fun fetchFromDatabase(afterFetchInterface : AfterFetchInterface){
        databaseRef.child("categoryWiseItems").child(categoryName).child(databaseKey).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val item : Item  = snapshot.getValue(Item::class.java)!!
//                showItem()
                    afterFetchInterface.afterFetch(item)
                }
                else {
                    Log.e("696969", "Snapshot does not exist")
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun showItem() {
        setItemNameAndPriceToUi()

        val totalRating = thisItem.value!!.ratingTotal
        val peopleRatingCount = thisItem.value!!.peopleRatingCount
        if(peopleRatingCount != 0L) {
            _rating.value = totalRating/peopleRatingCount.toFloat()
        }

        _peopleRatingCount.value = peopleRatingCount

        _itemImages.value = thisItem.value!!.photosUrl

        _deliveryTimeText.value = thisItem.value!!.deliveryDuration

        if(thisItem.value!!.inStock){
            _stockAvailability.value = "In Stock."
        }
        else {
            _stockAvailability.value = "Out of Stock."
        }

        _itemDescription.value = thisItem.value!!.description

        _otherSizes.value = thisItem.value!!.otherSizes.keys.toList()

    }

    private fun setItemNameAndPriceToUi() {
        _itemName.value = thisItem.value!!.name + " " + thisItem.value!!.size

        val originalPrice = thisItem.value!!.price.toInt()
        val discount = thisItem.value!!.discount.toInt()
        val savePrice = (originalPrice*discount/100)
        val price = originalPrice - savePrice

        _itemPrice.value = price.toString()

        _mrpPrice.value = originalPrice.toString()

        _savePrice.value = savePrice.toString()
    }

    fun setNewSize(size: String) {
        thisItem.value!!.size = size
        thisItem.value!!.price = thisItem.value!!.otherSizes.getValue(size)

        setItemNameAndPriceToUi()

    }

    fun setQuantity(qty : Int){
        quantity = qty
    }

    fun addToCart(){

        val originalPrice = thisItem.value!!.price.toInt()
        val discount = thisItem.value!!.discount.toInt()
        val price = originalPrice - (originalPrice * discount / 100)

        val cartItem = CartItemEntity(itemName = thisItem.value!!.name + " " + thisItem.value!!.size,
            itemPrice = price,
            itemPriceOriginal = originalPrice,
            itemSize = thisItem.value!!.size,
            itemQty = quantity,
            stockAvailability = thisItem.value!!.inStock,
            photoUrl = thisItem.value!!.photosUrl[0],
            itemKey = thisItem.value!!.key,
            itemCategory = categoryName,
            deliveryDuration = thisItem.value!!.deliveryDuration)

        viewModelScope.launch {
            cartDatabase.insert(cartItem)
            _doneInserting.value = true
        }
    }

    fun doneDoneInserting() {
        _doneInserting.value = null
    }

    fun moveAheadToBuyItem() {
        val originalPrice = thisItem.value!!.price.toInt()
        val discountPrice = originalPrice - (originalPrice*thisItem.value!!.discount.toInt()/100)
        val cartItem = CartItems(thisItem.value!!.name, thisItem.value!!.size, discountPrice.toString(), originalPrice.toString(), quantity, thisItem.value!!.photosUrl[0], thisItem.value!!.key, categoryName, thisItem.value!!.deliveryDuration)
        _moveAheadToBuy.value = cartItem
    }

    fun doneMovingAhead() {
        _moveAheadToBuy.value = null
    }

    fun handleFavoriteClick() {
        if (itemCountInFavorite.value == 0){
            val favoriteItem = FavoritesEntity(categoryName = categoryName, itemName = thisItem.value!!.name, size = thisItem.value!!.size, price = thisItem.value!!.price, discount = thisItem.value!!.discount, deliveryDuration = thisItem.value!!.deliveryDuration, photoUrl = thisItem.value!!.photosUrl[0], databaseKey = thisItem.value!!.key, ratingTotal = thisItem.value!!.ratingTotal, peopleRatingCount = thisItem.value!!.peopleRatingCount, inStock = thisItem.value!!.inStock)
            viewModelScope.launch {
                favoritesDatabase.insert(favoriteItem)
            }
        }
        else if (itemCountInFavorite.value == 1) {
            viewModelScope.launch {
                favoritesDatabase.deleteFromDatabase(thisItem.value!!.key!!)
            }
        }
    }

}