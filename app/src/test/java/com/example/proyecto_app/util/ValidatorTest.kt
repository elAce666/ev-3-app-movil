package com.example.proyecto_app.util

import org.junit.Assert.*
import org.junit.Test

// EXPLICACIÓN (Punto K): Esta es una clase de prueba unitaria para nuestro objeto Validator.
// Su propósito es verificar que cada función de validación se comporta como esperamos, de forma aislada.
class ValidatorTest {

    // Test para la validación de correo electrónico
    @Test
    fun `validateEmail con correo válido de duoc retorna null`() {
        // 1. Arrange (Preparar): Definimos el correo que vamos a probar.
        val email = "test.user@duoc.cl"

        // 2. Act (Actuar): Llamamos a la función que queremos probar.
        val result = Validator.validateEmail(email)

        // 3. Assert (Verificar): Comprobamos que el resultado es el esperado (null significa sin errores).
        assertNull("El correo válido no debería retornar error", result)
    }

    @Test
    fun `validateEmail con correo inválido (sin @) retorna mensaje de error`() {
        val email = "test.userduoc.cl"
        val result = Validator.validateEmail(email)
        assertNotNull("El correo inválido debería retornar un error", result)
        assertEquals("Formato de correo electrónico inválido.", result)
    }

    @Test
    fun `validateEmail con dominio incorrecto retorna mensaje de error`() {
        val email = "test.user@gmail.com"
        val result = Validator.validateEmail(email)
        assertNotNull("El correo con dominio incorrecto debería retornar un error", result)
        assertEquals("Solo se aceptan correos del dominio @duoc.cl.", result)
    }

    // Test para la validación de contraseña
    @Test
    fun `validatePassword con contraseña válida retorna lista vacía`() {
        val password = "Password123!"
        val result = Validator.validatePassword(password)
        assertTrue("Una contraseña válida no debería tener errores", result.isEmpty())
    }

    @Test
    fun `validatePassword con contraseña corta retorna error`() {
        val password = "Pass1!"
        val result = Validator.validatePassword(password)
        assertTrue("Una contraseña corta debería retornar errores", result.isNotEmpty())
        assertTrue(result.contains("Mínimo 8 caracteres."))
    }
}
