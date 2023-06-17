package com.jakesilver.photoclient.scintillate

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jakesilver.photoclient.api.FlickrRepository
import com.jakesilver.photoclient.api.PhotoDetails
import com.jakesilver.photoclient.api.PhotoSummary
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PhotoViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: FlickrRepository,
) : ViewModel() {

    val photoId: String = savedStateHandle["photoId"] ?: ""

    private val _searchByTag = MutableStateFlow<String?>(null)
    private val _photoDetailUiState = MutableStateFlow(PhotoDetailUiState(isLoading = true))

    @OptIn(ExperimentalCoroutinesApi::class)
    val photoSummaries: Flow<PagingData<PhotoSummary>> = _searchByTag
        .filterNotNull()
        .flatMapLatest { searchTag ->
            repository.getPhotoResultsStream(searchTag)
        }.cachedIn(viewModelScope)

    val photoDetails: StateFlow<PhotoDetailUiState> = _photoDetailUiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PhotoDetailUiState(isLoading = false)
    )

    fun searchByTag(tag: String) {
        _searchByTag.value = tag
    }

    fun onPhotoClicked(photoId: String) {
        viewModelScope.launch {
            _photoDetailUiState.value = _photoDetailUiState.value.copy(isLoading = true)
            repository.getPhotoDetails(photoId).collect { photoDetails ->
                _photoDetailUiState.value =
                    PhotoDetailUiState(
                        photoDetails = photoDetails,
                        isLoading = false,
                        errorMessage = null
                    )
            }
        }
    }
}

data class PhotoDetailUiState(
    val photoDetails: PhotoDetails? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)