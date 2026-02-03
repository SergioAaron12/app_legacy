package com.example.legacyframeapp

import android.app.Application
import com.example.legacyframeapp.data.local.storage.UserPreferences
import com.example.legacyframeapp.data.network.ExternalApiService
import com.example.legacyframeapp.data.network.model.IndicadorData
import com.example.legacyframeapp.data.network.model.IndicadoresResponse
import com.example.legacyframeapp.data.repository.CartRepository
import com.example.legacyframeapp.data.repository.ContactRepository
import com.example.legacyframeapp.data.repository.OrderRepository
import com.example.legacyframeapp.data.repository.UserRepository
import com.example.legacyframeapp.domain.repository.ProductRepository
import com.example.legacyframeapp.ui.viewmodel.AuthViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import com.example.legacyframeapp.data.remote.RetrofitClient
import com.example.legacyframeapp.domain.repository.CuadroRepository

@OptIn(ExperimentalCoroutinesApi::class)
class gAuthViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Dependency mocks
    private val app = mockk<Application>(relaxed = true)
    private val userRepo = mockk<UserRepository>()
    private val productRepo = mockk<ProductRepository>()
    private val cuadroRepo = mockk<CuadroRepository>()
    private val cartRepo = mockk<CartRepository>(relaxed = true)
    private val userPrefs = mockk<UserPreferences>(relaxed = true)
    private val orderRepo = mockk<OrderRepository>()
    private val contactRepo = mockk<ContactRepository>()
    private val externalServiceMock = mockk<ExternalApiService>(relaxed = true)

    private lateinit var viewModel: AuthViewModel

    @Before
    fun setup() {
        // 1. Mock RetrofitClient (for the dollar call in init)
        mockkObject(RetrofitClient)
        coEvery { RetrofitClient.externalService } returns externalServiceMock
        coEvery { externalServiceMock.getIndicadores() } returns Response.success(null)

        // 2. Mock UserPreferences flows to avoid initialization errors
        coEvery { userPrefs.isLoggedIn } returns flowOf(false)
        coEvery { userPrefs.isDarkMode } returns flowOf(false)
        coEvery { userPrefs.themeMode } returns flowOf("system")
        coEvery { userPrefs.accentColor } returns flowOf("#FF8B5C2A")
        coEvery { userPrefs.fontScale } returns flowOf(1.0f)
        coEvery { userPrefs.notifOffers } returns flowOf(true)
        coEvery { userPrefs.notifTracking } returns flowOf(true)
        coEvery { userPrefs.notifCart } returns flowOf(true)

        // 3. Mock repositories called on startup
        coEvery { productRepo.getProducts() } returns emptyList()
        coEvery { cartRepo.items() } returns flowOf(emptyList())
        coEvery { cartRepo.total() } returns flowOf(0)
        coEvery { cartRepo.count() } returns flowOf(0)

        // Mock cuadros catalog (getCuadros) called in init
        coEvery { cuadroRepo.getCuadros() } returns emptyList()

        // --- FIX HERE ---
        // It used to call getAll() (no longer exists); now we use getMyOrders()
        coEvery { orderRepo.getMyOrders(any()) } returns emptyList()
        // -----------------------

        // Initialize the ViewModel
        viewModel = AuthViewModel(
            app, userRepo, productRepo, cuadroRepo,
            cartRepo, userPrefs, orderRepo, contactRepo
        )
    }

    @After
    fun tearDown() {
        unmockkAll() // Clear static mocks
    }

    // --- TEST 1: Successful Login ---
    @Test
    fun`submitLogin exitoso actualiza estado a success`() = runTest {
        // GIVEN
        val email = "test@duoc.cl"
        val pass = "123456"
        coEvery { userRepo.login(email, pass) } returns Result.success("fake-token")

        // WHEN
        viewModel.onLoginEmailChange(email)
        viewModel.onLoginPassChange(pass)
        viewModel.submitLogin()

        // THEN
        assertTrue(viewModel.login.value.success)
        assertFalse(viewModel.login.value.isSubmitting)
        // Verify that the ViewModel attempted to save the session
        coVerify { userPrefs.setLoggedIn(true) }
    }

    // --- TEST 2: Failed Login ---
    @Test
    fun `submitLogin fallido actualiza estado con error`() = runTest {
        // GIVEN
        val email = "fail@duoc.cl"
        val pass = "wrong"
        coEvery { userRepo.login(email, pass) } returns Result.failure(Exception("Credenciales malas"))

        // WHEN
        viewModel.onLoginEmailChange(email)
        viewModel.onLoginPassChange(pass)
        viewModel.submitLogin()

        // THEN
        assertFalse(viewModel.login.value.success)
        assertEquals("Credenciales malas", viewModel.login.value.errorMsg)
    }

    // --- TEST 3: Successful Register ---
    @Test
    fun `submitRegister exitoso actualiza estado`() = runTest {
        // GIVEN
        coEvery { userRepo.register(any()) } returns Result.success(true)

        // WHEN
        viewModel.onRegisterNombreChange("Juan")
        viewModel.onRegisterEmailChange("juan@test.com")
        viewModel.onRegisterPassChange("Pass123!")
        viewModel.onRegisterConfirmChange("Pass123!")
        viewModel.onRegisterRutChange("11111111")
        viewModel.onRegisterDvChange("1")
        viewModel.onRegisterPhoneChange("987654321")

        viewModel.submitRegister()

        // THEN
        assertTrue(viewModel.register.value.success)
        assertFalse(viewModel.register.value.isSubmitting)
    }
}