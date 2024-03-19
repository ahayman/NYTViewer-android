package com.example.nytviewer.di

import com.example.nytviewer.navigation.AppNavigator
import com.example.nytviewer.navigation.NavGraph
import com.example.nytviewer.navigation.NavGraphInterface
import com.example.nytviewer.navigation.Navigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideNavigator(): Navigator = AppNavigator()

    @Provides
    @Singleton
    fun provideNavGraph(): NavGraphInterface = NavGraph()
}