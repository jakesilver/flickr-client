package com.jakesilver.takehome.api

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface FlickrRepository {
    fun getPhotoResultsStream(tag: String): Flow<PagingData<PhotoSummary>>
    suspend fun getPhotoDetails(photoId: String): PhotoDetails
}

class FlickrRepositoryImpl constructor(private val service: PhotoService): FlickrRepository
{
    override fun getPhotoResultsStream(tag: String): Flow<PagingData<PhotoSummary>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = PAGE_SIZE),
            pagingSourceFactory = { FlickrPagingSource(service, tag) }
        ).flow
    }

    override suspend fun getPhotoDetails(photoId: String): PhotoDetails {
        return service.getPhotoDetails(photoId)
    }

    private companion object {
        private const val PAGE_SIZE = 20
    }

}