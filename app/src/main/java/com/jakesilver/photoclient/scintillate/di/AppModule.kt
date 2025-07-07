package com.jakesilver.photoclient.scintillate.di

import com.jakesilver.photoclient.scintillate.viewmodels.PhotoDetailsViewModel
import com.jakesilver.photoclient.scintillate.viewmodels.PhotoSearchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel {
        PhotoSearchViewModel(
            repository = get(),
        )
    }

    viewModel {
        PhotoDetailsViewModel(
            savedStateHandle = get(),
            repository = get()
        )
    }
}
