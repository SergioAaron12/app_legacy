package com.example.legacyframeapp.data.repository

import com.example.legacyframeapp.domain.model.Cuadro

class CuadroRepository {

    suspend fun insert(cuadro: Cuadro) {
    }

    suspend fun delete(cuadro: Cuadro) {
    }

    suspend fun getAllCategories(): List<String> {
        // Return static or empty categories because the main API handles this.
        return emptyList()
    }
}