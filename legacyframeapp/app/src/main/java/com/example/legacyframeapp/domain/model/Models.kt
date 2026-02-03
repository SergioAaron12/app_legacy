package com.example.legacyframeapp.domain.model

// UI model for products (molduras).
data class Product(
    val id: String,
    val name: String,
    val description: String,
    val price: Int,
    val imageUrl: String,
    val category: String,
    val stock: Int = 0  // Available product stock.
)

// UI model for cuadros.
data class Cuadro(
    val id: String,
    val title: String,
    val description: String,
    val price: Int,
    val imageUrl: String,
    val category: String = "Cuadros",
    // Optional fields for compatibility with the current UI.
    val size: String = "",
    val material: String = "",
    val artist: String = ""
)

// Purchase history model (replaces OrderEntity).
data class Order(
    val id: Long = 0,
    val dateMillis: Long,
    val itemsText: String, // Items summary (e.g., "Frame x2...")
    val total: Int
)

// User model (replaces UserEntity for in-memory session).
data class User(
    val id: String = "0",
    val nombre: String,
    val apellido: String? = null,
    val email: String,
    val phone: String? = null
)