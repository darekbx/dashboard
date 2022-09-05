package com.darekbx.dashboard.di

import com.darekbx.dashboard.repository.currency.CurrencyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class CommonModule {

    @Provides
    fun provideCurrencyRepository(): CurrencyRepository = CurrencyRepository()
}