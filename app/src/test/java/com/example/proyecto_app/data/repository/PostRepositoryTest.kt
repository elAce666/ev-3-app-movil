package com.example.proyecto_app.data.repository

import com.example.proyecto_app.data.model.remote.Post
import com.example.proyecto_app.data.network.JsonPlaceholderApi
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

// EXPLICACIÓN (Punto K): Esta es una prueba unitaria. Su objetivo es probar una pequeña "unidad" de código
// (la clase PostRepository) de forma aislada, sin depender de una conexión a internet real.
@ExperimentalCoroutinesApi
class PostRepositoryTest {

    private lateinit var api: JsonPlaceholderApi
    private lateinit var repository: PostRepository

    @Before
    fun setUp() {
        // Usamos la librería MockK para crear un "doble de prueba" o un mock de nuestra API.
        // Esto nos permite simular su comportamiento sin hacer una llamada de red real.
        api = mockk()
        // Inyectamos el mock en el constructor para que el repositorio trabaje con nuestra versión falsa de la API.
        repository = PostRepository(api)
    }

    @Test
    fun `getPosts success should emit list of posts`() = runTest {
        // 1. Arrange (Preparar): Preparamos el escenario de la prueba.
        // Le decimos a nuestro mock que cuando se llame a la función `getPosts()`, debe devolver una lista falsa.
        val fakePosts = listOf(Post(1, 1, "Title 1", "Body 1"))
        coEvery { api.getPosts() } returns fakePosts

        // 2. Act (Actuar): Ejecutamos la función que queremos probar.
        val result = repository.getPosts().first()

        // 3. Assert (Verificar): Comprobamos que el resultado es el esperado.
        // Verificamos que la lista tiene 1 elemento y el título es correcto.
        assertEquals(1, result.size)
        assertEquals("Title 1", result[0].title)
        // EXPLICACIÓN: Las pruebas son importantes porque garantizan la calidad y previenen regresiones.
        // Si alguien modifica el PostRepository en el futuro y rompe la lógica, esta prueba fallará, avisándonos del error.
    }

    @Test
    fun `getPosts failure should emit empty list`() = runTest {
        // Arrange: Simulamos que la API lanza una excepción (ej. error de red).
        coEvery { api.getPosts() } throws RuntimeException("Network error")

        // Act: Ejecutamos la misma función.
        val result = repository.getPosts().first()

        // Assert: Verificamos que nuestro repositorio maneja el error correctamente devolviendo una lista vacía.
        assertEquals(0, result.size)
    }
}
