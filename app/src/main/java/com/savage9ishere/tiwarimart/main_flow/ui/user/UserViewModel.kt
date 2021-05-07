package com.savage9ishere.tiwarimart.main_flow.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.savage9ishere.tiwarimart.R


class UserViewModel : ViewModel() {

    private var listItems : ArrayList<ListItem> = arrayListOf()

    private val _listItemsData = MutableLiveData<ArrayList<ListItem>>()
    val listItemData : LiveData<ArrayList<ListItem>>
        get() = _listItemsData

    init {

        listItems.add(ListItem(R.drawable.ic_profile, "Profile", "Name, Address, Phone number..."))
        listItems.add(ListItem(R.drawable.ic_orders, "Orders", "Current pending and Previous orders..."))
        listItems.add(ListItem(R.drawable.ic_games, "Games", "Play games and win Exciting offers..."))
        listItems.add(ListItem(R.drawable.ic_feedback, "Feedback", "Write us your doubts, suggestions, complaints..."))
        listItems.add(ListItem(R.drawable.ic_favorites, "Favorites", "All your favorite items collection..."))
        listItems.add(ListItem(R.drawable.ic_customer_support, "Support", "Always here to help you, contact any time..."))
        listItems.add(ListItem(R.drawable.ic_about, "About", "Who are we..."))
//        listItems.add(ListItem(R.drawable.ic_logout, "Logout", "logout from your account"))

        _listItemsData.value = listItems
    }

}

data class ListItem(val image : Int = 0, val name : String = "", val description : String = "")