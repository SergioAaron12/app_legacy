package com.example.legacyframeapp.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.legacyframeapp.data.local.cart.CartDao
import com.example.legacyframeapp.data.local.cart.CartItemEntity

@Database(
    entities = [CartItemEntity::class], // Keep only the cart table.
    version = 5, // Bump the version to force an update.
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // The only DAO we need now.
    abstract fun cartDao(): CartDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "legacyframe_db_v2" // Renamed to create a clean database from scratch.
                )
                    .fallbackToDestructiveMigration() // If version conflicts exist, rebuild the DB (safe because the cart is temporary).
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}