package com.jakesilver.photoclient.api

import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PhotoServiceTest {

    private val testDispatcher = StandardTestDispatcher()

    @Test
    fun `getPhotoSummariesByTag returns successful response with photos`() = runTest(testDispatcher) {
        // Given
        val fakeService = FakePhotoService()
        val tag = "nature"
        val numImagesPerPage = 5
        val page = 1

        // When
        val response = fakeService.getPhotoSummariesByTag(tag, numImagesPerPage, page)

        // Then
        assertEquals(5, response.photoSummaries.size)
        assertEquals(page, response.pageOffset)
        assertEquals(25, response.totalPhotos)
        
        val firstPhoto = response.photoSummaries[0]
        assertEquals("${tag}_1", firstPhoto.id)
        assertEquals("Fake Photo ${tag}_1", firstPhoto.title)
        assertEquals("https://fake-url.com/${tag}_1.jpg", firstPhoto.url)
    }

    @Test
    fun `getPhotoSummariesByTag returns empty list when no photos available`() = runTest(testDispatcher) {
        // Given
        val fakeService = FakePhotoService(customPhotos = emptyList())

        // When
        val response = fakeService.getPhotoSummariesByTag("empty", 10, 1)

        // Then
        assertTrue(response.photoSummaries.isEmpty())
        assertEquals(0, response.totalPhotos)
        assertEquals(1, response.pageOffset)
    }

    @Test
    fun `getPhotoSummariesByTag throws exception when service fails`() = runTest(testDispatcher) {
        // Given
        val fakeService = FakePhotoService(shouldThrowException = true)

        // When/Then
        try {
            fakeService.getPhotoSummariesByTag("test", 10, 1)
            assertTrue("Expected exception was not thrown", false)
        } catch (e: RuntimeException) {
            assertEquals("Network error", e.message)
        }
    }

    @Test
    fun `getPhotoSummariesByTag handles different page sizes correctly`() = runTest(testDispatcher) {
        // Given
        val fakeService = FakePhotoService()
        val tag = "test"
        val pageSize = 3
        val page = 2

        // When
        val response = fakeService.getPhotoSummariesByTag(tag, pageSize, page)

        // Then
        assertEquals(pageSize, response.photoSummaries.size)
        assertEquals(page, response.pageOffset)
        
        // Verify that photos have correct IDs for the page (page 2, size 3: items 4-6)
        val firstPhoto = response.photoSummaries[0]
        assertEquals("${tag}_4", firstPhoto.id) // First item on page 2
        
        val lastPhoto = response.photoSummaries[pageSize - 1]
        assertEquals("${tag}_6", lastPhoto.id) // Last item on page 2
    }

    @Test
    fun `getPhotoDetails returns successful response with photo details`() = runTest(testDispatcher) {
        // Given
        val fakeService = FakePhotoService()
        val photoId = "test123"

        // When
        val response = fakeService.getPhotoDetails(photoId)

        // Then
        assertNotNull(response.photo)
        assertNull(response.error)
        
        val photo = response.photo!!
        assertEquals(photoId, photo.id)
        assertEquals("Fake Photo $photoId", photo.title)
        assertEquals("https://fake-url.com/$photoId.jpg", photo.url)
        assertEquals("This is a fake photo description for $photoId", photo.description)
        assertNotNull(photo.dateTaken)
        assertNotNull(photo.datePosted)
    }

    @Test
    fun `getPhotoDetails returns error when photo not found`() = runTest(testDispatcher) {
        // Given
        val fakeService = FakePhotoService(photoNotFound = true)

        // When
        val response = fakeService.getPhotoDetails("nonexistent")

        // Then
        assertNull(response.photo)
        assertEquals(Error.NoPhotoFound, response.error)
    }

    @Test
    fun `getPhotoDetails returns error when photoId is null`() = runTest(testDispatcher) {
        // Given
        val fakeService = FakePhotoService()

        // When
        val response = fakeService.getPhotoDetails(null)

        // Then
        assertNull(response.photo)
        assertEquals(Error.NoPhotoFound, response.error)
    }

    @Test
    fun `getPhotoDetails returns error when photoId is empty`() = runTest(testDispatcher) {
        // Given
        val fakeService = FakePhotoService()

        // When
        val response = fakeService.getPhotoDetails("")

        // Then
        assertNull(response.photo)
        assertEquals(Error.NoPhotoFound, response.error)
    }

    @Test
    fun `getPhotoDetails throws exception when service fails`() = runTest(testDispatcher) {
        // Given
        val fakeService = FakePhotoService(shouldThrowException = true)

        // When/Then
        try {
            fakeService.getPhotoDetails("test")
            assertTrue("Expected exception was not thrown", false)
        } catch (e: RuntimeException) {
            assertEquals("Network error", e.message)
        }
    }

    @Test
    fun `getPhotoDetails with custom photo details returns expected data`() = runTest(testDispatcher) {
        // Given
        val customPhotoDetails = PhotoDetails(
            id = "custom456",
            url = "https://custom-url.com/photo.jpg",
            title = "Custom Photo Title",
            description = "Custom photo description",
            dateTaken = java.util.Date(1234567890000),
            datePosted = java.util.Date(1234567890000)
        )
        val fakeService = FakePhotoService(customPhotoDetails = customPhotoDetails)

        // When
        val response = fakeService.getPhotoDetails("test")

        // Then
        assertNotNull(response.photo)
        assertNull(response.error)
        
        val photo = response.photo!!
        assertEquals("custom456", photo.id)
        assertEquals("https://custom-url.com/photo.jpg", photo.url)
        assertEquals("Custom Photo Title", photo.title)
        assertEquals("Custom photo description", photo.description)
        assertEquals(java.util.Date(1234567890000), photo.dateTaken)
        assertEquals(java.util.Date(1234567890000), photo.datePosted)
    }

    @Test
    fun `getPhotoSummariesByTag with custom photos returns expected data`() = runTest(testDispatcher) {
        // Given
        val customPhotos = listOf(
            PhotoSummary("custom1", "url1", "Custom Title 1"),
            PhotoSummary("custom2", "url2", "Custom Title 2"),
            PhotoSummary("custom3", "url3", "Custom Title 3")
        )
        val fakeService = FakePhotoService(customPhotos = customPhotos)

        // When
        val response = fakeService.getPhotoSummariesByTag("test", 10, 1)

        // Then
        assertEquals(3, response.photoSummaries.size)
        assertEquals(1, response.pageOffset)
        assertEquals(3, response.totalPhotos) // Fake service returns custom photos size
        
        assertEquals("custom1", response.photoSummaries[0].id)
        assertEquals("url1", response.photoSummaries[0].url)
        assertEquals("Custom Title 1", response.photoSummaries[0].title)
        
        assertEquals("custom2", response.photoSummaries[1].id)
        assertEquals("custom3", response.photoSummaries[2].id)
    }
}