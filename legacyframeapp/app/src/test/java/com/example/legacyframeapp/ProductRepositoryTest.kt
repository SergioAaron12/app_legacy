package com.example.legacyframeapp

import com.example.legacyframeapp.data.network.ProductApiService
import com.example.legacyframeapp.data.network.model.CategoriaRemote
import com.example.legacyframeapp.data.network.model.ProductRemote
import com.example.legacyframeapp.data.repository.ProductRepositoryImpl
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class ProductRepositoryTest {

    // 1. Mock the API service
    private val apiServiceMock = mockk<ProductApiService>()
    private lateinit var repository: ProductRepositoryImpl

    @Before
    fun setup() {
        // Mock Android's static Log class (avoid logs during tests)
        mockkStatic(android.util.Log::class)
        every { android.util.Log.e(any(), any()) } returns 0
        every { android.util.Log.e(any(), any(), any()) } returns 0

        // Initialize the repository with the mocked service
        repository = ProductRepositoryImpl(apiServiceMock)
    }

    @After
    fun tearDown() {
        unmockkAll() // Clear mocks after each test
    }

    @Test
    fun `getAllProducts devuelve lista mapeada correctamente cuando API responde 200`() = runBlocking {
        // GIVEN: The API returns a list of remote products (backend format)
        val fakeApiData = listOf(
            ProductRemote(
                id = 1,
                nombre = "Marco Test",
                descripcion = "Descripción de prueba",
                precio = 5000.0,
                stock = 10,
                imagenUrl = "http://fake.url/img.jpg",
                categoria = CategoriaRemote(1, "Grecas", null)
            )
        )
        // Configure the mock to return success
        coEvery { apiServiceMock.getProducts() } returns Response.success(fakeApiData)

        // WHEN: We call the real repository
        val result = repository.getProducts()

        // THEN: Verify it mapped data to the domain model (UI format)
        assertEquals(1, result.size)
        assertEquals("Marco Test", result[0].name)        // Verify name -> name mapping
        assertEquals(5000, result[0].price)               // Verify Double -> Int conversion
        assertEquals("Grecas", result[0].category)        // Verify category extraction
    }

    @Test
    fun `getAllProducts devuelve lista vacia cuando API falla`() = runBlocking {
        // GIVEN: The API fails with a 500 error (Internal Server Error)
        coEvery { apiServiceMock.getProducts() } returns Response.error(
            500,
            okhttp3.ResponseBody.create(null, "Error del servidor")
        )

        // WHEN: We call the repository
        val result = repository.getProducts()

        // THEN: The repository should catch the error (and Log) and return an empty list
        assertTrue(result.isEmpty())
    }

    @Test
    fun `getAllProducts maneja correctamente URLs de imagenes`() = runBlocking {
        // GIVEN: The API responds with a product with a relative URL
        val fakeApiData = listOf(
            ProductRemote(
                id = 1,
                nombre = "Marco Test",
                descripcion = "Descripción de prueba",
                precio = 5000.0,
                stock = 10,
                imagenUrl = "/assets/img.jpg",
                categoria = CategoriaRemote(1, "Grecas", null)
            )
        )
        coEvery { apiServiceMock.getProducts() } returns Response.success(fakeApiData)

        // WHEN: We call the repository
        val result = repository.getProducts()

        // THEN: Verify that the URL was built correctly with the base server
        assertEquals(1, result.size)
        val expectedUrl = "http://10.0.2.2:8083/assets/img.jpg"
        assertEquals(expectedUrl, result[0].imageUrl)
    }
}