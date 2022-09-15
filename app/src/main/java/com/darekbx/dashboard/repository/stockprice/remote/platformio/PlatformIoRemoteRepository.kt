package com.darekbx.dashboard.repository.stockprice.remote.platformio

import com.darekbx.dashboard.BuildConfig
import com.darekbx.dashboard.repository.CommonWrapper
import com.darekbx.dashboard.repository.commonDateFormatter
import com.darekbx.dashboard.model.StockPrice as WidgetStockPrice
import com.darekbx.dashboard.repository.stockprice.BaseStockPriceRepository
import com.darekbx.dashboard.repository.stockprice.local.StockPriceDao
import com.darekbx.dashboard.repository.stockprice.remote.platformio.model.StockPrice
import com.darekbx.dashboard.repository.stockprice.local.entities.StockPrice as LocalStockPrice
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.sql.Date
import javax.inject.Inject

class PlatformIoRemoteRepository @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val stockPriceDao: StockPriceDao
) : BaseStockPriceRepository {

    private val platformIoService: PlatformIoService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PlatformIoService::class.java)
    }

    override suspend fun fetchStockPrice(stockPrice: WidgetStockPrice): Result<CommonWrapper> {
        try {
            val storedData = stockPriceDao.listStockPrices(stockPrice.companyCode)
            val currentDate = commonDateFormatter.format(java.util.Date())
            val latest = stockPriceDao.fetchLatest(stockPrice.companyCode)
            val fetchedForToday = latest.date == currentDate

            if (fetchedForToday) {
                return Result.success(
                    CommonWrapper(
                        currentDate,
                        storedData.toFloatList()
                    )
                )
            }

            platformIoService.fetchPreviousClosePrice(stockPrice.companyCode)
                ?.takeIf { it.results.isNotEmpty() }
                ?.let { resultsWrapper ->
                    val data = resultsWrapper.results.first()

                    val noNewData = storedData.any { it.date == data.timestamp.toDate() }
                    if (noNewData) {
                        return Result.success(
                            CommonWrapper(
                                data.timestamp.toDate(),
                                storedData.toFloatList()
                            )
                        )
                    }

                    val stockPrices = storedData.toMutableList()
                    val newEntry = LocalStockPrice(null, stockPrice.companyCode,
                        data.closePrice, data.timestamp.toDate())
                    stockPriceDao.add(newEntry)
                    stockPrices.add(newEntry)

                    return Result.success(
                        CommonWrapper(
                            data.timestamp.toDate(),
                            stockPrices.toFloatList()
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

    private fun List<LocalStockPrice>.toFloatList() = map { it.price.toFloat() }

    private fun Long.toDate(): String {
        val date = Date(this)
        return commonDateFormatter.format(date)
    }

    private interface PlatformIoService {

        @GET("aggs/ticker/{name}/prev?adjusted=true&apiKey=${BuildConfig.POLYGON_IO_API_KEY}")
        suspend fun fetchPreviousClosePrice(@Path("name") name: String): StockPrice?
    }

    companion object {
        private const val BASE_URL = "https://api.polygon.io/v2/"
    }
}
