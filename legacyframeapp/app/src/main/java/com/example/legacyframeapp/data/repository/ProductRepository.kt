package com.example.legacyframeapp.data.repository

import android.util.Log
import com.example.legacyframeapp.data.network.ProductApiService
import com.example.legacyframeapp.data.network.model.CreateProductRequest
import com.example.legacyframeapp.data.network.model.ProductRemote
import com.example.legacyframeapp.domain.model.Product
import com.example.legacyframeapp.domain.repository.ProductRepository

class ProductRepositoryImpl(private val apiService: ProductApiService) : ProductRepository {

    companion object {
        // Your server (emulator or Dev Tunnels).
        private const val BASE_IMAGE_URL = "http://10.0.2.2:8083"
    }

    override suspend fun getProducts(): List<Product> {
        return try {
            val response = apiService.getProducts()

            if (response.isSuccessful) {
                val remotes: List<ProductRemote> = response.body() ?: emptyList()

                remotes.map { remote ->
                    val pathDesdeBd = remote.imagenUrl ?: ""

                    // --- URL NORMALIZATION ---
                    // If it starts with 'content' or 'file', it's a local photo -> keep as is.
                    // If it starts with 'http', it's already remote -> keep as is.
                    // Otherwise, it is a server image (/assets) -> prefix with the server URL.

                    val finalUrl = when {
                        pathDesdeBd.startsWith("content://") -> pathDesdeBd
                        pathDesdeBd.startsWith("file://") -> pathDesdeBd
                        pathDesdeBd.startsWith("http") -> pathDesdeBd
                        pathDesdeBd.isBlank() -> ""
                        else -> {
                            val pathLimpio = if (pathDesdeBd.startsWith("/")) pathDesdeBd else "/$pathDesdeBd"
                            BASE_IMAGE_URL + pathLimpio
                        }
                    }

                    Product(
                        id = remote.id.toString(),
                        name = remote.nombre,
                        description = remote.descripcion ?: "",
                        price = remote.precio.toInt(),
                        imageUrl = finalUrl,
                        category = remote.categoria?.nombre ?: "Sin categoría",
                        stock = remote.stock
                    )
                }
            } else {
                Log.e("ProductRepo", "Error API: ${response.code()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("ProductRepo", "Error red: ${e.message}")
            emptyList()
        }
    }

    override suspend fun createProduct(request: CreateProductRequest): Boolean {
        return try {
            val response = apiService.createProduct(request)
            if (!response.isSuccessful) {
                val body = try { response.errorBody()?.string() } catch (_: Exception) { null }
                Log.e("ProductRepo", "Create failed: HTTP ${response.code()} ${body ?: ""}")
            }
            response.isSuccessful
        } catch (e: Exception) {
            Log.e("ProductRepo", "Create exception: ${e.message}")
            false
        }
    }

    override suspend fun updateProduct(id: String, request: CreateProductRequest): Boolean {
        return try {
            val response = apiService.updateProduct(id.toLong(), request)
            if (!response.isSuccessful) {
                val body = try { response.errorBody()?.string() } catch (_: Exception) { null }
                Log.e("ProductRepo", "Update failed: id=$id HTTP ${response.code()} ${body ?: ""}")
            }
            response.isSuccessful
        } catch (e: Exception) {
            Log.e("ProductRepo", "Update exception: ${e.message}")
            false
        }
    }

    override suspend fun deleteProduct(id: String): Boolean {
        return try {
            val response = apiService.deleteProduct(id.toLong())
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getProductById(id: String): Product? {
        return getProducts().find { it.id == id }
    }
}