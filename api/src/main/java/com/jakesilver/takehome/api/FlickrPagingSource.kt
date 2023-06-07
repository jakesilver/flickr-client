package com.jakesilver.takehome.api

import androidx.paging.PagingSource
import androidx.paging.PagingState

class FlickrPagingSource(
    private val service: PhotoService,
    private val tag: String
) : PagingSource<Int, PhotoSummary>() {
    override fun getRefreshKey(state: PagingState<Int, PhotoSummary>): Int? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoSummary> {
        TODO("Not yet implemented")
    }

}