package com.darekbx.dashboard.repository.nbp.remote

import com.darekbx.dashboard.BuildConfig
import com.darekbx.dashboard.model.Currency
import com.darekbx.dashboard.model.GoldPrice
import com.darekbx.dashboard.repository.CommonWrapper
import com.darekbx.dashboard.repository.nbp.BaseNbpRepository
import com.darekbx.dashboard.repository.nbp.local.entities.Currency as LocalCurrency
import com.darekbx.dashboard.repository.nbp.local.entities.GoldPrice as LocalGoldPrice
import com.darekbx.dashboard.repository.nbp.remote.model.GoldPrice as RemoteGoldPrice
import com.darekbx.dashboard.repository.nbp.local.NbpDao
import com.darekbx.dashboard.repository.nbp.remote.model.RatesWrapper
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.lang.IllegalStateException
import javax.inject.Inject

class NbpRemoteRepository @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val nbpDao: NbpDao
) : BaseNbpRepository {

    private val nbpService: NBPService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NBPService::class.java)
    }

    override suspend fun fetchGoldPriceData(goldPrice: GoldPrice): Result<CommonWrapper> {
        try {
            nbpService.fetchActualGoldPrice().firstOrNull()?.let { data ->
                val storedGoldPrices = nbpDao.listGoldPrices()

                val noNewData = storedGoldPrices.any { it.date == data.date }
                if (noNewData) {
                    return Result.success(CommonWrapper(data.date, storedGoldPrices.toPrices()))
                }

                val goldPrices = storedGoldPrices.toMutableList()
                val newGoldPriceEntry = LocalGoldPrice(
                    null,
                    gramTo1oz(data.price),
                    data.date
                )
                nbpDao.add(newGoldPriceEntry)
                goldPrices.add(newGoldPriceEntry)

                return Result.success(CommonWrapper(data.date, goldPrices.toPrices()))
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

    /**
     * Convert 1g of gold to 1oz price
     * @param price Price of one gram of gold
     */
    private fun gramTo1oz(price: Double) =
        price * ONE_OZ_MULTIPLIER

    override suspend fun fetchCurrencyData(currency: Currency): Result<CommonWrapper> {
        if (currency.from != Currency.CurrencyType.PLN) {
            throw IllegalStateException("Coversions from PLN are only allowed")
        }

        try {
            nbpService.fetchActualTable().firstOrNull()?.let { data ->
                val storedRates = nbpDao.listCurrencies(currency.from.name, currency.to.name)
                val actualRate = data.rates
                    .firstOrNull { it.code.lowercase() == currency.to.name.lowercase() }

                val noNewData = storedRates.any { it.date == data.date }
                if (noNewData) {
                    return Result.success(CommonWrapper(data.date, storedRates.toRates()))
                }

                val rates = storedRates.toMutableList()

                actualRate?.let { it ->
                    val newCurrencyEntry = LocalCurrency(
                        null,
                        it.value,
                        currency.from.name,
                        currency.to.name,
                        data.date
                    )
                    nbpDao.add(newCurrencyEntry)
                    rates.add(newCurrencyEntry)
                }

                return Result.success(CommonWrapper(data.date, rates.toRates()))
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

    private fun List<LocalCurrency>.toRates() = map { it.rate.toFloat() }

    private fun List<LocalGoldPrice>.toPrices() = map { it.price.toFloat() }

    private interface NBPService {

        @GET("exchangerates/tables/A?format=json")
        suspend fun fetchActualTable(): List<RatesWrapper>

        @GET("cenyzlota?format=json")
        suspend fun fetchActualGoldPrice(): List<RemoteGoldPrice>
    }

    companion object {
        private const val BASE_URL = "https://api.nbp.pl/api/"
        private const val ONE_OZ_MULTIPLIER = 28.3495
    }
}
