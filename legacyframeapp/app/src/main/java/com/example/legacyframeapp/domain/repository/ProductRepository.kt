package com.example.legacyframeapp.domain.repository

import com.example.legacyframeapp.data.network.model.CreateProductRequest
import com.example.legacyframeapp.domain.model.Product

interface ProductRepository {
    suspend fun getProducts(): List<Product>

    suspend fun createProduct(req: CreateProductRequest): Boolean

    suspend fun updateProduct(id: String, req: CreateProductRequest): Boolean

    suspend fun deleteProduct(id: String): Boolean
}