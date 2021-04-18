package com.savage9ishere.tiwarimart.main_flow.ui.user.favorites

import androidx.lifecycle.ViewModel
import com.savage9ishere.tiwarimart.main_flow.ui.user.favorites.favorites_database.FavoritesDao

class FavoritesViewModel(favoritesDatabase: FavoritesDao) : ViewModel() {

    val favoriteItems = favoritesDatabase.getAllFavoriteItems()

}