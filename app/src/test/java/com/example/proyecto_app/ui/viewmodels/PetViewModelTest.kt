package com.example.proyecto_app.ui.viewmodels

import com.example.proyecto_app.data.model.Pet
import com.example.proyecto_app.data.repository.PetRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

// EXPLICACIÓN (Punto K): Esta es la segunda clase de prueba, para el PetViewModel.
// Es más compleja porque el ViewModel depende de un Repositorio y de Coroutines.
@ExperimentalCoroutinesApi
class PetViewModelTest {

    // 1. Declaramos las dependencias que vamos a usar.
    private lateinit var repository: PetRepository
    private lateinit var viewModel: PetViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        // 2. Antes de cada test, preparamos el entorno.
        Dispatchers.setMain(testDispatcher) // Le decimos a las coroutines que usen nuestro dispatcher de prueba.
        repository = mockk() // Creamos un mock (un doble) del repositorio.
        viewModel = PetViewModel(repository, testDispatcher) // Creamos el ViewModel inyectándole el mock.
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Limpiamos el dispatcher después de cada test.
    }

    @Test
    fun `cuando el ViewModel se inicia, carga la lista de mascotas exitosamente`() = runTest {
        // Arrange: Preparamos nuestro mock.
        val fakePets = listOf(Pet(name = "Rex", type = "Perro"))
        coEvery { repository.getAllPets() } returns flowOf(fakePets)

        // Act: La función loadPets() se llama en el `init` del ViewModel. Solo necesitamos avanzar el tiempo.
        testDispatcher.scheduler.advanceUntilIdle() 

        // Assert: Verificamos que el estado del ViewModel contenga la lista que nuestro mock devolvió.
        assertEquals(1, viewModel.pets.value.size)
        assertEquals("Rex", viewModel.pets.value[0].name)
    }
}
