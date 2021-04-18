package com.savage9ishere.tiwarimart.main_flow.ui.cart.cart_items_database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.savage9ishere.tiwarimart.checkout.address.address_database.AddressDao
import com.savage9ishere.tiwarimart.checkout.address.address_database.AddressEntity
import com.savage9ishere.tiwarimart.main_flow.ui.cart.save_for_later_database.SaveForLaterDao
import com.savage9ishere.tiwarimart.main_flow.ui.cart.save_for_later_database.SaveForLaterEntity
import com.savage9ishere.tiwarimart.main_flow.ui.user.favorites.favorites_database.FavoritesDao
import com.savage9ishere.tiwarimart.main_flow.ui.user.favorites.favorites_database.FavoritesEntity
import com.savage9ishere.tiwarimart.main_flow.ui.user.orders.previous_orders.ArrayListConverter
import com.savage9ishere.tiwarimart.main_flow.ui.user.orders.previous_orders.PreviousOrderDao
import com.savage9ishere.tiwarimart.main_flow.ui.user.orders.previous_orders.PreviousOrderEntity

@Database(entities = [CartItemEntity::class, SaveForLaterEntity::class, AddressEntity::class, PreviousOrderEntity::class, FavoritesEntity::class], version = 1, exportSchema = false)
@TypeConverters(ArrayListConverter::class)
abstract class CartItemsDatabase: RoomDatabase() {

    abstract val cartItemDao: CartItemDao
    abstract val saveForLaterDao: SaveForLaterDao
    abstract val addressDao : AddressDao
    abstract val previousOrderDao : PreviousOrderDao
    abstract val favoritesDao : FavoritesDao

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