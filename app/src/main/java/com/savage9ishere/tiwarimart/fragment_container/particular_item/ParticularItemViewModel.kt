package com.savage9ishere.tiwarimart.fragment_container.particular_item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
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

class ParticularItemViewModel(
    val item: Item,
    private val database: CartItemDao,
    private val categoryName: String,
    private val favoriteDataSource: FavoritesDao
) : ViewModel() {

    private val databaseRef = Firebase.database.reference

    val itemCountInFavorite = favoriteDataSource.countInstance(item.key!!)

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

    init {

        setItemNameAndPriceToUi()

        val totalRating = item.ratingTotal
        val peopleRatingCount = item.peopleRatingCount
        if(peopleRatingCount != 0L) {
            _rating.value = totalRating/peopleRatingCount.toFloat()
        }

        _peopleRatingCount.value = peopleRatingCount

        _itemImages.value = item.photosUrl

        _deliveryTimeText.value = item.deliveryDuration

        if(item.inStock){
            _stockAvailability.value = "In Stock."
        }
        else {
            _stockAvailability.value = "Out of Stock."
        }

        _itemDescription.value = item.description

        databaseRef.child("reviews").child(categoryName).child(item.key!!).addChildEventListener(object : ChildEventListener{
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

    private fun setItemNameAndPriceToUi() {
        _itemName.value = item.name + " " + item.size

        val originalPrice = item.price.toInt()
        val discount = item.discount.toInt()
        val savePrice = (originalPrice*discount/100)
        val price = originalPrice - savePrice

        _itemPrice.value = price.toString()

        _mrpPrice.value = originalPrice.toString()

        _savePrice.value = savePrice.toString()

    }

    fun setNewSize(size: String) {
        item.size = size
        item.price = item.otherSizes.getValue(size)

        setItemNameAndPriceToUi()

    }

    fun setQuantity(qty : Int){
        quantity = qty
    }

    fun addToCart(){

        val originalPrice = item.price.toInt()
        val discount = item.discount.toInt()
        val price = originalPrice - (originalPrice * discount / 100)

        val cartItem = CartItemEntity(itemName = item.name + " " + item.size,
        itemPrice = price,
        itemPriceOriginal = originalPrice,
        itemSize = item.size,
        itemQty = quantity,
        stockAvailability = item.inStock,
        photoUrl = item.photosUrl[0],
        itemKey = item.key,
        itemCategory = categoryName,
        deliveryDuration = item.deliveryDuration)

        viewModelScope.launch {
            database.insert(cartItem)
            _doneInserting.value = true
        }
    }

    fun doneDoneInserting() {
        _doneInserting.value = null
    }

    fun moveAheadToBuyItem() {
        val originalPrice = item.price.toInt()
        val discountPrice = originalPrice - (originalPrice*item.discount.toInt()/100)
        val cartItem = CartItems(item.name, item.size, discountPrice.toString(), originalPrice.toString(), quantity, item.photosUrl[0], item.key, categoryName, item.deliveryDuration)
        _moveAheadToBuy.value = cartItem
    }

    fun doneMovingAhead() {
        _moveAheadToBuy.value = null
    }

    fun handleFavoriteClick() {
        if (itemCountInFavorite.value == 0){
            val favoriteItem = FavoritesEntity(categoryName = categoryName, itemName = item.name, size = item.size, price = item.price, discount = item.discount, deliveryDuration = item.deliveryDuration, photoUrl = item.photosUrl[0], databaseKey = item.key, ratingTotal = item.ratingTotal, peopleRatingCount = item.peopleRatingCount, inStock = item.inStock)
            viewModelScope.launch {
                favoriteDataSource.insert(favoriteItem)
            }
        }
        else if (itemCountInFavorite.value == 1) {
            viewModelScope.launch {
                favoriteDataSource.deleteFromDatabase(item.key!!)
            }
        }
    }
}