package com.example.nytviewer.di

import com.example.nytviewer.services.transport.TransportService
import com.example.nytviewer.services.transport.TransportServiceInterface
import com.example.nytviewer.services.transport.utils.RetrofitBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ServicesModule {

    @Provides
    @Singleton
    fun provideTransportService(retrofitBuilder: RetrofitBuilder): TransportServiceInterface =
        TransportService(retrofitBuilder.apiService)
}