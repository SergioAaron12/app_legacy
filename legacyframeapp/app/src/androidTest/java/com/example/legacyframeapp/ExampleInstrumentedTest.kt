package com.example.legacyframeapp

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.legacyframeapp", appContext.packageName)
    }

    @Test
    fun appContext_isNotNull() {
        // Verify the target context is available.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertNotNull(appContext)
    }

    @Test
    fun appContext_hasResources() {
        // Verify resources can be accessed.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertNotNull(appContext.resources)
    }
}