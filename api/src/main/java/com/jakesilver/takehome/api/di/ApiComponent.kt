package com.jakesilver.takehome.api.di

import com.jakesilver.takehome.api.BuildConfig
import com.jakesilver.takehome.api.FlickrRepository
import com.jakesilver.takehome.api.PhotoService
import com.jakesilver.takehome.api.PhotoServiceImpl
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@Component
abstract class ApiComponent {
    abstract val repository: FlickrRepository

    @Provides fun photoService(): PhotoService = PhotoServiceImpl(BuildConfig.FLICKR_API_KEY)
}