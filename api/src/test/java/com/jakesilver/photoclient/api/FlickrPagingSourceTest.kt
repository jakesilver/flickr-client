package com.jakesilver.photoclient.api

import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.PagingConfig
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FlickrPagingSourceTest {

    @Test
    fun `load returns page with correct data on first page`() = runTest {
        // Given
        val fakeService = FakePhotoService()
        val pagingSource = FlickrPagingSource(fakeService, "sunset")
        val loadParams = PagingSource.LoadParams.Refresh<Int>(
            key = null,
            loadSize = 10,
            placeholdersEnabled = false
        )

        // When
        val result = pagingSource.load(loadParams)

        // Then
        assertTrue(result is PagingSource.LoadResult.Page)
        result as PagingSource.LoadResult.Page
        assertEquals(10, result.data.size)
        assertEquals("sunset_1", result.data[0].id)
        assertNull(result.prevKey)
        assertEquals(2, result.nextKey)
    }

    @Test
    fun `load returns page with correct data on second page`() = runTest {
        // Given
        val fakeService = FakePhotoService()
        val pagingSource = FlickrPagingSource(fakeService, "cats")
        val loadParams = PagingSource.LoadParams.Refresh<Int>(
            key = 2,
            loadSize = 5,
            placeholdersEnabled = false
        )

        // When
        val result = pagingSource.load(loadParams)

        // Then
        assertTrue(result is PagingSource.LoadResult.Page)
        result as PagingSource.LoadResult.Page
        assertEquals(5, result.data.size)
        assertEquals("cats_6", result.data[0].id) // Page 2, loadSize 5: items 6-10
        assertEquals(1, result.prevKey)
        assertEquals(3, result.nextKey)
    }

    @Test
    fun `load returns page with no next key when at last page`() = runTest {
        // Given
        val customPhotos = listOf(
            PhotoSummary("last1", "url1", "title1"),
            PhotoSummary("last2", "url2", "title2")
        )
        val fakeService = FakePhotoService(customPhotos = customPhotos)
        val pagingSource = FlickrPagingSource(fakeService, "test")
        val loadParams = PagingSource.LoadParams.Refresh<Int>(
            key = 1, // Request 10 items, but only 2 available
            loadSize = 10,
            placeholdersEnabled = false
        )

        // When
        val result = pagingSource.load(loadParams)

        // Then
        assertTrue(result is PagingSource.LoadResult.Page)
        result as PagingSource.LoadResult.Page
        assertEquals(2, result.data.size)
        assertNull(result.prevKey) // First page
        assertNull(result.nextKey) // No next key because photos.size (2) < loadSize (10)
    }

    @Test
    fun `load returns empty page when no photos available`() = runTest {
        // Given
        val fakeService = FakePhotoService(customPhotos = emptyList())
        val pagingSource = FlickrPagingSource(fakeService, "empty")
        val loadParams = PagingSource.LoadParams.Refresh<Int>(
            key = null,
            loadSize = 10,
            placeholdersEnabled = false
        )

        // When
        val result = pagingSource.load(loadParams)

        // Then
        assertTrue(result is PagingSource.LoadResult.Page)
        result as PagingSource.LoadResult.Page
        assertEquals(0, result.data.size)
        assertNull(result.prevKey)
        assertNull(result.nextKey)
    }

    @Test
    fun `load returns error when service throws exception`() = runTest {
        // Given
        val fakeService = FakePhotoService(shouldThrowException = true)
        val pagingSource = FlickrPagingSource(fakeService, "error")
        val loadParams = PagingSource.LoadParams.Refresh<Int>(
            key = null,
            loadSize = 10,
            placeholdersEnabled = false
        )

        // When
        val result = pagingSource.load(loadParams)

        // Then
        assertTrue(result is PagingSource.LoadResult.Error)
        result as PagingSource.LoadResult.Error
        assertTrue(result.throwable is RuntimeException)
        assertEquals("Network error", result.throwable.message)
    }

    @Test
    fun `load handles append params correctly`() = runTest {
        // Given
        val fakeService = FakePhotoService()
        val pagingSource = FlickrPagingSource(fakeService, "append")
        val loadParams = PagingSource.LoadParams.Append<Int>(
            key = 3,
            loadSize = 15,
            placeholdersEnabled = false
        )

        // When
        val result = pagingSource.load(loadParams)

        // Then
        assertTrue(result is PagingSource.LoadResult.Page)
        result as PagingSource.LoadResult.Page
        assertEquals(0, result.data.size) // Page 3, loadSize 15: would start at item 31, but only 25 total
        assertEquals(2, result.prevKey)
        assertNull(result.nextKey) // No next key because empty page
    }

    @Test
    fun `load handles prepend params correctly`() = runTest {
        // Given
        val fakeService = FakePhotoService()
        val pagingSource = FlickrPagingSource(fakeService, "prepend")
        val loadParams = PagingSource.LoadParams.Prepend<Int>(
            key = 5,
            loadSize = 8,
            placeholdersEnabled = false
        )

        // When
        val result = pagingSource.load(loadParams)

        // Then
        assertTrue(result is PagingSource.LoadResult.Page)
        result as PagingSource.LoadResult.Page
        assertEquals(0, result.data.size) // Page 5, loadSize 8: would start at item 33, but only 25 total
        assertEquals(4, result.prevKey)
        assertNull(result.nextKey) // No next key because empty page
    }

    @Test
    fun `getRefreshKey returns correct key when anchor position is available`() = runTest {
        // Given
        val fakeService = FakePhotoService()
        val pagingSource = FlickrPagingSource(fakeService, "refresh")
        
        // Create mock PagingState with anchor position
        val mockPages = listOf(
            PagingSource.LoadResult.Page(
                data = listOf(PhotoSummary("1", "url1", "title1")),
                prevKey = 1,
                nextKey = 3
            )
        )
        val pagingState = PagingState<Int, PhotoSummary>(
            pages = mockPages,
            anchorPosition = 0,
            config = PagingConfig(pageSize = 10),
            leadingPlaceholderCount = 0
        )

        // When
        val refreshKey = pagingSource.getRefreshKey(pagingState)

        // Then
        assertEquals(2, refreshKey) // prevKey + 1
    }

    @Test
    fun `getRefreshKey returns null when no anchor position`() = runTest {
        // Given
        val fakeService = FakePhotoService()
        val pagingSource = FlickrPagingSource(fakeService, "refresh")
        
        val pagingState = PagingState<Int, PhotoSummary>(
            pages = emptyList(),
            anchorPosition = null,
            config = PagingConfig(pageSize = 10),
            leadingPlaceholderCount = 0
        )

        // When
        val refreshKey = pagingSource.getRefreshKey(pagingState)

        // Then
        assertNull(refreshKey)
    }
}