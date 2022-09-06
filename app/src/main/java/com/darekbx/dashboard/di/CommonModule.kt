package com.darekbx.dashboard.di

import com.darekbx.dashboard.repository.currency.BaseCurrencyRepository
import com.darekbx.dashboard.repository.currency.nbp.NbpCurrencyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

@Module
@InstallIn(SingletonComponent::class)
class CommonModule {

    @Provides
    fun provideCurrencyRepository(okHttpClient: OkHttpClient): BaseCurrencyRepository =
        NbpCurrencyRepository(okHttpClient)

    @Provides
    fun provideOkHttpClient(): OkHttpClient  =
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.HEADERS)
            })
            .build()
}