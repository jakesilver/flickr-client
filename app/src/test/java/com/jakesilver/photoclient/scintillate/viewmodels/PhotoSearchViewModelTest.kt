package com.jakesilver.photoclient.scintillate.viewmodels

import androidx.paging.PagingData
import com.jakesilver.photoclient.api.FlickrRepository
import com.jakesilver.photoclient.api.PhotoDetails
import com.jakesilver.photoclient.api.PhotoSummary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PhotoSearchViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var testRepository: TestFlickrRepository
    private lateinit var viewModel: PhotoSearchViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        testRepository = TestFlickrRepository()
        viewModel = PhotoSearchViewModel(testRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should have empty search query and no photos`() = runTest(testDispatcher) {
        // Then
        val uiState = viewModel.uiState.value
        assertEquals("", uiState.searchQuery)
        assertFalse(uiState.isLoading)
        assertNull(uiState.photos)
    }

    @Test
    fun `when searchByTag called with valid tag, should update search query and set loading`() = runTest(testDispatcher) {
        // Given
        val searchTag = "cats"
        testRepository.setPhotoResults(searchTag, listOf(
            PhotoSummary(id = "1", title = "Cat Photo", url = "cat.jpg")
        ))

        // When
        viewModel.searchByTag(searchTag)

        // Then - immediately after call, before coroutine completes
        val initialState = viewModel.uiState.value
        assertEquals(searchTag, initialState.searchQuery)
        assertTrue(initialState.isLoading)
        assertNull(initialState.photos)
    }

    @Test
    fun `when searchByTag completes, should set loading false and provide photos`() = runTest(testDispatcher) {
        // Given
        val searchTag = "cats"
        testRepository.setPhotoResults(searchTag, listOf(
            PhotoSummary(id = "1", title = "Cat Photo", url = "cat.jpg")
        ))

        // When
        viewModel.searchByTag(searchTag)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then - after coroutine completes
        val finalState = viewModel.uiState.value
        assertEquals(searchTag, finalState.searchQuery)
        assertFalse(finalState.isLoading)
        assertNotNull(finalState.photos)
    }

    @Test
    fun `when searchByTag called with blank tag, should not update state`() = runTest(testDispatcher) {
        // Given - initial state
        val initialState = viewModel.uiState.value

        // When
        viewModel.searchByTag("")
        testDispatcher.scheduler.advanceUntilIdle()

        // Then - state should remain unchanged
        val finalState = viewModel.uiState.value
        assertEquals(initialState, finalState)
    }

    @Test
    fun `when searchByTag called with whitespace tag, should not update state`() = runTest(testDispatcher) {
        // Given - initial state
        val initialState = viewModel.uiState.value

        // When
        viewModel.searchByTag("   ")
        testDispatcher.scheduler.advanceUntilIdle()

        // Then - state should remain unchanged
        val finalState = viewModel.uiState.value
        assertEquals(initialState, finalState)
    }

    @Test
    fun `when multiple searchByTag calls made, should update with latest search`() = runTest(testDispatcher) {
        // Given
        val firstTag = "cats"
        val secondTag = "dogs"
        testRepository.setPhotoResults(firstTag, listOf(
            PhotoSummary(id = "1", title = "Cat Photo", url = "cat.jpg")
        ))
        testRepository.setPhotoResults(secondTag, listOf(
            PhotoSummary(id = "2", title = "Dog Photo", url = "dog.jpg")
        ))

        // When - first search
        viewModel.searchByTag(firstTag)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then - verify first search
        val firstState = viewModel.uiState.value
        assertEquals(firstTag, firstState.searchQuery)
        assertFalse(firstState.isLoading)
        assertNotNull(firstState.photos)

        // When - second search
        viewModel.searchByTag(secondTag)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then - verify second search overwrites first
        val secondState = viewModel.uiState.value
        assertEquals(secondTag, secondState.searchQuery)
        assertFalse(secondState.isLoading)
        assertNotNull(secondState.photos)
    }

    @Test
    fun `when repository returns empty results, should still complete successfully`() = runTest(testDispatcher) {
        // Given
        val searchTag = "nonexistent"
        testRepository.setPhotoResults(searchTag, emptyList())

        // When
        viewModel.searchByTag(searchTag)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val finalState = viewModel.uiState.value
        assertEquals(searchTag, finalState.searchQuery)
        assertFalse(finalState.isLoading)
        assertNotNull(finalState.photos)
    }

    @Test
    fun `when repository call is in progress, should maintain loading state`() = runTest(testDispatcher) {
        // Given
        val searchTag = "slow"
        testRepository.setPhotoResults(searchTag, listOf(
            PhotoSummary(id = "1", title = "Slow Photo", url = "slow.jpg")
        ))

        // When
        viewModel.searchByTag(searchTag)
        // Don't advance dispatcher - simulates slow repository call

        // Then - should be loading
        val loadingState = viewModel.uiState.value
        assertEquals(searchTag, loadingState.searchQuery)
        assertTrue(loadingState.isLoading)
        assertNull(loadingState.photos)
    }

    private class TestFlickrRepository : FlickrRepository {
        private val photoResultsMap = mutableMapOf<String, List<PhotoSummary>>()

        fun setPhotoResults(tag: String, photos: List<PhotoSummary>) {
            photoResultsMap[tag] = photos
        }

        override suspend fun getPhotoResultsStream(tag: String): Flow<PagingData<PhotoSummary>> {
            val photos = photoResultsMap[tag] ?: emptyList()
            return flowOf(PagingData.from(photos))
        }

        override suspend fun getPhotoDetails(photoId: String): Flow<PhotoDetails?> {
            throw NotImplementedError("Not needed for PhotoSearchViewModel tests")
        }
    }
}