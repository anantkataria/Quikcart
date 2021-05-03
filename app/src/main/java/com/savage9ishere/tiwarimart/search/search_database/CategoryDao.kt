package com.savage9ishere.tiwarimart.search.search_database

import androidx.core.provider.FontsContractCompat
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item : CategoryEntity)

    @Update
    suspend fun update(item : CategoryEntity)

    @Delete
    suspend fun delete(item : CategoryEntity)

    @Query("DELETE FROM categories_table")
    suspend fun deleteAllCategories()

    @Query("SELECT * FROM categories_table ORDER BY category_id")
    fun getAllCategories() : LiveData<List<CategoryEntity>>

    @Query("SELECT category_name FROM categories_table ORDER BY category_name")
    fun getAllCategoriesNames() : LiveData<List<String>>

    @Query("SELECT category_name FROM categories_table WHERE category_name LIKE :query ORDER BY category_name")
    fun getCategoryByName(query : String): LiveData<List<String>>
}