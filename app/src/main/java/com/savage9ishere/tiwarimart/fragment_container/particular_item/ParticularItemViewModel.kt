package com.savage9ishere.tiwarimart.fragment_container.particular_item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.savage9ishere.tiwarimart.main_flow.ui.cart.cart_items_database.CartItemDao
import com.savage9ishere.tiwarimart.main_flow.ui.cart.cart_items_database.CartItemEntity
import com.savage9ishere.tiwarimart.main_flow.ui.home.Item
import com.savage9ishere.tiwarimart.main_flow.ui.home.Review
import kotlinx.coroutines.launch

private var quantity = 0

class ParticularItemViewModel(val item: Item, private val database: CartItemDao, val categoryName: String) : ViewModel() {

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

    //todo setting spinner for size

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

    //todo setting spinner for qty
    //todo setting for address
    //todo secure transaction setting

    private val _itemDescription = MutableLiveData<String>()
    val itemDescription: LiveData<String>
        get() = _itemDescription

    private val _itemReviews = MutableLiveData<ArrayList<Review>>()
    val itemReviews : LiveData<ArrayList<Review>>
        get() = _itemReviews

    private val _doneInserting = MutableLiveData<Boolean?>()
    val doneInserting : LiveData<Boolean?>
        get() = _doneInserting

    init {

        setItemNameAndPriceToUi()

        val totalRating = item.ratingTotal.toFloat()
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
        itemSize = item.size,
        itemQty = quantity,
        stockAvailability = item.inStock,
        photoUrl = item.photosUrl[0],
        itemKey = item.key,
        itemCategory = categoryName)

        viewModelScope.launch {
            database.insert(cartItem)
            _doneInserting.value = true
        }
    }

    fun doneDoneInserting() {
        _doneInserting.value = null
    }
}