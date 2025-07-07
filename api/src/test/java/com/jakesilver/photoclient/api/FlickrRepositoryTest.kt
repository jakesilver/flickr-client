package com.jakesilver.photoclient.api

import androidx.paging.testing.asSnapshot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class FlickrRepositoryTest {

    @Test
    fun `getPhotoResultsStream returns paging data with correct photos`() = runTest {
        // Given
        val fakeService = FakePhotoService()
        val repository = FlickrRepositoryImpl(fakeService)
        val tag = "sunset"

        // When
        val flow = repository.getPhotoResultsStream(tag)
        val items = flow.asSnapshot()

        // Then
        assertEquals(25, items.size) // Total photos in fake service
        assertEquals("${tag}_1", items[0].id)
        assertEquals("Fake Photo ${tag}_1", items[0].title)
        assertEquals("https://fake-url.com/${tag}_1.jpg", items[0].url)
    }

    @Test
    fun `getPhotoResultsStream with custom photos returns expected data`() = runTest {
        // Given
        val customPhotos = listOf(
            PhotoSummary("custom1", "url1", "title1"),
            PhotoSummary("custom2", "url2", "title2")
        )
        val fakeService = FakePhotoService(customPhotos = customPhotos)
        val repository = FlickrRepositoryImpl(fakeService)

        // When
        val flow = repository.getPhotoResultsStream("test")
        val items = flow.asSnapshot()

        // Then
        assertEquals(2, items.size)
        assertEquals("custom1", items[0].id)
        assertEquals("custom2", items[1].id)
    }

    @Test
    fun `getPhotoDetails returns photo details successfully`() = runTest {
        // Given
        val fakeService = FakePhotoService()
        val repository = FlickrRepositoryImpl(fakeService)
        val photoId = "test123"

        // When
        val photoDetails = repository.getPhotoDetails(photoId).first()

        // Then
        assertEquals(photoId, photoDetails?.id)
        assertEquals("Fake Photo $photoId", photoDetails?.title)
        assertEquals("https://fake-url.com/$photoId.jpg", photoDetails?.url)
        assertEquals("This is a fake photo description for $photoId", photoDetails?.description)
    }

    @Test
    fun `getPhotoDetails returns null when photo not found`() = runTest {
        // Given
        val fakeService = FakePhotoService(photoNotFound = true)
        val repository = FlickrRepositoryImpl(fakeService)

        // When
        val photoDetails = repository.getPhotoDetails("nonexistent").first()

        // Then
        assertNull(photoDetails)
    }

    @Test
    fun `getPhotoDetails returns null when photoId is empty`() = runTest {
        // Given
        val fakeService = FakePhotoService()
        val repository = FlickrRepositoryImpl(fakeService)

        // When
        val photoDetails = repository.getPhotoDetails("").first()

        // Then
        assertNull(photoDetails)
    }

    @Test
    fun `getPhotoDetails with custom photo details returns expected data`() = runTest {
        // Given
        val customPhotoDetails = PhotoDetails(
            id = "custom123",
            url = "custom-url",
            title = "Custom Title",
            description = "Custom Description",
            dateTaken = java.util.Date(0),
            datePosted = java.util.Date(1000)
        )
        val fakeService = FakePhotoService(customPhotoDetails = customPhotoDetails)
        val repository = FlickrRepositoryImpl(fakeService)

        // When
        val photoDetails = repository.getPhotoDetails("test").first()

        // Then
        assertEquals("custom123", photoDetails?.id)
        assertEquals("custom-url", photoDetails?.url)
        assertEquals("Custom Title", photoDetails?.title)
        assertEquals("Custom Description", photoDetails?.description)
    }
}