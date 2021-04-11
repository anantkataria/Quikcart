package com.savage9ishere.tiwarimart.main_flow.ui.user.about

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AboutViewModel : ViewModel() {
    private val itemsList : ArrayList<AboutItem> = arrayListOf()

    private val _items = MutableLiveData<ArrayList<AboutItem>>()
    val items : LiveData<ArrayList<AboutItem>>
        get() = _items

    init {
        itemsList.add(AboutItem("Anant Kataria", "https://firebasestorage.googleapis.com/v0/b/tiwari-mart.appspot.com/o/team%2FAnant.jpg?alt=media&token=8c614ab0-c4c0-4d77-8a4d-59f9a67a8f96"))
        itemsList.add(AboutItem("Vraj Soni", "https://firebasestorage.googleapis.com/v0/b/tiwari-mart.appspot.com/o/team%2FAnant.jpg?alt=media&token=8c614ab0-c4c0-4d77-8a4d-59f9a67a8f96"))
        itemsList.add(AboutItem("Janvi Patel", "https://firebasestorage.googleapis.com/v0/b/tiwari-mart.appspot.com/o/team%2FAnant.jpg?alt=media&token=8c614ab0-c4c0-4d77-8a4d-59f9a67a8f96"))
        itemsList.add(AboutItem("Rohin Nanavati", "https://firebasestorage.googleapis.com/v0/b/tiwari-mart.appspot.com/o/team%2FAnant.jpg?alt=media&token=8c614ab0-c4c0-4d77-8a4d-59f9a67a8f96"))
        itemsList.add(AboutItem("Parita Patel", "https://firebasestorage.googleapis.com/v0/b/tiwari-mart.appspot.com/o/team%2FAnant.jpg?alt=media&token=8c614ab0-c4c0-4d77-8a4d-59f9a67a8f96"))
        itemsList.add(AboutItem("Parth Katrodiya", "https://firebasestorage.googleapis.com/v0/b/tiwari-mart.appspot.com/o/team%2FAnant.jpg?alt=media&token=8c614ab0-c4c0-4d77-8a4d-59f9a67a8f96"))
        itemsList.add(AboutItem("Tanishq Nagpal", "https://firebasestorage.googleapis.com/v0/b/tiwari-mart.appspot.com/o/team%2FAnant.jpg?alt=media&token=8c614ab0-c4c0-4d77-8a4d-59f9a67a8f96"))
        itemsList.add(AboutItem("Yagnik Vaghela", "https://firebasestorage.googleapis.com/v0/b/tiwari-mart.appspot.com/o/team%2FAnant.jpg?alt=media&token=8c614ab0-c4c0-4d77-8a4d-59f9a67a8f96"))
        itemsList.add(AboutItem("Smit Kumbhani", "https://firebasestorage.googleapis.com/v0/b/tiwari-mart.appspot.com/o/team%2FAnant.jpg?alt=media&token=8c614ab0-c4c0-4d77-8a4d-59f9a67a8f96"))
        itemsList.add(AboutItem("Yaksh Talavia", "https://firebasestorage.googleapis.com/v0/b/tiwari-mart.appspot.com/o/team%2FAnant.jpg?alt=media&token=8c614ab0-c4c0-4d77-8a4d-59f9a67a8f96"))

        _items.value = itemsList
    }
}

data class AboutItem(val name: String = "", val photo : String = "")