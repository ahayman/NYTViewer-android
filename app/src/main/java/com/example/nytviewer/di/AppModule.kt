package com.example.nytviewer.di

import com.example.nytviewer.navigation.AppNavigator
import com.example.nytviewer.navigation.NavGraph
import com.example.nytviewer.navigation.NavGraphInterface
import com.example.nytviewer.navigation.Navigator
import com.example.nytviewer.services.transport.utils.RetrofitBuilder
import com.example.nytviewer.services.transport.utils.TransportKeysProvider
import com.example.nytviewer.ui.theme.MutableThemeProvider
import com.example.nytviewer.ui.theme.ThemeProviders
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.Date
import javax.inject.Singleton

/**
 * Main Dependency Injection for a variety of components that need to be available throughout
 * the app.
 */
@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideNavigator(): Navigator = AppNavigator()

    @Provides
    @Singleton
    fun provideNavGraph(): NavGraphInterface = NavGraph()

    @Provides
    @Singleton
    fun provideTheme(): MutableThemeProvider = ThemeProviders.inMemoryProvider()

    @Provides
    @Singleton
    fun provideTransportKeysProvider() = object : TransportKeysProvider {
        override val apiKey: String = "3L0gAAnMGB3TtBTiYltEzaWVZK1mMKmA"
        override val baseUrl: String = "https://api.nytimes.com/svc/"
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(Date::class.java, Rfc3339DateJsonAdapter())
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(
        transportKeys: TransportKeysProvider,
        moshi: Moshi,
    ): RetrofitBuilder = RetrofitBuilder(transportKeys, moshi)
}