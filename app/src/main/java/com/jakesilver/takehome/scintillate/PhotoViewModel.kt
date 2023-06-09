package com.jakesilver.takehome.scintillate

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.jakesilver.takehome.api.FlickrRepository
import me.tatarka.inject.annotations.Inject

@Inject
class PhotoViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val repository: FlickrRepository
) : ViewModel() {

    private var searchTag: String? = savedStateHandle["tag"]

    val photoSummaries = repository.getPhotoResultsStream(searchTag ?: "").cachedIn(viewModelScope)

    fun photoSummaryOnClick(id: String) {
        // TODO
    }
}