package com.jakesilver.takehome.scintillate.di

import androidx.lifecycle.SavedStateHandle
import com.jakesilver.takehome.scintillate.PhotoViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { (handle: SavedStateHandle) ->
        PhotoViewModel(
            savedStateHandle = handle,
            repository = get()
        )
    }
}