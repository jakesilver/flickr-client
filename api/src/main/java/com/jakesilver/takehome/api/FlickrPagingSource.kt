package com.jakesilver.takehome.api

import androidx.paging.PagingSource
import androidx.paging.PagingState

class FlickrPagingSource(
    private val service: PhotoService,
    private val tag: String
) : PagingSource<Int, PhotoSummary>() {
    override fun getRefreshKey(state: PagingState<Int, PhotoSummary>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoSummary> {
        val page = params.key ?: 1
        return try {
            val response = service.getPhotoSummariesByTag(tag, page, params.loadSize)
            val photos = response.photoSummaries
            LoadResult.Page(
                data = photos,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (page == response.totalPhotos) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }
}