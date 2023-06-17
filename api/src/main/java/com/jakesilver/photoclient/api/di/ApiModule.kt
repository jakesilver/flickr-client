package com.jakesilver.photoclient.api.di

import com.jakesilver.photoclient.api.BuildConfig
import com.jakesilver.photoclient.api.FlickrRepository
import com.jakesilver.photoclient.api.FlickrRepositoryImpl
import com.jakesilver.photoclient.api.PhotoService
import com.jakesilver.photoclient.api.PhotoServiceImpl
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

sealed class NamedConfiguration {
    object FlickrApiKey : NamedConfiguration()
}

val apiModule = module {
    single(qualifier<NamedConfiguration.FlickrApiKey>()) { BuildConfig.FLICKR_API_KEY }
    single<PhotoService> {
        PhotoServiceImpl(
            apiKey = get(qualifier<NamedConfiguration.FlickrApiKey>()),
        )
    }
    single<FlickrRepository> {
        FlickrRepositoryImpl(
            service = get(),
        )
    }
}
