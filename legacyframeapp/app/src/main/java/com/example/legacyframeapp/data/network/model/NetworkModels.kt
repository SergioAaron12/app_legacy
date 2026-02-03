package com.example.legacyframeapp.data.network.model

import com.google.gson.annotations.SerializedName

// ==================================================================
// 1. AUTH (LOGIN AND REGISTER)
// ==================================================================
data class LoginRequest(
    val email: String,
    @SerializedName("password") val pass: String
)

data class TokenResponse(
    val token: String
)

data class RegisterRequest(
    val nombre: String,
    val apellido: String?,
    val email: String,
    val password: String,
    val confirmPassword: String,
    val rut: String,
    val dv: String,
    val telefono: String
)

data class ApiErrorResponse(
    val message: String? = null,
    val field: String? = null
)

// ==================================================================
// 2. USER PROFILE (NEW)
// ==================================================================
// What we receive from the server when requesting data (/auth/perfil)
data class UserProfileResponse(
    val nombre: String,
    val apellido: String?,
    val email: String,
    val telefono: String?,
    val direccion: String?
)

// What we send to the server to update (/auth/profile)
data class UpdateProfileRequest(
    val nombre: String,
    val apellido: String,
    val telefono: String,
    val direccion: String,
    val password: String? = null,        // Optional (if null, it is not changed)
    val confirmPassword: String? = null  // Optional
)

// ==================================================================
// 3. PRODUCTS (CATALOG)
// ==================================================================
data class ProductRemote(
    val id: Long,
    val nombre: String,
    val descripcion: String?,
    val precio: Double,
    val stock: Int,
    val imagenUrl: String?,
    val categoria: CategoriaRemote?
)

data class CategoriaRemote(
    val id: Long,
    val nombre: String,
    val descripcion: String?
)

data class CategoryIdRequest(
    val id: Long
)

// ==================================================================
// 4. ORDERS
// ==================================================================
data class OrderRequest(
    val items: List<OrderDetail>
)

data class OrderDetail(
    val productoId: Long,
    val nombreProducto: String,
    val cantidad: Int,
    val precioUnitario: Double
)

data class OrderResponse(
    val id: Long,
    val total: Double,
    val estado: String,
    val fechaCreacion: String?,
    val detalles: List<OrderDetailResponse>?
)

data class OrderDetailResponse(
    val nombreProducto: String,
    val cantidad: Int,
    val precioUnitario: Double
)

// ==================================================================
// 5. CONTACT
// ==================================================================
data class ContactRequest(
    val nombre: String,
    val email: String,
    val mensaje: String
)

// ==================================================================
// 6. EXTERNAL API (ECONOMIC INDICATORS)
// ==================================================================
data class IndicadoresResponse(
    val dolar: IndicadorData
)

data class IndicadorData(
    val valor: Double,
    val fecha: String
)
