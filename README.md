# Proyecto GUAU&MIAU - Evaluación Parcial

## Descripción del Proyecto

Esta es una aplicación para Android desarrollada como parte de la Evaluación Parcial. La aplicación implementa un sistema de registro y login de usuarios para la tienda ficticia "GUAU&MIAU". El proyecto está construido desde cero utilizando tecnologías modernas de Android, como Kotlin y Jetpack Compose, y sigue una arquitectura MVVM para garantizar un código limpio, escalable y fácil de mantener.

El objetivo principal es demostrar la aplicación de patrones de diseño, la integración con recursos nativos del dispositivo y la creación de una interfaz de usuario funcional y reactiva.

---

## Integrantes

*   **Gabriel Mayorga**

---

## Funcionalidades Implementadas

La aplicación cuenta con las siguientes funcionalidades clave, cumpliendo con todos los requisitos de la pauta:

#### 1. Gestión de Usuarios

*   **Pantalla de Registro Completa:**
    *   Formulario para registrar nuevos usuarios con validación en tiempo real.
    *   Captura de foto de perfil utilizando la cámara del dispositivo.
    *   Sección para registrar una o más mascotas de forma dinámica (añadir/eliminar).
*   **Pantalla de Login:**
    *   Autenticación de usuarios contra los datos guardados localmente.
    *   Mensajes de error claros en caso de credenciales incorrectas.
*   **Persistencia de Sesión (Simulada):**
    *   Se utiliza `DataStore` de Jetpack para guardar los datos del usuario registrado, simulando una sesión persistente incluso si la aplicación se cierra.

#### 2. Validaciones Desacopladas

Se ha implementado una lógica de validación centralizada en una clase `Validator` separada, que verifica:
*   **Nombre Completo:** No vacío, solo letras y espacios, máximo 50 caracteres.
*   **Correo Electrónico:** Formato válido y perteneciente al dominio `@duoc.cl`.
*   **Contraseña:** Complejidad mínima (8 caracteres, mayúscula, minúscula, número y carácter especial) con retroalimentación visual en tiempo real (✓/✗).
*   **Mascotas:** Obligatoriedad de registrar al menos una mascota, con su nombre y tipo.

#### 3. Integración de Recursos Nativos

*   **Cámara:** Se integra con la cámara del dispositivo para tomar y mostrar una foto de perfil.
*   **GPS (Ubicación):** Se incluye un botón que solicita permisos de ubicación y muestra las coordenadas actuales del dispositivo.

#### 4. Interfaz de Usuario y UX

*   **Jetpack Compose:** Toda la UI está construida de forma declarativa.
*   **Animaciones Funcionales:** Se utilizan animaciones `AnimatedVisibility` para mostrar y ocultar los mensajes de error de forma suave, mejorando la experiencia de usuario.
*   **Navegación:** Se gestiona el flujo entre las pantallas de Login y Registro con `Jetpack Navigation` para Compose.

---

## Tecnologías y Arquitectura

*   **Lenguaje:** Kotlin
*   **UI:** Jetpack Compose
*   **Arquitectura:** MVVM (Model-View-ViewModel)
*   **Asincronía:** Coroutines & Flow
*   **Persistencia:** Jetpack DataStore Preferences
*   **Navegación:** Jetpack Navigation for Compose
*   **Carga de Imágenes:** Coil
*   **Recursos Nativos:** Camera & Location (GPS)

---

## Pasos para Ejecutar el Proyecto

1.  **Clonar el Repositorio:**
    ```bash
    git clone <URL_DEL_REPOSITORIO_GITHUB>
    ```
2.  **Abrir en Android Studio:**
    *   Abre el proyecto con una versión reciente de Android Studio (por ejemplo, Hedgehog o superior).
3.  **Sincronizar Gradle:**
    *   Espera a que Android Studio sincronice el proyecto y descargue todas las dependencias necesarias. Esto puede tardar unos minutos la primera vez.
4.  **Ejecutar la Aplicación:**
    *   Selecciona un emulador o un dispositivo físico con API 24 o superior y ejecuta la aplicación.
5.  **Conceder Permisos:**
    *   La aplicación solicitará permisos para acceder a la **Ubicación** y la **Cámara**. Es necesario aceptarlos para probar dichas funcionalidades.
