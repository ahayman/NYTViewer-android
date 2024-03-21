package com.example.nytviewer.di

import com.example.nytviewer.repos.articles.ArticlesRepo
import com.example.nytviewer.repos.articles.ArticlesRepoInterface
import com.example.nytviewer.services.transport.TransportServiceInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dependency Injection of all Repositories available in the app.
 */
@Module
@InstallIn(SingletonComponent::class)
class ReposModule {

    @Provides
    @Singleton
    fun provideArticlesRepository(transport: TransportServiceInterface): ArticlesRepoInterface =
        ArticlesRepo(transport)
}