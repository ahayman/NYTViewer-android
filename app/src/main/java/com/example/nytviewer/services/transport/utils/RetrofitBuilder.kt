package com.example.nytviewer.services.transport.utils

import com.example.nytviewer.BuildConfig
import com.example.nytviewer.services.transport.apis.NYTimesAPI
import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Object that holds the Retrofit Builder and API service instances.
 * Responsible for building the Retrofit instance and creating the API service.
 */
class RetrofitBuilder(private val provider: TransportKeysProvider, private val moshi: Moshi) {

    /**
     * Private function that builds and returns a Retrofit instance.
     * The instance is configured with a base URL and a GSON converter factory.
     *
     * @return A configured Retrofit instance.
     */
    private fun getRetrofit(): Retrofit {
        val clientBuilder = OkHttpClient.Builder()
            .readTimeout(120, TimeUnit.SECONDS)
            .connectTimeout(120, TimeUnit.SECONDS)
            .protocols(listOf(Protocol.HTTP_1_1))

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            clientBuilder.addInterceptor(loggingInterceptor)
        }

        val networkInterceptor = Interceptor { chain ->
            val request = chain.request()
            val url = request.url.newBuilder().addQueryParameter("api-key", provider.apiKey).build()
            val requestBuilder: Request.Builder = request.newBuilder().url(url)
            requestBuilder.header("Application", "application/json")
            requestBuilder.header("Content-Type", "application/json")
            chain.proceed(requestBuilder.build())
        }
        clientBuilder.addInterceptor(networkInterceptor)
        return Retrofit.Builder()
            .baseUrl(provider.baseUrl)
            .client(clientBuilder.build())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    /**
     * Returns an instance of Api Service
     *
     * This function uses the getRetrofit function to create a Retrofit instance, and then
     * uses that instance to create and return an API Service.
     */
    private fun createAPIService(): NYTimesAPI {
        return getRetrofit().create(NYTimesAPI::class.java)
    }

    /**
     * Creates and returns the full NYTimes API.
     */
    val apiService: NYTimesAPI by lazy {
        createAPIService()
    }
}
