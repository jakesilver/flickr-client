package com.jakesilver.takehome.api

import com.googlecode.flickrjandroid.Flickr
import com.googlecode.flickrjandroid.photos.PhotosInterface
import java.util.Date

class FlickrWrapperForInterview() {
    private val photosInterface: PhotosInterface

    init {
        val flickr = Flickr("YOUR-FLICKR-API-KEY")
        photosInterface = flickr.photosInterface
    }

    fun getPhotosByTag(tags: String, page: Int, numImagePerPage: Int) {
        TODO("not implemented")
    }

    fun getPhotoInfo(photoId: String?) {
        TODO("not implemented")
    }
}

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
