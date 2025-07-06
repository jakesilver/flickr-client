package com.jakesilver.photoclient.scintillate.viewmodels

import androidx.lifecycle.SavedStateHandle
import com.jakesilver.photoclient.api.FlickrRepository
import com.jakesilver.photoclient.api.PhotoDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class PhotoDetailsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var testRepository: TestFlickrRepository
    private lateinit var savedStateHandle: SavedStateHandle

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        testRepository = TestFlickrRepository()
        savedStateHandle = SavedStateHandle()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when photoId is null, should emit error state`() = runTest(testDispatcher) {
        // Given
        val viewModel = PhotoDetailsViewModel(savedStateHandle, testRepository)

        // When
        val state = viewModel.photoDetailsUiState.first { it !is PhotoDetailsUiState.Loading }

        // Then
        assertTrue(state is PhotoDetailsUiState.Error)
        assertEquals("No photo found.", (state as PhotoDetailsUiState.Error).message)
    }

    @Test
    fun `when photoId is provided and photo exists, should emit photo details`() = runTest(testDispatcher) {
        // Given
        val photoId = "12345"
        val photoDetails = PhotoDetails(
            id = photoId,
            title = "Test Photo",
            description = "Test Description",
            url = "https://example.com/photo.jpg",
            dateTaken = Date(),
            datePosted = Date()
        )
        savedStateHandle["photoId"] = photoId
        testRepository.setPhotoDetails(photoDetails)

        // When
        val viewModel = PhotoDetailsViewModel(savedStateHandle, testRepository)
        val state = viewModel.photoDetailsUiState.first { it !is PhotoDetailsUiState.Loading }
        
        // Then
        assertTrue("Expected PhotoDetails but got ${state::class.simpleName}", state is PhotoDetailsUiState.PhotoDetails)
        assertEquals(photoDetails, (state as PhotoDetailsUiState.PhotoDetails).details)
    }

    @Test
    fun `when photoId is provided but photo not found, should emit error state`() = runTest(testDispatcher) {
        // Given
        val photoId = "nonexistent"
        savedStateHandle["photoId"] = photoId
        testRepository.setPhotoDetails(null)

        // When
        val viewModel = PhotoDetailsViewModel(savedStateHandle, testRepository)

        val state = viewModel.photoDetailsUiState.first { it !is PhotoDetailsUiState.Loading }

        // Then
        assertTrue(state is PhotoDetailsUiState.Error)
        assertEquals("No photo found.", (state as PhotoDetailsUiState.Error).message)
    }

    private class TestFlickrRepository : FlickrRepository {
        private var photoDetails: PhotoDetails? = null

        fun setPhotoDetails(details: PhotoDetails?) {
            this.photoDetails = details
        }

        override suspend fun getPhotoDetails(photoId: String) = flowOf(photoDetails)

        override suspend fun getPhotoResultsStream(tag: String) = throw NotImplementedError()
    }
}