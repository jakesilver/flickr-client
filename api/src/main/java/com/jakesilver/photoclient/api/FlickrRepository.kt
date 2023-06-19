package com.jakesilver.photoclient.api

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface FlickrRepository {
    fun getPhotoResultsStream(tag: String): Flow<PagingData<PhotoSummary>>
    suspend fun getPhotoDetails(photoId: String): Flow<PhotoDetails?>
}

class FlickrRepositoryImpl constructor(private val service: PhotoService) : FlickrRepository {
    override fun getPhotoResultsStream(tag: String): Flow<PagingData<PhotoSummary>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = PAGE_SIZE),
            pagingSourceFactory = { FlickrPagingSource(service, tag) },
        ).flow
    }

    override suspend fun getPhotoDetails(photoId: String): Flow<PhotoDetails?> {
        return flow {
            emit(service.getPhotoDetails(photoId).photo)
        }
    }

    private companion object {
        private const val PAGE_SIZE = 10
    }
}
