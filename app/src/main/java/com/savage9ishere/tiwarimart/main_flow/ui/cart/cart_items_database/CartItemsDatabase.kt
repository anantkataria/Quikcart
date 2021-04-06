package com.savage9ishere.tiwarimart.main_flow.ui.cart.cart_items_database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.savage9ishere.tiwarimart.checkout.address.address_database.AddressDao
import com.savage9ishere.tiwarimart.checkout.address.address_database.AddressEntity
import com.savage9ishere.tiwarimart.main_flow.ui.cart.save_for_later_database.SaveForLaterDao
import com.savage9ishere.tiwarimart.main_flow.ui.cart.save_for_later_database.SaveForLaterEntity

@Database(entities = [CartItemEntity::class, SaveForLaterEntity::class, AddressEntity::class], version = 1, exportSchema = false)
abstract class CartItemsDatabase: RoomDatabase() {

    abstract val cartItemDao: CartItemDao
    abstract val saveForLaterDao: SaveForLaterDao
    abstract val addressDao : AddressDao

    companion object {

        @Volatile
        private var INSTANCE: CartItemsDatabase? = null

        fun getInstance(context : Context) : CartItemsDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if(instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CartItemsDatabase::class.java,
                        "cart_items_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}