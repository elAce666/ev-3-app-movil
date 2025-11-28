package com.example.proyecto_app.ui.viewmodels

import com.example.proyecto_app.MainDispatcherRule
import com.example.proyecto_app.data.model.remote.Post
import com.example.proyecto_app.data.repository.PostRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PostViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: PostRepository
    private lateinit var viewModel: PostViewModel

    @Before
    fun setUp() {
        repository = mockk()
    }

    @Test
    fun `init success should update posts state`() = runTest {
        // Arrange
        val fakePosts = listOf(Post(1, 1, "Title 1", "Body 1"))
        coEvery { repository.getPosts() } returns flowOf(fakePosts)

        // Act: Inyectamos el despachador de prueba
        viewModel = PostViewModel(repository, mainDispatcherRule.testDispatcher)

        // Assert
        assertEquals(1, viewModel.posts.value.size)
        assertEquals("Title 1", viewModel.posts.value[0].title)
    }

    @Test
    fun `init failure should result in empty posts state`() = runTest {
        // Arrange
        coEvery { repository.getPosts() } returns flowOf(emptyList())

        // Act: Inyectamos el despachador de prueba
        viewModel = PostViewModel(repository, mainDispatcherRule.testDispatcher)

        // Assert
        assertEquals(0, viewModel.posts.value.size)
    }
}
