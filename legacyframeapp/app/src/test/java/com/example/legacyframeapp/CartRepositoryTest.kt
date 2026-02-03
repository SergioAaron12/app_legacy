package com.example.legacyframeapp

import com.example.legacyframeapp.data.local.cart.CartDao
import com.example.legacyframeapp.data.local.cart.CartItemEntity
import com.example.legacyframeapp.data.repository.CartRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class CartRepositoryTest {

    private val cartDao = mockk<CartDao>(relaxed = true)
    private lateinit var repository: CartRepository

    @Before
    fun setup() {
        repository = CartRepository(cartDao)
    }

    @Test
    fun insertaItemNuevoSiNoExiste() = runBlocking {
        // GIVEN: The DAO says that product does not exist (returns null)
        coEvery { cartDao.getItemByRef("product", 1L) } returns null

        // WHEN: We add a product
        repository.addOrIncrement("product", 1L, "Marco", 1000, "img.jpg")

        // THEN: Verify it called INSERT
        coVerify { cartDao.insert(any()) }
        // Verify it did NOT call UPDATE
        coVerify(exactly = 0) { cartDao.update(any()) }
    }

    @Test
    fun addOrIncrementActualizaCantidadSiItemYaExiste() = runBlocking {
        // GIVEN: The DAO says the product already exists with quantity 1
        val existingItem = CartItemEntity(
            id = 5, type = "product", refId = 1L, name = "Marco", price = 1000, imageUrl = "img.jpg", quantity = 1
        )
        coEvery { cartDao.getItemByRef("product", 1L) } returns existingItem

        // WHEN: We add it again
        repository.addOrIncrement("product", 1L, "Marco", 1000, "img.jpg")

        // THEN: Verify it called UPDATE
        coVerify {
            // Verify it updated the object to quantity 2 (1 + 1)
            cartDao.update(match { it.quantity == 2 })
        }
        // Verify it did NOT call INSERT
        coVerify(exactly = 0) { cartDao.insert(any()) }
    }

    @Test
    fun removeEliminaItemDelCarrito() = runBlocking {
        // GIVEN: An item exists in the cart
        val itemToRemove = CartItemEntity(
            id = 10, type = "product", refId = 2L, name = "Greca", price = 500, imageUrl = "img.jpg", quantity = 2
        )

        // WHEN: We remove the item
        repository.remove(itemToRemove)

        // THEN: Verify it called delete
        coVerify {
            cartDao.delete(itemToRemove)
        }
    }
}