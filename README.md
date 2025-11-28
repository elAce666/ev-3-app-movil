# Proyecto "Guau&Miau" - Aplicación Full-Stack

## Descripción del Proyecto

"Guau&Miau" es una aplicación full-stack que simula una tienda de mascotas. El proyecto abarca tanto el desarrollo de una **aplicación nativa para Android** como el de una **API REST de backend**.

La **aplicación Android**, construida con tecnologías modernas como Kotlin y Jetpack Compose, permite a los usuarios registrarse, iniciar sesión, ver un catálogo de productos, gestionar un carrito de compras, registrar estadías para sus mascotas y administrar su perfil.

El **backend**, desarrollado con Spring Boot, proporciona los microservicios necesarios para gestionar los datos de productos y mascotas.

El proyecto sigue una arquitectura MVVM en el frontend y una arquitectura de capas en el backend, demostrando un desarrollo de software limpio, escalable y mantenible.

---

## Integrantes

*   **Gabriel Mayorga**

---

## Funcionalidades Implementadas

#### 1. Gestión de Usuarios y Perfil
*   **Registro y Login:** Sistema de autenticación local.
*   **Persistencia de Sesión:** Se utiliza `DataStore` para guardar los datos del usuario, simulando una sesión que persiste al cerrar la app.
*   **Perfil de Usuario Editable:** El usuario puede modificar sus datos personales (género, edad, etc.) y cambiar su foto de perfil.

#### 2. Catálogo de Productos y Flujo de Compra
*   **Catálogo de Productos:** La app consume una API REST para mostrar una lista de productos.
*   **Carrito de Compras:** Funcionalidad para añadir, modificar la cantidad y eliminar productos del carrito.
*   **Simulación de Compra:** Al "proceder al pago", la compra se guarda y se ve reflejada en el historial del usuario.
*   **Historial de Compras:** La pantalla de perfil muestra un historial de todas las compras realizadas.

#### 3. Gestión de Mascotas
*   **CRUD Completo de Mascotas:** Los usuarios pueden registrar las estadías de sus mascotas, incluyendo nombre, tipo, fechas y servicios adicionales.
*   **Edición y Eliminación:** La lista de mascotas permite modificar o eliminar registros existentes.

#### 4. Recursos Nativos del Dispositivo
*   **Cámara y Galería:** Integración completa para que el usuario pueda seleccionar una foto de perfil desde la galería o tomar una nueva con la cámara.

#### 5. Interfaz de Usuario y UX
*   **100% Jetpack Compose:** Toda la UI es declarativa, moderna y reactiva.
*   **Diseño Personalizado:** Se aplica una paleta de colores y un tema visual propio de "Guau&Miau".
*   **Animaciones Funcionales:** Se utilizan animaciones para mejorar el feedback al usuario, como al mostrar errores en formularios.

---

## Tecnologías y Arquitectura

*   **Aplicación Android:**
    *   **Lenguaje:** Kotlin
    *   **UI:** Jetpack Compose
    *   **Arquitectura:** MVVM (Model-View-ViewModel)
    *   **Asincronía:** Coroutines & Flow
    *   **Persistencia:** Jetpack DataStore
    *   **Navegación:** Jetpack Navigation for Compose
    *   **Red:** Retrofit & Kotlinx Serialization
    *   **Carga de Imágenes:** Coil
*   **Backend:**
    *   **Framework:** Spring Boot
    *   **Lenguaje:** Java
    *   **Base de Datos:** H2 (en memoria, para desarrollo)
    *   **Acceso a Datos:** Spring Data JPA
    *   **Otros:** Lombok

---

## Guía de Ejecución y Pruebas (Paso a Paso)

Para ejecutar y probar el proyecto completo, necesitas tener ambos, el backend y el frontend, funcionando en paralelo.

#### Paso 1: Levantar el Backend (Spring Boot)
1.  Abre el proyecto del backend (`guau-miau-api`) con IntelliJ IDEA.
2.  Espera a que IntelliJ sincronice las dependencias de Maven (puede tardar unos minutos la primera vez).
3.  Busca la clase `GuauMiauApiApplication.java` y ejecútala (botón de play).
4.  Verifica que en la consola aparezca el mensaje `Started GuauMiauApiApplication...`. Esto significa que tu API ya está corriendo en `localhost:8080`.

#### Paso 2: Probar el Backend (Postman)
1.  Abre Postman.
2.  **Prueba `GET`:** Haz una petición `GET` a `http://localhost:8080/api/v1/productos`. Debería devolver un array vacío `[]`.
3.  **Prueba `POST`:** Haz una petición `POST` a `http://localhost:8080/api/v1/productos` con un JSON en el `Body` para crear un producto. Debería devolver el producto con un `id`.
4.  Vuelve a hacer el `GET` para confirmar que el producto se ha creado.

#### Paso 3: Ejecutar la Aplicación Android
1.  Abre el proyecto de la app (`proyecto_app`) con Android Studio.
2.  Espera a que Gradle sincronice las dependencias.
3.  **Con el backend aún corriendo**, ejecuta la aplicación en un emulador o dispositivo físico (API 24+).
4.  **Flujo de Prueba:**
    *   Regístrate como un nuevo usuario.
    *   Inicia sesión.
    *   Ve a la pestaña "Artículos". La app llamará a tu backend; si está vacío, lo poblará con datos de ejemplo y los mostrará.
    *   Añade productos al carrito, haz una compra y verifica que aparece en tu historial de perfil.
    *   Prueba a cambiar tu foto de perfil.
