package com.example.legacyframeapp.data.network.model

data class CreateProductRequest(
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val stock: Int,
    val imagenUrl: String,
    val categoria: CategoryIdRequest
)
