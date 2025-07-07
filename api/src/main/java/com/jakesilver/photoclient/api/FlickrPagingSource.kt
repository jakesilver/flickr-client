package com.jakesilver.photoclient.api

import androidx.paging.PagingSource
import androidx.paging.PagingState

class FlickrPagingSource(
    private val service: PhotoService,
    private val tag: String,
) : PagingSource<Int, PhotoSummary>() {
    override fun getRefreshKey(state: PagingState<Int, PhotoSummary>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoSummary> {
        return try {
            val page = params.key ?: 1
            val response = service.getPhotoSummariesByTag(tag, params.loadSize, page)
            val photos = response.photoSummaries
            LoadResult.Page(
                data = photos,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (photos.size < params.loadSize) null else page + 1,
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }
}
