package com.jakesilver.takehome.scintillate.di

import com.jakesilver.takehome.scintillate.PhotoViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel {
        PhotoViewModel(
            savedStateHandle = get(),
            repository = get(),
        )
    }
}
