package com.jakesilver.photoclient.api

import java.util.Date

class FakePhotoService(
    private val shouldThrowException: Boolean = false,
    private val photoNotFound: Boolean = false,
    private val customPhotos: List<PhotoSummary>? = null,
    private val customPhotoDetails: PhotoDetails? = null,
) : PhotoService {

    override suspend fun getPhotoSummariesByTag(
        tags: String,
        numImagePerPage: Int,
        page: Int,
    ): PhotoSummaryResponse {
        if (shouldThrowException) {
            throw RuntimeException("Network error")
        }

        val photos = customPhotos ?: createFakePhotoSummaries(tags, numImagePerPage, page)
        return PhotoSummaryResponse(
            photoSummaries = photos,
            pageOffset = page,
            totalPhotos = customPhotos?.size ?: 25 // Simulate total count
        )
    }

    override suspend fun getPhotoDetails(photoId: String?): PhotoDetailsResponse {
        if (shouldThrowException) {
            throw RuntimeException("Network error")
        }

        if (photoNotFound || photoId.isNullOrEmpty()) {
            return PhotoDetailsResponse(error = Error.NoPhotoFound)
        }

        val photoDetails = customPhotoDetails ?: createFakePhotoDetails(photoId)
        return PhotoDetailsResponse(photo = photoDetails)
    }

    private fun createFakePhotoSummaries(tag: String, count: Int, page: Int): List<PhotoSummary> {
        // Simulate finite data - let's say we have 25 total photos
        val totalPhotos = 25
        val startIndex = (page - 1) * count + 1
        val endIndex = minOf(startIndex + count - 1, totalPhotos)
        
        return if (startIndex <= totalPhotos) {
            (startIndex..endIndex).map { index ->
                val photoId = "${tag}_$index"
                PhotoSummary(
                    id = photoId,
                    url = "https://fake-url.com/$photoId.jpg",
                    title = "Fake Photo $photoId"
                )
            }
        } else {
            emptyList()
        }
    }

    private fun createFakePhotoDetails(photoId: String): PhotoDetails {
        return PhotoDetails(
            id = photoId,
            url = "https://fake-url.com/$photoId.jpg",
            title = "Fake Photo $photoId",
            description = "This is a fake photo description for $photoId",
            dateTaken = Date(1609459200000), // 2021-01-01
            datePosted = Date(1609459200000)
        )
    }
}