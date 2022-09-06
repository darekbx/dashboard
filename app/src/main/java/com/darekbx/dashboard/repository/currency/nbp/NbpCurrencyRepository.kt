package com.darekbx.dashboard.repository.currency.nbp

import com.darekbx.dashboard.BuildConfig
import com.darekbx.dashboard.model.Currency
import com.darekbx.dashboard.repository.currency.BaseCurrencyRepository
import com.darekbx.dashboard.repository.currency.CurrencyWrapper
import com.darekbx.dashboard.repository.currency.nbp.model.RatesWrapper
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.lang.IllegalStateException
import javax.inject.Inject

class NbpCurrencyRepository @Inject constructor(
    private val okHttpClient: OkHttpClient
) : BaseCurrencyRepository {

    private val nbpService: NBPService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NBPService::class.java)
    }

    override suspend fun fetchData(currency: Currency): CurrencyWrapper {
        if (currency.from != Currency.CurrencyType.PLN) {
            throw IllegalStateException("Coversions from PLN are only allowed")
        }

        try {
            nbpService.fetchActualTable().firstOrNull()?.let { data ->
                val actualRate = data.rates
                    .firstOrNull { it.code.lowercase() == currency.to.name.lowercase() }

                // TODO: store rates in room
                val rates = mutableListOf(
                    4.5F, 4.6f, 4.55f, 4.67F, 4.69F, 4.68F,
                    4.70F, 4.70F, 4.71F, 4.73F, 4.75F
                )
                actualRate?.run {
                    rates.add(value.toFloat())
                }

                return CurrencyWrapper(data.date, rates)
            } ?: run {
                return CurrencyWrapper(errorMessage = "Empty reponse")
            }
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
            return CurrencyWrapper(errorMessage = e.message)
        }
    }

    private interface NBPService {

        @GET("exchangerates/tables/A?format=json")
        suspend fun fetchActualTable(): List<RatesWrapper>
    }

    companion object {
        private const val BASE_URL = "https://api.nbp.pl/api/"
    }
}
