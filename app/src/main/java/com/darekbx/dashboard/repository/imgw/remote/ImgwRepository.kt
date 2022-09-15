package com.darekbx.dashboard.repository.imgw.remote

import android.util.Log
import com.darekbx.dashboard.BuildConfig
import com.darekbx.dashboard.model.WaterLevel
import com.darekbx.dashboard.repository.CommonWrapper
import com.darekbx.dashboard.repository.commonDateFormatter
import com.darekbx.dashboard.repository.imgw.local.WaterLevelDao
import com.darekbx.dashboard.repository.imgw.local.entities.WaterLevel as LocalWaterLevel
import com.darekbx.dashboard.repository.imgw.remote.model.StationWrapper
import com.darekbx.dashboard.repository.imgw.remote.model.WaterStateRecord
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

class ImgwRepository @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val waterLevelDao: WaterLevelDao
) : BaseWaterLevelRepository {

    private val imgwService: ImgwService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ImgwService::class.java)
    }

    override suspend fun fetchStationInfo(waterLevel: WaterLevel): Result<CommonWrapper> {
        try {
            val currentDate = commonDateFormatter.format(Date())
            val data = imgwService.fetchStationData(waterLevel.stationId)
            Log.v(TAG, "Received ${data.waterStateRecords.size} records")
            addNewEntries(data, waterLevel.stationId)
            val entries = loadEntries()

            return Result.success(CommonWrapper(
                currentDate,
                entries.map { it.value.toFloat() }
            ))
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
            return Result.failure(e.fillInStackTrace())
        }
    }

    private suspend fun loadEntries(): List<WaterStateRecord> =
        waterLevelDao
            .fetch()
            .map { WaterStateRecord("", it.value, it.date) }

    /**
     * @return How many rows were addded
     */
    private suspend fun addNewEntries(
        data: StationWrapper,
        stationId: Long
    ): Int {
        val lastEntry = waterLevelDao.fetchLast()
        Log.v(TAG, "Last entry from ${lastEntry?.date}")
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")

        val records = when (lastEntry) {
            null -> data.waterStateRecords
            else -> {
                val latestDate = LocalDateTime.parse(lastEntry.date, formatter)
                Log.v(TAG, "Append entries")
                data.waterStateRecords.filter {
                    val entryDate = LocalDateTime.parse(it.date, formatter)
                    entryDate > latestDate
                }
            }
        }
        if (records.isNotEmpty()) {
            Log.v(TAG, "Adding ${records.size} new records")
            val entries = records.map {
                LocalWaterLevel(null, it.value, it.date, stationId)
            }
            waterLevelDao.insert(entries)
        } else {
            Log.v(TAG, "No new records, skip")
        }

        return records.size
    }

    private interface ImgwService {

        @GET("station/hydro/")
        suspend fun fetchStationData(@Query("id") id: Long): StationWrapper
    }

    companion object {
        private const val BASE_URL = "https://hydro.imgw.pl/api/"
        private const val TAG = "ImgwRepository"
    }
}