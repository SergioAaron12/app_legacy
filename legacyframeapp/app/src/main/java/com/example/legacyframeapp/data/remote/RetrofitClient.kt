package com.example.legacyframeapp.data.remote

import com.example.legacyframeapp.data.network.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // Base URLs for each microservice.
    private const val AUTH_BASE_URL = "http://10.0.2.2:8085/"
    private const val PRODUCT_BASE_URL = "http://10.0.2.2:8083/"
    private const val ORDER_BASE_URL = "http://10.0.2.2:8084/"
    private const val CONTACT_BASE_URL = "http://10.0.2.2:8081/"
    private const val EXTERNAL_BASE_URL = "https://mindicador.cl/" // For economic indicators.

    // Auth instances
    val authApiService: AuthApiService by lazy {
        createRetrofit(AUTH_BASE_URL, addAuthHeader = true).create(AuthApiService::class.java)
    }

    // Alias for compatibility
    val authService: AuthApiService by lazy { authApiService }

    // Product instances
    val productApiService: ProductApiService by lazy {
        createRetrofit(PRODUCT_BASE_URL, addAuthHeader = true).create(ProductApiService::class.java)
    }

    val productService: ProductApiService by lazy { productApiService }

    // Order instances
    val orderApiService: OrderApiService by lazy {
        createRetrofit(ORDER_BASE_URL, addAuthHeader = true).create(OrderApiService::class.java)
    }

    val orderService: OrderApiService by lazy { orderApiService }

    // Contact instances
    val contactApiService: ContactApiService by lazy {
        // Contact does not require authentication; avoid sending Authorization.
        createRetrofit(CONTACT_BASE_URL, addAuthHeader = false).create(ContactApiService::class.java)
    }

    val contactService: ContactApiService by lazy { contactApiService }

    // External API instance
    val externalService: ExternalApiService by lazy {
        createRetrofit(EXTERNAL_BASE_URL, addAuthHeader = false).create(ExternalApiService::class.java)
    }

    private fun createRetrofit(baseUrl: String, addAuthHeader: Boolean): Retrofit {
        val client = if (addAuthHeader) authClient else defaultClient

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val defaultClient: OkHttpClient by lazy {
        OkHttpClient.Builder().build()
    }

    private val authClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                val original = chain.request()
                val path = original.url.encodedPath

                // Login/register should not include Authorization.
                if (path.endsWith("/auth/login") || path.endsWith("/auth/register")) {
                    return@Interceptor chain.proceed(original)
                }

                val token = TokenManager.token

                if (token.isNullOrBlank()) {
                    return@Interceptor chain.proceed(original)
                }

                val authValue = if (token.startsWith("Bearer ", ignoreCase = true)) token else "Bearer $token"
                val newRequest = original.newBuilder()
                    .header("Authorization", authValue)
                    .build()

                chain.proceed(newRequest)
            })
            .build()
    }
}
