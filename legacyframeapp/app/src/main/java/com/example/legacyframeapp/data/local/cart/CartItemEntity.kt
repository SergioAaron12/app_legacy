package com.example.legacyframeapp.data.local.cart

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String,      // "product" or "cuadro"
    val refId: Long,       // Original product ID
    val name: String,
    val price: Int,
    val imageUrl: String,  // This is the correct field name we need.
    val quantity: Int = 1
)