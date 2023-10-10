package com.instagram.light.shared.di.module

import com.google.gson.GsonBuilder
import com.instagram.light.network.ApiService
import com.instagram.light.utils.Constants
import com.instagram.light.utils.NetworkUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun providesRetrofit(): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.addInterceptor(loggingInterceptor)
        okHttpClient.readTimeout(Constants.TIMEOUT, TimeUnit.SECONDS)
        okHttpClient.connectTimeout(Constants.TIMEOUT, TimeUnit.SECONDS)
        val gson = GsonBuilder().create()
        return Retrofit.Builder()
            .baseUrl(NetworkUtils.BASE_URL)
            .client(okHttpClient.build())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    fun providesService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}