package com.example.legacyframeapp.data.repository

import com.example.legacyframeapp.data.network.ContactApiService
import com.example.legacyframeapp.data.network.model.ContactRequest
import java.io.IOException

class ContactRepository(private val apiService: ContactApiService) {
    suspend fun sendMessage(nombre: String, email: String, mensaje: String): Result<Unit> {
        return try {
            val request = ContactRequest(nombre, email, mensaje)
            val response = apiService.sendContact(request)

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al enviar: ${response.code()}"))
            }
        } catch (e: IOException) {
            Result.failure(Exception("No se pudo conectar con Soporte. Verifica que el microservicio 'contacto' esté encendido (puerto 8081)."))
        } catch (e: Exception) {
            Result.failure(Exception(e.message ?: "Error al enviar"))
        }
    }
}