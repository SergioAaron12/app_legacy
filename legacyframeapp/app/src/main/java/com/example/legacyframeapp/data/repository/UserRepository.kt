package com.example.legacyframeapp.data.repository

import com.example.legacyframeapp.data.local.storage.UserPreferences
import com.example.legacyframeapp.data.network.model.*
import com.example.legacyframeapp.data.remote.RetrofitClient
import com.google.gson.Gson

class UserRepository(private val userPreferences: UserPreferences) {

    private fun parseApiErrorMessage(rawBody: String?): String? {
        if (rawBody.isNullOrBlank()) return null
        return try {
            Gson().fromJson(rawBody, ApiErrorResponse::class.java)?.message
        } catch (_: Exception) {
            null
        }
    }

    // --- LOGIN ---
    suspend fun login(email: String, pass: String): Result<String> {
        return try {
            // Use `pass =` instead of `password =`.
            val request = LoginRequest(email = email, pass = pass)

            val response = RetrofitClient.authService.login(request)

            if (response.isSuccessful) {
                val token = response.body()?.token ?: ""
                // Save token and email.
                userPreferences.saveToken(token)
                userPreferences.saveEmail(email)
                userPreferences.setLoggedIn(true) // Ensure the user is marked as logged in.
                Result.success(token)
            } else {
                val msg = parseApiErrorMessage(response.errorBody()?.string())
                Result.failure(Exception(msg ?: "Error Login: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --- REGISTER ---
    suspend fun register(request: RegisterRequest): Result<Boolean> {
        return try {
            val response = RetrofitClient.authService.register(request)
            if (response.isSuccessful) {
                Result.success(true)
            } else {
                val msg = parseApiErrorMessage(response.errorBody()?.string())
                Result.failure(Exception(msg ?: "Error Registro: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --- LOG OUT ---
    suspend fun logout() {
        userPreferences.clear()
    }

    // --- GET PROFILE ---
    suspend fun getProfile(email: String): Result<UserProfileResponse> {
        return try {
            val response = RetrofitClient.authService.getProfile(email)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al cargar perfil: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --- UPDATE PROFILE ---
    suspend fun updateProfile(req: UpdateProfileRequest): Result<Boolean> {
        return try {
            val response = RetrofitClient.authService.updateProfile(req)
            if (response.isSuccessful) {
                Result.success(true)
            } else {
                val msg = parseApiErrorMessage(response.errorBody()?.string())
                Result.failure(Exception(msg ?: "Error al actualizar: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}