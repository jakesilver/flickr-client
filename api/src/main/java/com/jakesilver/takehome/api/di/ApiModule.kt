package com.jakesilver.takehome.api.di

import com.jakesilver.takehome.api.BuildConfig
import com.jakesilver.takehome.api.FlickrRepository
import com.jakesilver.takehome.api.FlickrRepositoryImpl
import com.jakesilver.takehome.api.PhotoService
import com.jakesilver.takehome.api.PhotoServiceImpl
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

sealed class NamedConfiguration {
    object FlickrApiKey : NamedConfiguration()
}

val apiModule = module {
    single(qualifier<NamedConfiguration.FlickrApiKey>()) { BuildConfig.FLICKR_API_KEY }
    single<PhotoService> {
        PhotoServiceImpl(
            apiKey = get(qualifier<NamedConfiguration.FlickrApiKey>())
        )
    }
    single<FlickrRepository> {
        FlickrRepositoryImpl(
            service = get()
        )
    }
}