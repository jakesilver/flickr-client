package com.jakesilver.photoclient.scintillate.di

import com.jakesilver.photoclient.scintillate.PhotoViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel {
        PhotoViewModel(
            repository = get(),
        )
    }
}
