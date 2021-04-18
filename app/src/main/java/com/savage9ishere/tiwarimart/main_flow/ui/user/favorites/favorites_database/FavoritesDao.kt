package com.savage9ishere.tiwarimart.main_flow.ui.user.favorites.favorites_database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoritesDao {

    @Insert
    suspend fun insert(item : FavoritesEntity)

    @Update
    suspend fun update(item : FavoritesEntity)

    @Delete
    suspend fun delete(item : FavoritesEntity)

    @Query("SELECT * FROM favorite_items_table")
    fun getAllFavoriteItems() : LiveData<List<FavoritesEntity>>

    @Query("SELECT COUNT(*) FROM favorite_items_table WHERE databaseKey = :key")
    fun countInstance(key : String) : LiveData<Int?>

    @Query("DELETE FROM favorite_items_table WHERE databaseKey = :key")
    suspend fun deleteFromDatabase(key : String)
}