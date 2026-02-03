package com.example.legacyframeapp

import com.example.legacyframeapp.domain.validation.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ValidatorsTest {

    // --- TEST 1: Email ---
    @Test
    fun `validateEmail retorna error si esta vacio`() {
        // Execute (When)
        val result = validateEmail("")
        // Verify (Then)
        assertEquals("El correo es obligatorio", result)
    }

    // --- TEST 2: RUT ---
    @Test
    fun `validateRut detecta letras`() {
        val result = validateRut("1234567K")
        assertEquals("Solo números (sin puntos ni guión)", result)
    }

    // --- TEST 3: Password ---
    @Test
    fun `validateStrongPassword pide mayuscula`() {
        val result = validateStrongPassword("clave123!")
        assertEquals("Debe incluir una mayúscula", result)
    }
}