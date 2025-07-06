package com.jakesilver.photoclient.scintillate.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jakesilver.photoclient.api.FlickrRepository
import com.jakesilver.photoclient.api.PhotoSummary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PhotoSearchUiState(
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val photos: Flow<PagingData<PhotoSummary>>? = null
)

class PhotoSearchViewModel(
    private val repository: FlickrRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PhotoSearchUiState())
    val uiState: StateFlow<PhotoSearchUiState> = _uiState.asStateFlow()

    fun searchByTag(tag: String) {
        if (tag.isBlank()) return
        
        _uiState.value = _uiState.value.copy(
            searchQuery = tag,
            isLoading = true,
            photos = null
        )
        
        viewModelScope.launch {
            val photosFlow = repository.getPhotoResultsStream(tag).cachedIn(viewModelScope)
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                photos = photosFlow
            )
        }
    }
}
