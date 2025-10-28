package com.example.proyecto_app.util

import android.util.Patterns

object Validator {

    fun validateFullName(name: String): String? {
        if (name.isBlank()) {
            return "El nombre no puede estar vacío."
        }
        if (!name.matches(Regex("^[a-zA-Z ]+\$"))) {
            return "El nombre solo debe contener letras y espacios."
        }
        if (name.length > 50) {
            return "El nombre no puede exceder los 50 caracteres."
        }
        return null
    }

    fun validateEmail(email: String): String? {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return "Formato de correo electrónico inválido."
        }
        if (!email.endsWith("@duoc.cl", ignoreCase = true)) {
            return "Solo se aceptan correos del dominio @duoc.cl."
        }
        return null
    }

    fun validatePassword(password: String): List<String> {
        val errors = mutableListOf<String>()
        if (password.length < 8) {
            errors.add("Mínimo 8 caracteres.")
        }
        if (!password.contains(Regex("[A-Z]"))) {
            errors.add("Al menos una mayúscula.")
        }
        if (!password.contains(Regex("[a-z]"))) {
            errors.add("Al menos una minúscula.")
        }
        if (!password.contains(Regex("[0-9]"))) {
            errors.add("Al menos un número.")
        }
        if (!password.contains(Regex("[^a-zA-Z0-9]"))) {
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
