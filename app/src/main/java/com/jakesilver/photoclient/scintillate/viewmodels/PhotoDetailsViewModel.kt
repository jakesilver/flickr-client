package com.jakesilver.photoclient.scintillate.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jakesilver.photoclient.api.FlickrRepository
import com.jakesilver.photoclient.api.PhotoDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PhotoDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: FlickrRepository,
) : ViewModel() {

    private val photoId: String? = savedStateHandle.get<String>(PHOTO_ID_SAVED_STATE_KEY)

    private val _photoDetailsUiState = MutableStateFlow(PhotoDetailsUiState(isLoading = true))

    val photoDetailsUiState: StateFlow<PhotoDetailsUiState> = _photoDetailsUiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PhotoDetailsUiState(isLoading = true),
    )

    init {
        viewModelScope.launch {
            if (photoId != null) {
                getPhotoDetails(photoId)
            } else {
                _photoDetailsUiState.value = PhotoDetailsUiState(
                    photoDetails = null,
                    isLoading = false,
                    errorMessage = "No photo found.",
                )
            }
        }
    }

    private suspend fun getPhotoDetails(photoId: String) {
        repository.getPhotoDetails(photoId).collect { photoDetails ->
            _photoDetailsUiState.value = if (photoDetails != null) {
                PhotoDetailsUiState(
                    photoDetails = photoDetails,
                    isLoading = false,
                    errorMessage = null,
                )
            } else {
                PhotoDetailsUiState(
                    photoDetails = null,
                    isLoading = false,
                    errorMessage = "No photo found.",
                )
            }
        }
    }

    private companion object {
        private const val PHOTO_ID_SAVED_STATE_KEY = "photoId"
    }
}

data class PhotoDetailsUiState(
    val photoDetails: PhotoDetails? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)