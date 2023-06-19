package com.jakesilver.photoclient.api

import com.googlecode.flickrjandroid.Flickr
import com.googlecode.flickrjandroid.Flickr.SAFETYLEVEL_SAFE
import com.googlecode.flickrjandroid.photos.PhotosInterface
import com.googlecode.flickrjandroid.photos.SearchParameters
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date

interface PhotoService {
    suspend fun getPhotoSummariesByTag(
        tags: String,
        numImagePerPage: Int,
        page: Int,
    ): PhotoSummaryResponse

    suspend fun getPhotoDetails(photoId: String?): PhotoDetailsResponse
}

internal class PhotoServiceImpl(
    apiKey: String,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : PhotoService {
    private val flickr: PhotosInterface = Flickr(apiKey).photosInterface

    override suspend fun getPhotoSummariesByTag(
        tags: String,
        numImagePerPage: Int,
        page: Int,
    ): PhotoSummaryResponse {
        return withContext(dispatcher) {
            try {
                val photoList = flickr.search(
                    SearchParameters().apply {
                        this.tags = arrayOf(tags)
                        safeSearch = SAFETYLEVEL_SAFE
                    },
                    numImagePerPage,
                    page,
                )
                val photos = photoList.map { photo ->
                    PhotoSummary(
                        id = photo.id,
                        url = photo.mediumUrl,
                        title = photo.title,
                    )
                }
                return@withContext PhotoSummaryResponse(photos, photoList.page, photoList.total)
            } catch (exception: Exception) {
                return@withContext PhotoSummaryResponse(emptyList(), 1, 0)
            }
        }
    }

    override suspend fun getPhotoDetails(photoId: String?): PhotoDetailsResponse {
        return withContext(dispatcher) {
            try {
                val photo = flickr.getInfo(photoId, null)
                return@withContext PhotoDetailsResponse(
                    photo = PhotoDetails(
                        id = photo.id,
                        url = photo.largeUrl,
                        title = photo.title,
                        description = photo.description,
                        dateTaken = photo.dateTaken,
                        datePosted = photo.datePosted,
                    ),
                )
            } catch (exception: java.lang.Exception) {
                return@withContext PhotoDetailsResponse(error = Error.NoPhotoFound)
            }
        }
    }
}

sealed class Error {
    object NoPhotoFound : Error()
}

data class PhotoDetailsResponse(val photo: PhotoDetails? = null, val error: Error? = null)

data class PhotoSummaryResponse(
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
