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
        itemsList.add(AboutItem("Vraj Soni", "https://firebasestorage.googleapis.com/v0/b/tiwari-mart.appspot.com/o/team%2Fvraj.jfif?alt=media&token=2736b293-5355-4404-968c-0d2909906126"))
        itemsList.add(AboutItem("Janvi Patel", "https://firebasestorage.googleapis.com/v0/b/tiwari-mart.appspot.com/o/team%2Fjanvi.jpg?alt=media&token=1e54782b-a78e-4bb4-9dd4-b3d4c8184f67"))
        itemsList.add(AboutItem("Rohin Nanavati", "https://firebasestorage.googleapis.com/v0/b/tiwari-mart.appspot.com/o/team%2Frohin.jpg?alt=media&token=5230f987-b9ff-4264-8a8a-e29182b7c740"))
        itemsList.add(AboutItem("Parita Patel", "https://firebasestorage.googleapis.com/v0/b/tiwari-mart.appspot.com/o/team%2Fparita.jpg?alt=media&token=367398cd-defa-46c7-864d-5d63bd74ece0"))
        itemsList.add(AboutItem("Parth Katrodiya", "https://firebasestorage.googleapis.com/v0/b/tiwari-mart.appspot.com/o/team%2Fparth.jpg?alt=media&token=2f5dd883-37de-4579-8665-04297012f2ea"))
        itemsList.add(AboutItem("Tanishq Nagpal", "https://firebasestorage.googleapis.com/v0/b/tiwari-mart.appspot.com/o/team%2Ftanishq.jpeg?alt=media&token=6d1cf772-64cd-4559-8517-84f80f62c4f5"))
        itemsList.add(AboutItem("Yagnik Vaghela", "https://firebasestorage.googleapis.com/v0/b/tiwari-mart.appspot.com/o/team%2Fyagnik.jpeg?alt=media&token=54d90377-4f67-4228-9207-5d36f19a4ec5"))
        itemsList.add(AboutItem("Smit Kumbhani", "https://firebasestorage.googleapis.com/v0/b/tiwari-mart.appspot.com/o/team%2Fsmit.jpeg?alt=media&token=f3e4cfec-c97a-46bf-be95-bb732e55ec71"))
        itemsList.add(AboutItem("Yaksh Talavia", "https://firebasestorage.googleapis.com/v0/b/tiwari-mart.appspot.com/o/team%2Fyaksh.jpeg?alt=media&token=8b618202-c095-4053-b570-e4f4d81dec1c"))

        _items.value = itemsList
    }
}

data class AboutItem(val name: String = "", val photo : String = "")