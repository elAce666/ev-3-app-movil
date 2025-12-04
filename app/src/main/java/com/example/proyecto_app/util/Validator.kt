package com.example.proyecto_app.util

// Objeto que centraliza toda la lógica de validación.
object Validator {

    fun validateFullName(name: String): String? {
        if (name.isBlank()) {
            return "El nombre no puede estar vacío."
        }
        return null
    }

    fun validateEmail(email: String): String? {
        // Expresión regular simple para validar el formato de un email.
        // Se corrige el escape del punto para que la expresión regular sea válida en Kotlin.
        val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

        return when {
            email.isBlank() -> "El correo no puede estar vacío."
            !email.matches(emailRegex) -> "Formato de correo electrónico inválido."
            !email.endsWith("@duoc.cl", ignoreCase = true) -> "Solo se aceptan correos del dominio @duoc.cl."
            else -> null
        }
    }

    fun validatePassword(password: String): List<String> {
        val errors = mutableListOf<String>()
        if (password.length < 8) {
            errors.add("Mínimo 8 caracteres.")
        }
        if (!password.any { it.isUpperCase() }) {
            errors.add("Al menos una mayúscula.")
        }
        if (!password.any { it.isLowerCase() }) {
            errors.add("Al menos una minúscula.")
        }
        if (!password.any { it.isDigit() }) {
            errors.add("Al menos un número.")
        }
        // Corregido para que no falle con contraseñas que solo tienen letras y números
        if (password.all { it.isLetterOrDigit() }) {
            errors.add("Al menos un carácter especial.")
        }
        return errors
    }

    fun confirmPassword(password: String, confirmation: String): String? {
        if (password != confirmation) {
            return "Las contraseñas no coinciden."
        }
        return null
    }
}
