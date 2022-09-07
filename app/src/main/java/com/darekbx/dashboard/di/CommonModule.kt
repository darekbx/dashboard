package com.darekbx.dashboard.di

import android.content.Context
import androidx.room.Room
import com.darekbx.dashboard.repository.nbp.BaseNbpRepository
import com.darekbx.dashboard.repository.nbp.local.NbpDao
import com.darekbx.dashboard.repository.nbp.local.NbpDatabase
import com.darekbx.dashboard.repository.nbp.remote.NbpRemoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

@Module
@InstallIn(SingletonComponent::class)
class CommonModule {

    companion object {
        private const val APP_DATABASE = "dashboard-db"
    }

    @Provides
    fun provideCurrencyRepository(
        okHttpClient: OkHttpClient,
        nbpDao: NbpDao
    ): BaseNbpRepository =
        NbpRemoteRepository(okHttpClient, nbpDao)

    @Provides
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.HEADERS)
            })
            .build()

    @Provides
    fun provideCurrencyDatabase(@ApplicationContext context: Context): NbpDatabase =
        Room.databaseBuilder(
            context,
            NbpDatabase::class.java, APP_DATABASE
        ).build()

    @Provides
    fun provideCurrencyDao(nbpDatabase: NbpDatabase): NbpDao =
        nbpDatabase.nbpDao()
}
