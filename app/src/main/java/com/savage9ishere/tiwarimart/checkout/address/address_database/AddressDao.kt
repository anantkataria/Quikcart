package com.savage9ishere.tiwarimart.checkout.address.address_database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AddressDao {

    @Insert
    suspend fun insert(item: AddressEntity)

    @Update
    suspend fun update(item: AddressEntity)

    @Delete
    suspend fun delete(item: AddressEntity)

    @Query("SELECT * FROM address_table")
    fun getAllAddresses(): LiveData<List<AddressEntity>>

}