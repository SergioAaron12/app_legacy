package com.example.legacyframeapp.data.local.cart

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    // Get all items (reactive).
    @Query("SELECT * FROM cart_items")
    fun getAllItems(): Flow<List<CartItemEntity>>

    // Count the total number of products (sum of 'quantity').
    // Use COALESCE so it returns 0 when empty instead of null.
    @Query("SELECT COALESCE(SUM(quantity), 0) FROM cart_items")
    fun getItemCount(): Flow<Int>

    // Calculate the total price (sum of price * quantity).
    @Query("SELECT SUM(price * quantity) FROM cart_items")
    fun getTotalPrice(): Flow<Int?>

    // Find a specific product (to check if it exists and add +1).
    @Query("SELECT * FROM cart_items WHERE type = :type AND refId = :refId LIMIT 1")
    suspend fun getItemByRef(type: String, refId: Long): CartItemEntity?

    // Insert a new item.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CartItemEntity)

    // Update the quantity.
    @Update
    suspend fun update(item: CartItemEntity)

    // Delete a single item.
    @Delete
    suspend fun delete(item: CartItemEntity)

    // Clear the cart.
    @Query("DELETE FROM cart_items")
    suspend fun clear(): Unit
}