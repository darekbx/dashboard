package com.darekbx.dashboard.repository.crypto.remote.coinapi

import com.darekbx.dashboard.BuildConfig
import com.darekbx.dashboard.model.BitcoinPrice
import com.darekbx.dashboard.repository.CommonWrapper
import com.darekbx.dashboard.repository.crypto.BaseCryptoRepository
import com.darekbx.dashboard.repository.crypto.local.CryptoDao
import com.darekbx.dashboard.repository.crypto.remote.coinapi.model.Trade
import com.darekbx.dashboard.repository.crypto.local.entities.BitcoinPrice as LocalBitcoinPrice
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import javax.inject.Inject

class CoinApiRemoteRepository @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val cryptoDao: CryptoDao
) : BaseCryptoRepository {

    private val coinApiService: CoinApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CoinApiService::class.java)
    }

    override suspend fun fetchBitcoinPrice(bitcoinPrice: BitcoinPrice): Result<CommonWrapper> {
        try {
            coinApiService.fetchActualBitcoinPrice().firstOrNull()?.let { data ->
                val storedBitcoinPrices = cryptoDao.listBitcoinPrices()
                val noNewData =
                    storedBitcoinPrices.any { it.date.stripTime() == data.timeExchange.stripTime() }
                if (noNewData) {
                    return Result.success(
                        CommonWrapper(
                            data.timeExchange.stripTime(),
                            storedBitcoinPrices.toFloatList()
                        )
                    )
                }

                val bitcoinPrices = storedBitcoinPrices.toMutableList()

                if (data.price > 0.0) {
                    val newGoldPriceEntry = LocalBitcoinPrice(
                        null,
                        data.price,
                        data.timeExchange.stripTime()
                    )
                    cryptoDao.add(newGoldPriceEntry)
                    bitcoinPrices.add(newGoldPriceEntry)
                }

                return Result.success(
                    CommonWrapper(
                        data.timeExchange.stripTime(),
                        bitcoinPrices.toFloatList()
                    )
                )
            } ?: run {
                return Result.failure(Throwable("Null or empty reponse"))
            }
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
            return Result.failure(e.fillInStackTrace())
        }
    }

    private fun List<LocalBitcoinPrice>.toFloatList() = map { it.price.toFloat() }

    private fun String.stripTime() = this.split('T')[0]

    private interface CoinApiService {

        @Headers("X-CoinAPI-Key: ${BuildConfig.COIN_API_KEY}")
        @GET("trades/latest?limit=10&filter_symbol_id=BTC")
        suspend fun fetchActualBitcoinPrice(): List<Trade>
    }

    companion object {
        private const val BASE_URL = "https://rest.coinapi.io/v1/"
    }
}
