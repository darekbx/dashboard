package com.darekbx.dashboard.di

import android.content.Context
import androidx.room.Room
import com.darekbx.dashboard.repository.crypto.BaseCryptoRepository
import com.darekbx.dashboard.repository.crypto.local.CryptoDao
import com.darekbx.dashboard.repository.crypto.local.CryptoDatabase
import com.darekbx.dashboard.repository.crypto.remote.coinapi.CoinApiRemoteRepository
import com.darekbx.dashboard.repository.imgw.local.WaterLevelDao
import com.darekbx.dashboard.repository.imgw.local.WaterLevelDatabase
import com.darekbx.dashboard.repository.imgw.remote.BaseWaterLevelRepository
import com.darekbx.dashboard.repository.imgw.remote.ImgwRepository
import com.darekbx.dashboard.repository.nbp.BaseNbpRepository
import com.darekbx.dashboard.repository.nbp.local.NbpDao
import com.darekbx.dashboard.repository.nbp.local.NbpDatabase
import com.darekbx.dashboard.repository.nbp.remote.NbpRemoteRepository
import com.darekbx.dashboard.repository.stockprice.BaseStockPriceRepository
import com.darekbx.dashboard.repository.stockprice.local.StockPriceDao
import com.darekbx.dashboard.repository.stockprice.local.StockPriceDatabase
import com.darekbx.dashboard.repository.stockprice.remote.platformio.PlatformIoRemoteRepository
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
        private const val CRYPTO_DATABASE = "dashboard-crypto-db"
        private const val STOCK_PRICE_DATABASE = "dashboard-stock-price-db"
        private const val WATER_LEVEL_DATABASE = "dashboard-water-level-db"
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

    @Provides
    fun provideCryptoRepository(
        okHttpClient: OkHttpClient,
        cryptoDao: CryptoDao
    ): BaseCryptoRepository =
        CoinApiRemoteRepository(okHttpClient, cryptoDao)

    @Provides
    fun provideCryptoDatabase(@ApplicationContext context: Context): CryptoDatabase =
        Room.databaseBuilder(
            context,
            CryptoDatabase::class.java, CRYPTO_DATABASE
        ).build()

    @Provides
    fun provideCryptoDao(cryptoDatabase: CryptoDatabase): CryptoDao =
        cryptoDatabase.cryptoDao()

    @Provides
    fun provideStockPriceRepository(
        okHttpClient: OkHttpClient,
        stockPriceDao: StockPriceDao
    ): BaseStockPriceRepository =
        PlatformIoRemoteRepository(okHttpClient, stockPriceDao)

    @Provides
    fun provideStockPriceDatabase(@ApplicationContext context: Context): StockPriceDatabase =
        Room.databaseBuilder(
            context,
            StockPriceDatabase::class.java, STOCK_PRICE_DATABASE
        ).build()

    @Provides
    fun provideStockPriceDao(stockPriceDatabase: StockPriceDatabase): StockPriceDao =
        stockPriceDatabase.stockPriceDao()

    @Provides
    fun provideWaterLevelRepository(
        okHttpClient: OkHttpClient,
        waterLevelDao: WaterLevelDao
    ): BaseWaterLevelRepository =
        ImgwRepository(okHttpClient, waterLevelDao)

    @Provides
    fun provideWaterLevelDatabase(@ApplicationContext context: Context): WaterLevelDatabase =
        Room.databaseBuilder(
            context,
            WaterLevelDatabase::class.java, WATER_LEVEL_DATABASE
        ).build()

    @Provides
    fun provideWaterLevelDao(waterLevelDatabase: WaterLevelDatabase): WaterLevelDao =
        waterLevelDatabase.waterLevelDao()
}
