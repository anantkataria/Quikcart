package com.savage9ishere.tiwarimart.search.search_database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface CategoryWiseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CategoryWiseEntity)

    @Update
    suspend fun update(item: CategoryWiseEntity)

    @Delete
    suspend fun delete(item: CategoryWiseEntity)

    @Query("DELETE FROM category_wise_items_table")
    suspend fun deleteAllItems()

    @Query("SELECT * FROM category_wise_items_table")
    fun getAllItems() : LiveData<List<CategoryWiseEntity>>

    @Query("SELECT item_name FROM category_wise_items_table WHERE item_name LIKE :query ORDER BY item_name")
    fun getItemNamesByName(query : String) : LiveData<List<String>>

    @Query("DELETE FROM category_wise_items_table WHERE category_of_item = :name")
    suspend fun deleteCategoryByName(name : String)

    @RawQuery(observedEntities = [CategoryWiseEntity::class])
    fun getProductsFromDatabase(query: SupportSQLiteQuery) : LiveData<List<CategoryWiseEntity>>

    @RawQuery(observedEntities = [CategoryWiseEntity::class])
    fun sortProducts(query: SupportSQLiteQuery) : LiveData<List<CategoryWiseEntity>>

    @RawQuery(observedEntities = [CategoryWiseEntity::class])
    fun filterProducts(query : SupportSQLiteQuery) : LiveData<List<CategoryWiseEntity>>
}