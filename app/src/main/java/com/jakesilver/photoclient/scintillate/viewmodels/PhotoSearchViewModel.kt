package com.jakesilver.photoclient.scintillate.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jakesilver.photoclient.api.FlickrRepository
import com.jakesilver.photoclient.api.PhotoSummary
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest

class PhotoSearchViewModel(
    private val repository: FlickrRepository,
) : ViewModel() {

    private val _searchByTag = MutableStateFlow<String?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val photoSummaries: Flow<PagingData<PhotoSummary>> = _searchByTag
        .filterNotNull()
        .flatMapLatest { searchTag ->
            repository.getPhotoResultsStream(searchTag)
        }.cachedIn(viewModelScope)

    fun searchByTag(tag: String) {
        _searchByTag.value = tag
    }
}
