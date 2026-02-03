package com.example.legacyframeapp.data.repository

import com.example.legacyframeapp.data.local.cart.CartDao
import com.example.legacyframeapp.data.local.cart.CartItemEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CartRepository(private val cartDao: CartDao) {

    // Get all cart items (Flow for real-time updates).
    fun items(): Flow<List<CartItemEntity>> = cartDao.getAllItems()

    // Count total products.
    fun count(): Flow<Int> = cartDao.getItemCount()

    // Calculate total price.
    fun total(): Flow<Int> = cartDao.getTotalPrice().map { it ?: 0 }

    // Add a product or increment quantity if it already exists.
    suspend fun addOrIncrement(
        type: String,
        refId: Long,
        name: String,
        price: Int,
        image: String // Incoming image value.
    ) {
        val existingItem = cartDao.getItemByRef(type, refId)

        if (existingItem != null) {
            // If it already exists, increment the quantity by 1.
            val updated = existingItem.copy(quantity = existingItem.quantity + 1)
            cartDao.update(updated)
        } else {
            // If it is new, create it.
            // This was the bug: we now use `imageUrl`.
            val newItem = CartItemEntity(
                type = type,
                refId = refId,
                name = name,
                price = price,
                imageUrl = image, // Assign the incoming image to the correct field.
                quantity = 1
            )
            cartDao.insert(newItem)
        }
    }

    suspend fun updateQuantity(item: CartItemEntity, newQuantity: Int) {
        if (newQuantity > 0) {
            cartDao.update(item.copy(quantity = newQuantity))
        }
    }

    suspend fun remove(item: CartItemEntity) {
        cartDao.delete(item)
    }

    suspend fun clear() {
        cartDao.clear()
    }
}