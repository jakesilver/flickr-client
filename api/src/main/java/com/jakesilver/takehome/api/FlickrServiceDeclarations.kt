package com.jakesilver.takehome.api

import com.googlecode.flickrjandroid.Flickr
import com.googlecode.flickrjandroid.photos.PhotosInterface
import com.googlecode.flickrjandroid.photos.SearchParameters
import java.util.Date

interface PhotoService {
    suspend fun getPhotoSummariesByTag(tags: String, numImagePerPage: Int, page: Int): PhotoResponse
    suspend fun getPhotoDetails(photoId: String?): PhotoDetails
}

internal class PhotoServiceImpl(private val apiKey: String) : PhotoService {
    private val flickr: PhotosInterface = Flickr(apiKey).photosInterface

    override suspend fun getPhotoSummariesByTag(
        tags: String,
        numImagePerPage: Int,
        page: Int
    ): PhotoResponse {

        val photoList = flickr.search(
            SearchParameters().apply {
                this.tags = arrayOf(tags)
            },
            numImagePerPage,
            page
        )
        val photos = photoList.map { photo ->
            PhotoSummary(
                id = photo.id,
                url = photo.url,
                title = photo.title,
            )
        }
        return PhotoResponse(photos, photoList.page, photoList.total)
    }

    override suspend fun getPhotoDetails(photoId: String?): PhotoDetails {
        val photo = flickr.getPhoto(photoId)
        return PhotoDetails(
            id = photo.id,
            url = photo.url,
            title = photo.title,
            description = photo.description,
            dateTaken = photo.dateTaken,
            datePosted = photo.datePosted
        )
    }
}

data class PhotoResponse(
    val photoSummaries: List<PhotoSummary>,
    val pageOffset: Int,
    val totalPhotos: Int,
)

data class PhotoSummary(
    val id: String,
    val url: String,
    val title: String,
)

data class PhotoDetails(
    val id: String,
    val url: String,
    val title: String,
    val description: String,
    val dateTaken: Date,
    val datePosted: Date,
)
